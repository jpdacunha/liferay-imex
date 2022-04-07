package com.liferay.imex.core.api.trigger;

import com.liferay.imex.core.api.deploy.Deployable;

public interface Trigger extends Deployable {
	
	public String getTriggerName();
	
	public String getTriggerDescription();

}
