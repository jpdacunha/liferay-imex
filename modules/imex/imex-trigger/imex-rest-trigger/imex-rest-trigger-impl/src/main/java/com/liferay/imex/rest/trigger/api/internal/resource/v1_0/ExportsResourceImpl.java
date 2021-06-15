package com.liferay.imex.rest.trigger.api.internal.resource.v1_0;

import com.liferay.imex.core.api.exporter.ImexExportService;
import com.liferay.imex.rest.trigger.api.dto.v1_0.ExportProcess;
import com.liferay.imex.rest.trigger.api.resource.v1_0.ExportsResource;

import java.util.Arrays;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Variant;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ServiceScope;

/**
 * @author jpdacunha
 */
@Component(
	properties = "OSGI-INF/liferay/rest/v1_0/exports.properties",
	scope = ServiceScope.PROTOTYPE, service = ExportsResource.class
)
public class ExportsResourceImpl extends BaseExportsResourceImpl {
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected ImexExportService imexExportService;
	
	@Override
	public Response postExports(ExportProcess exportProcess) throws Exception {
		
		Response.ResponseBuilder responseBuilder = null;
				
		if (exportProcess != null) {
			
			String[] exporterNames = exportProcess.getExporterNames();
			String profileId = exportProcess.getProfileId();
			boolean isDebug = exportProcess.getDebug();
			
			List<String> bundleNames = null;
			
			if (exporterNames != null) {
				bundleNames = Arrays.asList(exporterNames);
			}
			
			String id = imexExportService.doExport(bundleNames, profileId, isDebug);
			
			responseBuilder = Response.ok(id);
				
			
		} else {
			List<Variant> variants = Variant.VariantListBuilder.newInstance().build();
			responseBuilder = Response.notAcceptable(variants);
		}
	
		return responseBuilder.build();
		
	}
	
}