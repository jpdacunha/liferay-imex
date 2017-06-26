package com.liferay.product.demo.portlet.orderslist.model.comparator;

import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.product.demo.portlet.orderslist.model.OrderUI;

public class OrderAmountComparator extends OrderByComparator<OrderUI> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1634303871589839644L;
	
	public OrderAmountComparator() {
		this(false);
	}

	public OrderAmountComparator(boolean asc) {
		_asc = asc;
	}

	public int compare(OrderUI p1, OrderUI p2) {
		
		int value = 0;
		
        if (p1.getOrderAmount() < p2.getOrderAmount()) {
        	value = -1;
        }
        
        if (p1.getOrderAmount() > p2.getOrderAmount()) {
        	value = 1;
        }
        
		if (_asc) {
			return value;
		} else {
			return -value;
		}
	} 
	
	private boolean _asc;

}
