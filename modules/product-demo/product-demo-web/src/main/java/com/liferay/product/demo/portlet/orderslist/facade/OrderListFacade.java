package com.liferay.product.demo.portlet.orderslist.facade;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.http.HttpServletRequest;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.product.demo.api.model.OrderStatusEnum;
import com.liferay.product.demo.portlet.orderslist.converter.OrderStatusConverter;
import com.liferay.product.demo.portlet.orderslist.model.OrderSearchContainer;
import com.liferay.product.demo.portlet.orderslist.model.OrderUI;
import com.liferay.product.demo.portlet.orderslist.model.OrderUIComparatorUtil;

public class OrderListFacade {
	
	private static Log log = LogFactoryUtil.getLog(OrderListFacade.class);
	
	public OrderListFacade() {
		super();
	}

	public OrderSearchContainer getSeachContainer(RenderRequest renderRequest, RenderResponse renderResponse) {
		
		HttpServletRequest request = PortalUtil.getHttpServletRequest(renderRequest);
		
		PortletURL portletURL = renderResponse.createRenderURL();
		OrderSearchContainer searchContainer = new OrderSearchContainer(renderRequest, portletURL);
		
		List<OrderUI> orders = new ArrayList<>();
		
		OrderUI ui1 = new OrderUI();
		ui1.setCountry("France");
		ui1.setOrderAmount(1180);
		ui1.setCustomerName("Candice Flatley");
		ui1.setNbProducts("4");
		ui1.setOrderDate("2017-06-01 12:33");
		ui1.setOrderStatus("Shipped");
		ui1.setOrderId("6453217");
		ui1.setCustomerId("ZSDEM44007");
		ui1.setCurrency("$");
		ui1.setCompanyName("Total");
		
		String value1 = OrderStatusConverter.toHTMLBadge(OrderStatusEnum.NEW, request);
		ui1.setOrderStatus(value1);
		
		orders.add(ui1);
		
		OrderUI ui2 = new OrderUI();
		ui2.setCountry("France");
		ui2.setOrderAmount(27);
		ui2.setCustomerName("Bob Falker");
		ui2.setNbProducts("4");
		ui2.setOrderDate("2017-05-15 12:33");
		ui2.setOrderId("1290872");
		ui2.setCustomerId("244ZSQR324");
		ui2.setCurrency("$");
		ui2.setCompanyName("BNP paribas");
		
		String value2 = OrderStatusConverter.toHTMLBadge(OrderStatusEnum.NEW, request);
		ui2.setOrderStatus(value2);
		
		orders.add(ui2);
		
		OrderUI ui3 = new OrderUI();
		ui3.setCountry("United States");
		ui3.setOrderAmount(233.27);
		ui3.setCustomerName("Clarissa Mac Knight");
		ui3.setNbProducts("12");
		ui3.setOrderDate("2017-05-12 12:33");
		ui3.setOrderId("2096544");
		ui3.setCustomerId("A24XSZZ399");
		ui3.setCurrency("$");
		ui3.setCompanyName("Liferay");
		
		String value3 = OrderStatusConverter.toHTMLBadge(OrderStatusEnum.CANCELED, request);
		ui3.setOrderStatus(value3);
		
		orders.add(ui3);
		
		OrderUI ui4 = new OrderUI();
		ui4.setCountry("France");
		ui4.setOrderAmount(12);
		ui4.setCustomerName("Zack Simian");
		ui4.setNbProducts("1");
		ui4.setOrderDate("2016-08-03 09:33");
		ui4.setOrderId("2096544");
		ui4.setCustomerId("A24XSZZ399");
		ui4.setCurrency("$");
		ui4.setCompanyName("PSA");
		
		String value4 = OrderStatusConverter.toHTMLBadge(OrderStatusEnum.CLOSED, request);
		ui4.setOrderStatus(value4);
		
		orders.add(ui4);
		
		OrderUI ui5 = new OrderUI();
		ui5.setCountry("France");
		ui5.setOrderAmount(12333);
		ui5.setCustomerName("Sandrine Gey");
		ui5.setNbProducts("1");
		ui5.setOrderDate("2017-05-03 12:33");
		ui5.setOrderId("1296532");
		ui5.setCustomerId("123XSZXX96");
		ui5.setCurrency("$");
		ui5.setCompanyName("EDF");
		
		String value5 = OrderStatusConverter.toHTMLBadge(OrderStatusEnum.SHIPPED, request);
		ui5.setOrderStatus(value5);
		
		orders.add(ui5);
		
		OrderUI ui6 = new OrderUI();
		ui6.setCountry("France");
		ui6.setOrderAmount(12);
		ui6.setCustomerName("George Sandor");
		ui6.setNbProducts("1");
		ui6.setOrderDate("2017-05-03 14:45");
		ui6.setOrderId("2023344");
		ui6.setCustomerId("B24XZZZ459");
		ui6.setCurrency("$");
		ui6.setCompanyName("GRDF");
		
		String value6 = OrderStatusConverter.toHTMLBadge(OrderStatusEnum.IN_PROGRESS, request);
		ui6.setOrderStatus(value6);
		
		orders.add(ui6);
		
		//Sorting datas
		String sortByCol = ParamUtil.getString(renderRequest, "orderByCol"); 
		String sortByType = ParamUtil.getString(renderRequest, "orderByType"); 
		
		OrderByComparator<OrderUI> comparator = OrderUIComparatorUtil.getUserOrderByComparator(sortByCol, sortByType);
		
		searchContainer.setOrderByComparator(comparator);
		
		if (comparator != null) {
			Collections.sort(orders,comparator);
		}
		
		//Finally setting results
		searchContainer.setResults(orders);
		
		return searchContainer;
		
	}

}
