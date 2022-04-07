package com.liferay.imex.shell.trigger;

import com.liferay.imex.core.api.trigger.Trigger;

import org.osgi.service.component.annotations.Component;

@Component(
		immediate = true,
		property = {
				"service.ranking:Integer=10"
		},
		service = Trigger.class
)
public class GogoShellTrigger implements Trigger {

	@Override
	public String getTriggerName() {
		return "Gogo shell trigger";
	}

	@Override
	public String getTriggerDescription() {
		return "Allow triggering IMEX using Gogo shell custom commands";
	}

	@Override
	public void deploy() {
		
	}

	@Override
	public void undeploy() {
		
	}

}
