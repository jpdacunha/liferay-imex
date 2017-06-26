package com.liferay.product.demo.portlet.orderslist.model.comparator;

import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.product.demo.portlet.orderslist.model.OrderUI;

public class CountryOrderByComparator extends OrderByComparator<OrderUI> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1886548839668581051L;

	public CountryOrderByComparator() {
		this(false);
	}

	public CountryOrderByComparator(boolean asc) {
		_asc = asc;
	}

	public int compare(OrderUI obj1, OrderUI obj2) {

		int value = obj1.getCountry().toLowerCase().compareTo(obj2.getCountry().toLowerCase());

		if (_asc) {
			return value;
		} else {
			return -value;
		}

	}

	private boolean _asc;

}
