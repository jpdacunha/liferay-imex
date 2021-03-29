package com.liferay.imex.filesystem.trigger.listener;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.messaging.BaseMessageListener;
import com.liferay.portal.kernel.messaging.DestinationNames;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.module.framework.ModuleServiceLifecycle;
import com.liferay.portal.kernel.scheduler.SchedulerEngineHelper;
import com.liferay.portal.kernel.scheduler.SchedulerEntry;
import com.liferay.portal.kernel.scheduler.SchedulerEntryImpl;
import com.liferay.portal.kernel.scheduler.Trigger;
import com.liferay.portal.kernel.scheduler.TriggerFactory;

import java.util.Date;
import java.util.Map;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;

@Component(
		immediate = true, property = {

		}
, service = FilesystemTriggerListener.class)
public class FilesystemTriggerListener extends BaseMessageListener {
	
	private static final Log _log = LogFactoryUtil.getLog(FilesystemTriggerListener.class);
	
	private TriggerFactory _triggerFactory;
	private SchedulerEngineHelper _schedulerEngineHelper;
	
	@Override
	protected void doReceive(Message message) throws Exception {
		_log.info("Test ....");
	}
	
	@Modified
	@Activate
	protected void activate(Map<String, Object> properties) {
		
		_log.info("Registering scheduler entry for [" + this.getClass().getSimpleName() + "] ...");
		
		Class<?> clazz = getClass();

		String className = clazz.getName();
		
		String cronExpression = "0/30 * * * * ? *";

		Trigger trigger = _triggerFactory.createTrigger(className, className, new Date(), null, cronExpression);

		SchedulerEntry schedulerEntry = new SchedulerEntryImpl(className, trigger);
	
		_schedulerEngineHelper.register(this, schedulerEntry, DestinationNames.SCHEDULER_DISPATCH);
		
		_log.info("Done.");
		
	}	
	
	@Deactivate
	protected void deactivate() {
		_schedulerEngineHelper.unregister(this);
	}
	
	@Reference(target = ModuleServiceLifecycle.PORTAL_INITIALIZED, unbind = "-")
	protected void setModuleServiceLifecycle(ModuleServiceLifecycle moduleServiceLifecycle) {
	}

	@Reference(unbind = "-")
	protected void setTriggerFactory(TriggerFactory triggerFactory) {
		_triggerFactory = triggerFactory;
	}

	@Reference(unbind = "-")
	protected void setSchedulerEngineHelper(SchedulerEngineHelper schedulerEngineHelper) {
		_schedulerEngineHelper = schedulerEngineHelper;
	}

}
