package com.liferay.imex.wcddm.importer;

import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.imex.core.api.importer.Importer;
import com.liferay.imex.core.api.processor.ImexProcessor;
import com.liferay.imex.core.util.statics.FileUtil;
import com.liferay.imex.core.util.statics.GroupUtil;
import com.liferay.imex.core.util.statics.ImexNormalizer;
import com.liferay.imex.core.util.statics.MessageUtil;
import com.liferay.imex.wcddm.FileNames;
import com.liferay.imex.wcddm.importer.configuration.ImExWCDDmImporterPropsKeys;
import com.liferay.imex.wcddm.importer.service.ImportWcDDMService;
import com.liferay.imex.wcddm.model.ImExStructure;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.GroupLocalServiceUtil;
import com.liferay.portal.kernel.util.GetterUtil;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;

@Component(
		immediate = true,
		property = {
			"imex.component.execution.priority=20",
			"imex.component.description=Web Content DDM importer",
			"service.ranking:Integer=10"
		},
		service = Importer.class
	)
public class WcDDMImporter implements Importer {
	
	private static final Log _log = LogFactoryUtil.getLog(WcDDMImporter.class);
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected ImportWcDDMService importWcDDMService;
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected ImexProcessor processor;

	@Override
	public void doImport(User user, Properties config, File companyDir, long companyId, Locale locale, boolean debug) {
		
		_log.info(MessageUtil.getStartMessage("Web Content DDM import process"));
		
		boolean enabled = GetterUtil.getBoolean(config.get(ImExWCDDmImporterPropsKeys.IMPORT_WCDDM_ENABLED));
		
		if (enabled) {
		
			try {
				
				File dir = getWCDDMImportDirectory(companyDir);
				
				File[] groupDirs = FileUtil.listFiles(dir);
				for (File groupDir : groupDirs) {
					this.doImport(companyId, user, config, groupDir, locale, debug);
				}
				
				
			} catch (Exception e) {
				_log.error(e,e);
				_log.error(MessageUtil.getErrorMessage(e)); 
			}
			
		} else {
			_log.info(MessageUtil.getDisabled("Web Content DDM import"));
		}
		
		_log.info(MessageUtil.getEndMessage("Web Content DDM import process"));		
	}
	
	private void doImport(long companyId, User user, Properties config, File groupDir, Locale locale, boolean debug) throws IOException {
			
		if (groupDir != null) {
			
			if (groupDir.exists()) {
				
				String groupFriendlyUrl = ImexNormalizer.getFriendlyURLByDirName(groupDir.getName());
				
				Group group = GroupLocalServiceUtil.fetchFriendlyURLGroup(companyId, groupFriendlyUrl);
				
				if (group != null) {
					
					String groupName = GroupUtil.getGroupName(group, locale);
					
					File[] structuresDirs = FileUtil.listFiles(groupDir);
					
					//For each structure dir
					for (File structureDir : structuresDirs) {
						
						DDMStructure structure = doImportStructure(debug, groupName, structureDir);
							
					}
					
				} else {
					_log.warn(MessageUtil.getDNE(groupFriendlyUrl));
				}
				
			} else {
				_log.warn(MessageUtil.getDNE(groupDir.getAbsolutePath()));
			}
			
			
		} else {
			_log.error("Skipping null roleDir ...");
		}
	}

	private DDMStructure doImportStructure(boolean debug, String groupName, File structureDir) throws IOException {
		
		String structurefileBegin = FileNames.getStructureFileNameBegin();
		
		File[] structureFiles = FileUtil.listFiles(structureDir, structurefileBegin);
		if (structureFiles != null && structureFiles.length == 1) {
			
			try {
				
				String structureFileName = structureFiles[0].getName();
				ImExStructure imexStructure = (ImExStructure)processor.read(ImExStructure.class, structureDir, structureFileName);
				//FIXME virer cela
				_log.info(imexStructure.getData());
				_log.info(MessageUtil.getOK(groupName, structureDir.getName()));
				
			} catch (Exception e) {
				_log.error(MessageUtil.getError(structureDir.getName(), e.getMessage()));
				if (debug) {
					_log.error(e,e);
				}
			}
			
		} else {
			_log.info(MessageUtil.getError("Wrong number of files", "[" + structureDir.getPath() + "] cannot contain more than one file matching [" + structurefileBegin + "]"));
		}
		
		return null;
		
	}

	private File getWCDDMImportDirectory(File companyDir) {
		
		File rolesDir = new File(companyDir, FileNames.DIR_WCDDM);
		
		if (rolesDir.exists()) {
			return rolesDir;
		} else {
			_log.warn("[" + rolesDir.getAbsolutePath() + "] does not exists");
		}
		
		return null;
		
	}

	@Override
	public String getProcessDescription() {
		return "Web Content DDM import";
	}

}
