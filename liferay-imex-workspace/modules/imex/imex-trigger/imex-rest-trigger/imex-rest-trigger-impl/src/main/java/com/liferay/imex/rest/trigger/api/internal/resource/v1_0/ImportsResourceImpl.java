package com.liferay.imex.rest.trigger.api.internal.resource.v1_0;

import com.liferay.imex.core.api.importer.ImexImportService;
import com.liferay.imex.rest.trigger.api.dto.v1_0.ImportProcess;
import com.liferay.imex.rest.trigger.api.resource.v1_0.ImportsResource;

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
	properties = "OSGI-INF/liferay/rest/v1_0/imports.properties",
	scope = ServiceScope.PROTOTYPE, service = ImportsResource.class
)
public class ImportsResourceImpl extends BaseImportsResourceImpl {
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected ImexImportService imexImportService;
	
	@Override
	public Response postImports(ImportProcess importProcess) throws Exception {
		
		Response.ResponseBuilder responseBuilder = null;
				
		if (importProcess != null) {
			
			String[] exporterNames = importProcess.getImporterNames();
			String profileId = importProcess.getProfileId();
			boolean isDebug = importProcess.getDebug();
			
			List<String> bundleNames = null;
			
			if (exporterNames != null) {
				bundleNames = Arrays.asList(exporterNames);
			}
			
			String id = imexImportService.doImport(bundleNames, profileId, isDebug);
			
			responseBuilder = Response.ok(id);
				
			
		} else {
			List<Variant> variants = Variant.VariantListBuilder.newInstance().build();
			responseBuilder = Response.notAcceptable(variants);
		}
	
		return responseBuilder.build();
		
	}
	
}