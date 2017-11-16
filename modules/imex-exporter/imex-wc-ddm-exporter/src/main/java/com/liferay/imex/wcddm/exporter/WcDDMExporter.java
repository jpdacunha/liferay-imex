package com.liferay.imex.wcddm.exporter;

import com.liferay.imex.core.api.exporter.Exporter;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.service.GroupLocalServiceUtil;

import java.io.File;
import java.util.List;
import java.util.Properties;

import org.osgi.service.component.annotations.Component;

@Component(
		immediate = true,
		property = {
			"imex.component.description=Web Content DDM exporter",
			"service.ranking:Integer=10"
		},
		
		service = Exporter.class
	)
public class WcDDMExporter implements Exporter {
	
	public static final String DESCRIPTION = "Web Content DDM exporter";
	
	public static final String DIR = "/wcontent";
	public static final String FILENAME = "template-structure.xml";
	
	private static final Log _log = LogFactoryUtil.getLog(WcDDMExporter.class);

	@Override
	public void doExport(Properties config, File destDir, long companyId, boolean debug) {
		
		_log.info("Do Wc export ....");
		
	}
	
	@Override
	public String getProcessDescription() {
		return "Web Content DDM export";
	}
	
	/*
	 * Return all raw groups from database
	 * 
	 * @return
	 * @throws SystemException
	 */
	private List<Group> getAllGroups() throws SystemException {

		List<Group> groups = GroupLocalServiceUtil.getGroups(QueryUtil.ALL_POS, QueryUtil.ALL_POS);
		return groups;

	}

}
