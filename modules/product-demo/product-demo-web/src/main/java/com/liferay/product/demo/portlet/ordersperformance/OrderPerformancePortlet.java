package com.liferay.product.demo.portlet.ordersperformance;

import java.io.IOException;

import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;

import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.product.demo.portlet.orderslist.facade.OrderListFacade;

@Component(
		immediate = true,
		property = {
			"com.liferay.portlet.display-category=category.main.demo",
			"com.liferay.portlet.instanceable=false",
			"com.liferay.portlet.header-portlet-javascript=/ordersperformance/js/chart.js",
			"com.liferay.portlet.css-class-wrapper=order-performance-portlet",
			"javax.portlet.display-name=Order Performance Portlet",
			"javax.portlet.init-param.template-path=/ordersperformance/",
			"javax.portlet.init-param.view-template=/ordersperformance/view.jsp",
			"javax.portlet.resource-bundle=content.Language",
			"javax.portlet.security-role-ref=power-user,user"
		},
		service = Portlet.class
	)
public class OrderPerformancePortlet extends MVCPortlet {
	
	public OrderListFacade facade = new OrderListFacade();

	@Override
	public void doView(RenderRequest renderRequest, RenderResponse renderResponse) throws IOException, PortletException {
		
		super.doView(renderRequest, renderResponse);
		
	}
	
}
