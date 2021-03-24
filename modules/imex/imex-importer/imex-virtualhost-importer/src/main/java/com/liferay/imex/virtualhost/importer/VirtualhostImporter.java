package com.liferay.imex.virtualhost.importer;

import com.liferay.counter.kernel.service.CounterLocalService;
import com.liferay.imex.core.api.importer.Importer;
import com.liferay.imex.core.api.importer.ProfiledImporter;
import com.liferay.imex.core.api.processor.ImexProcessor;
import com.liferay.imex.core.api.report.ImexExecutionReportService;
import com.liferay.imex.core.util.statics.CollectionUtil;
import com.liferay.imex.core.util.statics.FileUtil;
import com.liferay.imex.core.util.statics.GroupUtil;
import com.liferay.imex.virtualhost.FileNames;
import com.liferay.imex.virtualhost.importer.configuration.ImExVirtualHostImporterPropsKeys;
import com.liferay.imex.virtualhost.importer.model.ExtendedImexVirtualhost;
import com.liferay.imex.virtualhost.model.ImexVirtualhost;
import com.liferay.imex.virtualhost.service.VirtualhostCommonService;
import com.liferay.portal.kernel.exception.NoSuchCompanyException;
import com.liferay.portal.kernel.exception.NoSuchGroupException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.LayoutSet;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.LayoutSetLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.VirtualHostLocalService;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Validator;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.TreeMap;

import org.osgi.framework.Bundle;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;

@Component(
		immediate = true,
		property = {
			"imex.component.execution.priority=20",
			"imex.component.description=Virtualhost importer",
			"service.ranking:Integer=10"
		},
		service = Importer.class
	)
public class VirtualhostImporter implements ProfiledImporter {
	
	private static final String DEFAULT_LANGUAGE_MESSAGE = "DEFAULT-LANGUAGE";

	private static final String DESCRIPTION = "VIRTUALHOST import";
	
	private static final Log _log = LogFactoryUtil.getLog(VirtualhostImporter.class);
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected ImexExecutionReportService reportService;
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected ImexProcessor processor;
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected GroupLocalService groupLocalService;
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected CompanyLocalService companyLocalService;
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected VirtualHostLocalService virtualHostLocalService;
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected LayoutSetLocalService layoutSetLocalService;
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected CounterLocalService counterLocalService;
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected VirtualhostCommonService virtualHostCommonService;

	@Override
	public void doImport(Bundle bundle, ServiceContext rootServiceContext, User exportUser, Properties config, File srcDir, long companyId, Locale locale, boolean debug) {
		
		reportService.getStartMessage(_log, "Virtualhost import process");
		
		boolean enabled = GetterUtil.getBoolean(config.get(ImExVirtualHostImporterPropsKeys.IMPORT_VIRTUALHOST_ENABLED));
		
		if (enabled) {
		
			try {
				
				if (srcDir == null || !srcDir.exists()){
					
					reportService.getDNE(_log, srcDir);
					
				} else {
					
					List<ExtendedImexVirtualhost> virtualHosts = deserialiseFiles(srcDir);
					
					if (virtualHosts.size() > 0) {
											
						Map<Long, TreeMap<String, String>> layoutSetHostnames = converToUpdatableMap(companyId, virtualHosts);
						
						updateVirtualHosts(companyId, locale, layoutSetHostnames);
						
					} else {
						reportService.getEmpty(_log, "virtualhost list");
					}	
					
				}
							
			} catch (Exception e) {
				_log.error(e,e);
				reportService.getError(_log, e); 
			}
			
		} else {
			reportService.getDisabled(_log, DESCRIPTION);
		}
		
		reportService.getEndMessage(_log, "Virtualhost import process");
		
	}

	private void updateVirtualHosts(long companyId, Locale locale, Map<Long, TreeMap<String, String>> layoutSetHostnames) throws PortalException {
		
		//Updating bunch of virtualhosts for each layoutSet - The Liferay method destriy all existing virtualhosts first
		for (Entry<Long, TreeMap<String,String>> entry : layoutSetHostnames.entrySet()) {
			
			long layoutSetId = entry.getKey();
			TreeMap<String,String> currentLayoutSetHostnames = entry.getValue();
			
			virtualHostLocalService.updateVirtualHosts(companyId, layoutSetId, currentLayoutSetHostnames);
			
			Company company = companyLocalService.getCompanyById(companyId);
			
			String name = "Company : " + company.getName();
			
			if (layoutSetId != VirtualhostCommonService.DEFAULT_LAYOUTSET_ID) {
				LayoutSet layoutSet = layoutSetLocalService.getLayoutSet(layoutSetId);
				Group group = layoutSet.getGroup();
				name = "Site : " + GroupUtil.getGroupName(group, locale);
			}
			
			//To avoid diplaying null in logs
			Map<String,String> toDisplay = new HashMap<String,String>();
			toDisplay.putAll(currentLayoutSetHostnames);	
			toDisplay = CollectionUtil.replaceNullValues(toDisplay, DEFAULT_LANGUAGE_MESSAGE);
			
			reportService.getOK(_log, name, "Virtual Hosts : "  + toDisplay);
			
		}
		
	}

	private Map<Long, TreeMap<String, String>> converToUpdatableMap(long companyId, List<ExtendedImexVirtualhost> virtualHosts) throws PortalException {
		
		//Constructing structured map to update
		Map<Long, TreeMap<String, String>> layoutSetHostnames = new HashMap<Long, TreeMap<String,String>>();
		
		for (ExtendedImexVirtualhost extendedVirtualHostObj : virtualHosts) {
			
			ImexVirtualhost virtualHostObj = extendedVirtualHostObj.getVirtualhost();
			
			String companyWebId = virtualHostObj.getCompanyWebId();
			String groupFriendlyURL = virtualHostObj.getGroupFriendlyURL();
			boolean isPublicVirtualHost = virtualHostObj.isPublicVirtualHost();
			long layoutSetId = VirtualhostCommonService.DEFAULT_LAYOUTSET_ID;
			boolean isCompanyVirtualHost = virtualHostObj.isCompanyVirtualHost();
			File virtualHostFile = extendedVirtualHostObj.getSourceFile();
			String originFileName = virtualHostFile.getName();
			String languageId = virtualHostObj.getLanguageId();
			
			try {
				
				//Verify if company exists
				companyLocalService.getCompanyByWebId(companyWebId);
				
				if (!isCompanyVirtualHost) {
					
					//Verify if group exists
					Group group = groupLocalService.getFriendlyURLGroup(companyId, groupFriendlyURL);
					
					LayoutSet layoutSet = layoutSetLocalService.getLayoutSet(group.getGroupId(), !isPublicVirtualHost);
					
					layoutSetId = layoutSet.getLayoutSetId();
					
				}
				
				String hostname = virtualHostObj.getHostname();
				TreeMap<String, String> currentLayoutSetHostnames = null;
				
				if (Validator.isNotNull(hostname)) {
				
					TreeMap<String, String> treeMap = layoutSetHostnames.get(layoutSetId);
					
					if (treeMap == null) {
						
						layoutSetHostnames.put(layoutSetId, new TreeMap<String, String>());
						
					} else {
						_log.debug("Updating existing TreeMap");
					}
					
					currentLayoutSetHostnames = layoutSetHostnames.get(layoutSetId);
					
					if (!currentLayoutSetHostnames.containsKey(hostname)) {
						currentLayoutSetHostnames.put(hostname, languageId);
					} else {
						_log.debug("Current TreeMap already contains [" + hostname + "]");
					}
					
				} else {
					reportService.getError(_log, originFileName, "missing required information hostname");
				}
				
			} catch(NoSuchCompanyException e) {
				reportService.getDNE(_log, "company identified by [" + companyWebId + "]");
				reportService.getSkipped(_log, originFileName);
			} catch(NoSuchGroupException e) {
				reportService.getDNE(_log, "group identified by [" + groupFriendlyURL + "]");
				reportService.getSkipped(_log, originFileName);
			}
			
		}
		return layoutSetHostnames;
		
	}

	private List<ExtendedImexVirtualhost> deserialiseFiles(File srcDir) throws Exception {
		
		File[] files = FileUtil.listFilesByExtension(srcDir, processor.getFileExtension());
		
		//Constructing deserialized list
		List<ExtendedImexVirtualhost> virtualHosts = new ArrayList<ExtendedImexVirtualhost>();
		
		for (File virtualHostFile : files) {
			ImexVirtualhost virtualHostObj = (ImexVirtualhost)processor.read(ImexVirtualhost.class, virtualHostFile);
			virtualHosts.add(new ExtendedImexVirtualhost(virtualHostFile, virtualHostObj));
		}
		
		if (virtualHosts.size() > 0) {
			//Ordering Default virtualhosts first
			Collections.sort(virtualHosts, new Comparator<ExtendedImexVirtualhost>(){
	
		        @Override
		        public int compare(ExtendedImexVirtualhost mall1, ExtendedImexVirtualhost mall2){
	
		            boolean b1 = mall1.isDefaultVirtualHost();
		            boolean b2 = mall2.isDefaultVirtualHost();
		         
		            return  Boolean.compare(b1, b2);
		        }
		        
		    });
		}
	
		return virtualHosts;
		
	}
	
	@Override
	public String getProcessDescription() {
		return DESCRIPTION;
	}

	public ImexExecutionReportService getReportService() {
		return reportService;
	}

	public void setReportService(ImexExecutionReportService reportService) {
		this.reportService = reportService;
	}
	
	@Override
	public String getRootDirectoryName() {
		return FileNames.DIR_VIRTUALHOST;
	}

	public ImexProcessor getProcessor() {
		return processor;
	}

	public void setProcessor(ImexProcessor processor) {
		this.processor = processor;
	}

	public GroupLocalService getGroupLocalService() {
		return groupLocalService;
	}

	public void setGroupLocalService(GroupLocalService groupLocalService) {
		this.groupLocalService = groupLocalService;
	}

	public CompanyLocalService getCompanyLocalService() {
		return companyLocalService;
	}

	public void setCompanyLocalService(CompanyLocalService companyLocalService) {
		this.companyLocalService = companyLocalService;
	}

	public VirtualHostLocalService getVirtualHostLocalService() {
		return virtualHostLocalService;
	}

	public void setVirtualHostLocalService(VirtualHostLocalService virtualHostLocalService) {
		this.virtualHostLocalService = virtualHostLocalService;
	}

	public LayoutSetLocalService getLayoutSetLocalService() {
		return layoutSetLocalService;
	}

	public void setLayoutSetLocalService(LayoutSetLocalService layoutSetLocalService) {
		this.layoutSetLocalService = layoutSetLocalService;
	}

	public CounterLocalService getCounterLocalService() {
		return counterLocalService;
	}

	public void setCounterLocalService(CounterLocalService counterLocalService) {
		this.counterLocalService = counterLocalService;
	}

	public VirtualhostCommonService getVirtualHostCommonService() {
		return virtualHostCommonService;
	}

	public void setVirtualHostCommonService(VirtualhostCommonService virtualHostCommonService) {
		this.virtualHostCommonService = virtualHostCommonService;
	}

}
