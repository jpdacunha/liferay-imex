package com.liferay.imex.filesystem.trigger;

import com.liferay.imex.core.api.ImexCoreService;
import com.liferay.imex.core.api.deploy.DeployDirEnum;
import com.liferay.imex.core.api.trigger.Trigger;
import com.liferay.imex.filesystem.trigger.service.FilesystemTriggerService;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;

@Component(
		immediate = true,
		property = {
				"service.ranking:Integer=10"
		},
		service = Trigger.class
)
public class FilesystemTrigger implements Trigger {
	
	private static final Log _log = LogFactoryUtil.getLog(FilesystemTrigger.class);
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected FilesystemTriggerService filesystemTriggerService;
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected ImexCoreService coreService;

	@Override
	public String getTriggerName() {
		return "File system trigger";
	}

	@Override
	public String getTriggerDescription() {
		return "Allow triggering IMEX using an action performed on filesystem";
	}
	
	@Activate
	public void activate() {
		_log.info("Starting initialisation of filesystem trigger");
		filesystemTriggerService.createMissingFiles();
		_log.info("Done");
	}

	@Override
	public void deploy() {
		
		_log.info("Deploying scripts ...");
		Bundle bundle = FrameworkUtil.getBundle(this.getClass());
		coreService.deployBundleFiles(DeployDirEnum.SCRIPTS, "/scripts", bundle);
		_log.info("Scripts succesfully deployed");
		
	}

	@Override
	public void undeploy() {
		
		Bundle bundle = FrameworkUtil.getBundle(this.getClass());
		
		_log.info("Undeploying [" + bundle.getSymbolicName() + "] ...");
		
		coreService.cleanBundleFiles(DeployDirEnum.SCRIPTS, "/scripts", bundle);
		
		filesystemTriggerService.cleanFiles();
		
		_log.info("[" + bundle.getSymbolicName() + "] succesfully undeployed");
		
	}
	
}