package com.liferay.product.demo.portlet.orderslist;

import java.io.IOException;

import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;

import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.product.demo.portlet.orderslist.facade.OrderListFacade;
import com.liferay.product.demo.portlet.orderslist.model.OrderSearchContainer;

@Component(
		immediate = true,
		property = {
			"com.liferay.portlet.display-category=category.main.demo",
			"com.liferay.portlet.instanceable=false",
			"javax.portlet.display-name=Order List Portlet",
			"javax.portlet.init-param.template-path=/orderlist/",
			"javax.portlet.init-param.view-template=/orderlist/view.jsp",
			"javax.portlet.resource-bundle=content.Language",
			"com.liferay.portlet.header-portlet-css=/orderlist/css/main.css",
			"com.liferay.portlet.css-class-wrapper=order-list-portlet",
			"javax.portlet.security-role-ref=power-user,user"
		},
		service = Portlet.class
	)
public class OrderListPortlet extends MVCPortlet {
	
	public OrderListFacade facade = new OrderListFacade();

	@Override
	public void doView(RenderRequest renderRequest, RenderResponse renderResponse) throws IOException, PortletException {

		OrderSearchContainer searchContainer = facade.getSeachContainer(renderRequest, renderResponse);
		
		renderRequest.setAttribute("searchContainer", searchContainer);
		
		super.doView(renderRequest, renderResponse);
	}
	
}
