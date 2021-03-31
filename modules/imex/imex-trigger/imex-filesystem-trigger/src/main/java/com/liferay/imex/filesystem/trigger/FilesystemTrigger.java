package com.liferay.imex.filesystem.trigger;

import com.liferay.imex.core.api.trigger.Trigger;
import com.liferay.imex.filesystem.trigger.service.FilesystemTriggerService;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

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
	
}