package com.liferay.product.demo.portlet.businesstarget;

import java.io.IOException;

import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;

import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.product.demo.portlet.businesstarget.facade.BusinessTargetFacade;
import com.liferay.product.demo.portlet.businesstarget.model.BusinessTargetUI;

@Component(
		immediate = true,
		property = {
			"com.liferay.portlet.display-category=category.sample",
			"com.liferay.portlet.instanceable=false",
			"javax.portlet.display-name=Business Target Portlet",
			"javax.portlet.init-param.template-path=/businesstarget/",
			"javax.portlet.init-param.view-template=/businesstarget/view.jsp",
			"javax.portlet.resource-bundle=content.Language",
			"javax.portlet.security-role-ref=power-user,user"
		},
		service = Portlet.class
	)
public class BusinessTargetPortlet extends MVCPortlet {
	
	public BusinessTargetFacade facade = new BusinessTargetFacade();
	
	@Override
	public void doView(RenderRequest renderRequest, RenderResponse renderResponse) throws IOException, PortletException {
		
		BusinessTargetUI businessTargetFacade = facade.getIndicators();
		
		renderRequest.setAttribute("businessTargetFacade", businessTargetFacade);
		
		super.doView(renderRequest, renderResponse);
	}
	
}
