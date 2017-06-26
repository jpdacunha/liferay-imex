package com.liferay.product.demo.portlet.orderslist.model.comparator;

import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.product.demo.portlet.orderslist.model.OrderUI;

public class CustomerIdOrderByComparator extends OrderByComparator<OrderUI> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1886548839668581051L;

	public CustomerIdOrderByComparator() {
		this(false);
	}

	public CustomerIdOrderByComparator(boolean asc) {
		_asc = asc;
	}

	public int compare(OrderUI obj1, OrderUI obj2) {

		int value = obj1.getCustomerId().toLowerCase().compareTo(obj2.getCustomerId().toLowerCase());

		if (_asc) {
			return value;
		} else {
			return -value;
		}

	}

	private boolean _asc;

}
