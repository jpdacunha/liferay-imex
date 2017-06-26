package com.liferay.product.demo.portlet.businesstarget.facade;

import com.liferay.product.demo.portlet.businesstarget.model.BusinessTargetUI;

public class BusinessTargetFacade {
	
	public BusinessTargetUI getIndicators() {
		
		BusinessTargetUI ui = new BusinessTargetUI();
		ui.setIndicator1(14);
		ui.setIndicator1Color("#0088CC");
		ui.setIndicator2(85);
		ui.setIndicator2Color("#B73C23");
		
		return ui;
		
	}

}
