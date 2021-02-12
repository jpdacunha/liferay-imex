package com.liferay.imex.core.service;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Version;

/**
 * @author dev
 */
public class ImexCoreActivator implements BundleActivator {
	
	private static final Log _log = LogFactoryUtil.getLog(ImexCoreActivator.class);
	
	@Override
	public void start(BundleContext bundleContext) throws Exception {
		
		Bundle bundle = bundleContext.getBundle();
		Version version = bundle.getVersion();
		
		int state = bundle.getState();
		
		//State started or updated
		if (state == 2 || state == 8) {
			
			_log.info("##");
			_log.info("## IMEX core : (IMport EXport) v" + version +  " for Liferay DXP (7.2 EE) is starting ...");
			_log.info("## By Jean-Paul DA CUNHA");
			_log.info("##"); 
			
		} else if (state == 32 || state == 1) {
			_log.info("## IMEX (IMport EXport) v" + version +  " has failed to start.");
		}
		
	}
	
	/*private static String typeAsString(BundleEvent event) {
		if (event == null) {
			return "null";
		}
		int type = event.getType();
		switch (type) {
		case BundleEvent.INSTALLED:
			return "INSTALLED";
		case BundleEvent.LAZY_ACTIVATION:
			return "LAZY_ACTIVATION";
		case BundleEvent.RESOLVED:
			return "RESOLVED";
		case BundleEvent.STARTED:
			return "STARTED";
		case BundleEvent.STARTING:
			return "Starting";
		case BundleEvent.STOPPED:
			return "STOPPED";
		case BundleEvent.UNINSTALLED:
			return "UNINSTALLED";
		case BundleEvent.UNRESOLVED:
			return "UNRESOLVED";
		case BundleEvent.UPDATED:
			return "UPDATED";
		default:
			return "unknown event type: " + type;
		}
}*/

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		_log.info("## IMEX (IMport EXport) is now stopped");
	}

}