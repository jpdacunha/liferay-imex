package com.liferay.product.demo.portlet.orderslist.model;

import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.product.demo.portlet.orderslist.model.comparator.CountryOrderByComparator;
import com.liferay.product.demo.portlet.orderslist.model.comparator.CustomerIdOrderByComparator;
import com.liferay.product.demo.portlet.orderslist.model.comparator.OrderAmountComparator;

public class OrderUIComparatorUtil {
	
	public static OrderByComparator<OrderUI> getUserOrderByComparator(String orderByCol, String orderByType) {

		boolean orderByAsc = false;

		if (orderByType != null && orderByType.equals("asc")) {
			orderByAsc = true;
		}

		OrderByComparator<OrderUI> orderByComparator = null;

		if (orderByCol.equalsIgnoreCase("orderCountry")) {
			orderByComparator = new CountryOrderByComparator(orderByAsc);
		} else if (orderByCol.equalsIgnoreCase("customerId")) {
			orderByComparator = new CustomerIdOrderByComparator(orderByAsc);
		} else if (orderByCol.equalsIgnoreCase("orderAmount")) {
			orderByComparator = new OrderAmountComparator(orderByAsc);
		}

		return orderByComparator;
	}

}
