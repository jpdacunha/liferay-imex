package com.liferay.imex.core.service;

import java.io.File;
import java.util.Map;

import org.osgi.framework.ServiceReference;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import com.liferay.imex.core.api.ImexExportService;
import com.liferay.imex.core.api.exporter.Exporter;
import com.liferay.imex.core.api.exporter.ExporterTracker;
import com.liferay.imex.core.util.configuration.ImExPropsKeys;
import com.liferay.imex.core.util.configuration.ImExPropsValues;
import com.liferay.imex.core.util.statics.MessageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.PrefsPropsUtil;

@Component(immediate = true, service = ImexExportService.class)
public class ImexExportServiceImpl implements ImexExportService {
	
	private static final Log _log = LogFactoryUtil.getLog(ImexExportServiceImpl.class);
	
	private ExporterTracker trackerService;

	@Override
	public void doExport() {
		
		_log.info(MessageUtil.getStartMessage("export process"));
		
		File deployDir = new File(PrefsPropsUtil.getString(ImExPropsKeys.DEPLOY_DIR, ImExPropsValues.DEPLOY_DIR));
		
		_log.info(MessageUtil.getPropertyMessage("deployDir", deployDir.toString()));
		
		Map<String, ServiceReference<Exporter>> exporters = trackerService.getExporters();
		
		if (exporters != null && exporters.size() > 0) {
			
			for (Map.Entry<String ,ServiceReference<Exporter>> entry  : exporters.entrySet()) {
				
				ServiceReference<Exporter> reference = entry.getValue();
				
				Exporter exporter = reference.getBundle().getBundleContext().getService(reference);
				
				_log.info(MessageUtil.getStartMessage(exporter.getProcessDescription(), 1));
				
				exporter.doExport();
				
				_log.info(MessageUtil.getEndMessage(exporter.getProcessDescription(), 1));
				
			}
			
		} else {
			_log.info(MessageUtil.getMessage("No registered exporters"));
		}
		
		_log.info(MessageUtil.getEndMessage("export process"));

	}
	
	@Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.OPTIONAL)
	protected void setExporterTracker(ExporterTracker trackerService) {
		this.trackerService = trackerService;
	}

	protected void unsetExporterTracker(ExporterTracker trackerService) {
		this.trackerService = null;
	}

}
