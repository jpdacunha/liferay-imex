package com.liferay.product.demo.portlet.topcustomers.facade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.liferay.product.demo.api.converter.JsConverter;
import com.liferay.product.demo.portlet.topcustomers.model.CompanyRevenuUI;

public class TopCustomerFacade {
	
	public List<CompanyRevenuUI> getCompanyRevenuUI() {
		
		List<CompanyRevenuUI> results = new ArrayList<>();
		
		CompanyRevenuUI ui1 = new CompanyRevenuUI();
		ui1.setCompanyName("Walmart");
		ui1.setRevenue(48587);
		ui1.setCurrency("$");
		ui1.setCompanyId(12046);
		
		results.add(ui1);
		
		CompanyRevenuUI ui2 = new CompanyRevenuUI();
		ui2.setCompanyName("BP");
		ui2.setRevenue(32960);
		ui2.setCurrency("$");
		ui2.setCompanyId(12046);
		
		results.add(ui2);
		
		
		CompanyRevenuUI ui3 = new CompanyRevenuUI();
		ui3.setCompanyName("Samsung Electronics");
		ui3.setRevenue(17744);
		ui3.setCurrency("$");
		ui3.setCompanyId(12046);
		
		results.add(ui3);
		
		
		CompanyRevenuUI ui4 = new CompanyRevenuUI();
		ui4.setCompanyName("Volkswagen");
		ui4.setRevenue(23660);
		ui4.setCurrency("$");
		ui4.setCompanyId(12046);
		
		results.add(ui4);
		
		CompanyRevenuUI ui5 = new CompanyRevenuUI();
		ui5.setCompanyName("Ford Motor");
		ui5.setRevenue(42233);
		ui5.setCurrency("$");
		ui5.setCompanyId(12046);
		
		results.add(ui5);
		
		CompanyRevenuUI ui6 = new CompanyRevenuUI();
		ui6.setCompanyName("AXA");
		ui6.setRevenue(15235);
		ui6.setCurrency("$");
		ui6.setCompanyId(12046);
		
		results.add(ui6);
		
		CompanyRevenuUI ui7 = new CompanyRevenuUI();
		ui7.setCompanyName("Total");
		ui7.setRevenue(14342);
		ui7.setCurrency("$");
		ui7.setCompanyId(12046);
		
		results.add(ui7);
		
		CompanyRevenuUI ui8 = new CompanyRevenuUI();
		ui8.setCompanyName("Volkswagen");
		ui8.setRevenue(23660);
		ui8.setCurrency("$");
		ui8.setCompanyId(12046);
		
		results.add(ui8);
		
		CompanyRevenuUI ui9 = new CompanyRevenuUI();
		ui9.setCompanyName("Allianz");
		ui9.setRevenue(12294);
		ui9.setCurrency("$");
		ui9.setCompanyId(12046);
		
		results.add(ui9);
		
		CompanyRevenuUI ui10 = new CompanyRevenuUI();
		ui10.setCompanyName("Amazon");
		ui10.setRevenue(18626);
		ui10.setCurrency("$");
		ui10.setCompanyId(12046);
		
		results.add(ui10);
		
		
		return results;
		
		
	}
	
	public String getBarArray() {
		
		List<CompanyRevenuUI> results = getCompanyRevenuUI();
		Map<String, Double> bar = new HashMap<>();
		
		for (CompanyRevenuUI ui : results) {
			
			bar.put(ui.getCompanyName(), ui.getRevenue());
			
		}
		
		return JsConverter.mapToJsArray(bar);
		
	}

}
