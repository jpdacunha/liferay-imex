package com.liferay.imex.filesystem.trigger.listener;

import com.liferay.imex.core.api.configuration.ImexConfigurationService;
import com.liferay.imex.core.api.configuration.model.ImexProperties;
import com.liferay.imex.filesystem.trigger.conffiguration.ImExFileSystemTriggerPropsKeys;
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
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Validator;

import java.util.Date;
import java.util.Map;
import java.util.Properties;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;

@Component(
		immediate = true, property = {

		}
, service = FilesystemTriggerListener.class)
public class FilesystemTriggerListener extends BaseMessageListener {
	
	private static final Log _log = LogFactoryUtil.getLog(FilesystemTriggerListener.class);
	
	private TriggerFactory _triggerFactory;
	private SchedulerEngineHelper _schedulerEngineHelper;
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	private ImexConfigurationService configurationService;
	
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
		
		ImexProperties imexProperties = new ImexProperties();
		configurationService.loadTriggerAndCoreConfiguration(this.getClass(), imexProperties);
		
		Properties rawProps = imexProperties.getProperties();
		
		String cronExpression = GetterUtil.getString(rawProps.get(ImExFileSystemTriggerPropsKeys.TRIGGER_CHECK_PERIOD_CONR));
		
		if (Validator.isNotNull(cronExpression)) {
			
			Trigger trigger = _triggerFactory.createTrigger(className, className, new Date(), null, cronExpression);

			SchedulerEntry schedulerEntry = new SchedulerEntryImpl(className, trigger);
		
			_schedulerEngineHelper.register(this, schedulerEntry, DestinationNames.SCHEDULER_DISPATCH);	
			
		} else {
			_log.error("Unable to start job : no cron expression is properly configured");
		}
		
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
