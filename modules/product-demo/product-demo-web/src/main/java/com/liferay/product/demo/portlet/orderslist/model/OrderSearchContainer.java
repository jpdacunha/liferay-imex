package com.liferay.product.demo.portlet.orderslist.model;

import java.util.HashMap;
import java.util.Map;

import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;

import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

public class OrderSearchContainer extends SearchContainer<OrderUI>{
	
	private static Log log = LogFactoryUtil.getLog(OrderSearchContainer.class);
	
	private final static int DELTA = SearchContainer.DEFAULT_DELTA;

	static Map<String, String> orderableHeaders = new HashMap<String, String>();
	

	public OrderSearchContainer(RenderRequest renderRequest, PortletURL portletURL) {
		
		super(renderRequest, null, null, SearchContainer.DEFAULT_CUR_PARAM, DELTA, portletURL, null, "no-order-found");
		
	}

}
