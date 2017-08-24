package com.liferay.product.demo.portlet.topcustomers;

import java.io.IOException;

import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;

import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.product.demo.portlet.topcustomers.facade.TopCustomerFacade;

@Component(
		immediate = true,
		property = {
			"com.liferay.portlet.display-category=category.main.demo",
			"com.liferay.portlet.instanceable=false",
			"javax.portlet.display-name=Top Customers Portlet",
			"javax.portlet.init-param.template-path=/topcustomers/",
			"javax.portlet.init-param.view-template=/topcustomers/view.jsp",
			"javax.portlet.resource-bundle=content.Language",
			"javax.portlet.security-role-ref=power-user,user"
		},
		service = Portlet.class
	)
public class TopCustomersPortlet extends MVCPortlet {
	
	public TopCustomerFacade facade = new TopCustomerFacade();
	
	@Override
	public void doView(RenderRequest renderRequest, RenderResponse renderResponse) throws IOException, PortletException {
		
		String barArray = facade.getBarArray();
		
		renderRequest.setAttribute("barArray", barArray);
		
		super.doView(renderRequest, renderResponse);
	}
	
}
