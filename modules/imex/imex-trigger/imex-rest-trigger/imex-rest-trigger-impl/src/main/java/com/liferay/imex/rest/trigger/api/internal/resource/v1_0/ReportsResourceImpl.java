package com.liferay.imex.rest.trigger.api.internal.resource.v1_0;

import com.liferay.imex.core.api.report.ImexExecutionReportService;
import com.liferay.imex.core.util.statics.FileUtil;
import com.liferay.imex.rest.trigger.api.dto.v1_0.ReportFiles;
import com.liferay.imex.rest.trigger.api.resource.v1_0.ReportsResource;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.vulcan.pagination.Page;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ServiceScope;

/**
 * @author jpdacunha
 */
@Component(
	properties = "OSGI-INF/liferay/rest/v1_0/reports.properties",
	scope = ServiceScope.PROTOTYPE, service = ReportsResource.class
)
public class ReportsResourceImpl extends BaseReportsResourceImpl {
	
	private static final Log _log = LogFactoryUtil.getLog(ReportsResourceImpl.class);
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	private ImexExecutionReportService imexExecutionReportService;

	@Override
	public Page<ReportFiles> getReportsFilesPage() throws Exception {
		
		List<ReportFiles> reports = new ArrayList<>();
		
		List<File> logsFiles = imexExecutionReportService.getAllLogs();
		
		if (logsFiles != null && logsFiles.size() > 0) {
			
			for (File file : logsFiles) {
				
				ReportFiles report = new ReportFiles();
				report.setName(file.getName());
				
				String absolutePath = file.getAbsolutePath();
				Path path = Paths.get(absolutePath);
				BasicFileAttributes  attr = Files.readAttributes(path, BasicFileAttributes.class);
				if (attr != null) {
					
					FileTime creationTime = attr.creationTime();
					if (creationTime != null) {
						report.setCreationDate(new Date(creationTime.toMillis()));
					} else {
						_log.debug("Unable to extract creationDate for file identified by [" + absolutePath + "]");
					}
					
					FileTime modificationTime = attr.lastModifiedTime();
					if (modificationTime != null) {
						report.setLastModifiedDate(new Date(modificationTime.toMillis()));
					} else {
						_log.debug("Unable to extract modificationDate for file identified by [" + absolutePath + "]");
					}
					
				} else {
					_log.debug("Unable to retrieve attributes for file identified by [" + absolutePath + "]");
				}
				
				Long length = file.length();
				report.setSize(length.intValue());
				
				String humanReadableSize = FileUtil.readableFileSize(length);
				report.setHumanReadableSize(humanReadableSize);
				
				reports.add(report);
				
			}
			
		} else {
			_log.debug("No registered exporters");
		}
		
		return Page.of(reports);
		
		
	}
	
	
	
}