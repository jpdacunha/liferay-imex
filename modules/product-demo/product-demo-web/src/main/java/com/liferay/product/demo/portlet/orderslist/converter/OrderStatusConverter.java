package com.liferay.product.demo.portlet.orderslist.converter;

import javax.servlet.http.HttpServletRequest;

import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.product.demo.api.model.OrderStatusEnum;

public class OrderStatusConverter {
	
	public static String toHTMLBadge(OrderStatusEnum orderStatusEnum, HttpServletRequest request) {
		
		String css = null;
		String label = null;
		if (orderStatusEnum != null) {
			css = orderStatusEnum.getKey();
			label = LanguageUtil.get(request, orderStatusEnum.getKey());
		} else {
			css = OrderStatusEnum.UNKNOWN.getKey();
			label = LanguageUtil.get(request, OrderStatusEnum.UNKNOWN.getKey());
		}
		
		return "<span class=\"badge " + css + "\">"  + label + "</span>";
		
	}

}
