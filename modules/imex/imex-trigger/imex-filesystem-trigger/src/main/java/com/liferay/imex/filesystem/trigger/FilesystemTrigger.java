package com.liferay.imex.filesystem.trigger;

import com.liferay.imex.core.api.trigger.Trigger;

import org.osgi.service.component.annotations.Component;

@Component(
		immediate = true,
		property = {
				"service.ranking:Integer=10"
		},
		service = Trigger.class
)
public class FilesystemTrigger implements Trigger {

	@Override
	public String getTriggerName() {
		return "File system trigger";
	}

	@Override
	public String getTriggerDescription() {
		return "Allow triggering IMEX using an action performed on filesystem";
	}
	
}