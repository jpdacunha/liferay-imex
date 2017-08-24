package com.liferay.product.demo.dashboard.portlet.chartJsDemo.portlet;

import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;

import javax.portlet.Portlet;

import org.osgi.service.component.annotations.Component;

@Component(
	immediate = true,
	property = {
		"com.liferay.portlet.display-category=category.dashboard.demo",
		"com.liferay.portlet.instanceable=false",
		"javax.portlet.display-name=Chart JS demo Portlet",
		"javax.portlet.init-param.template-path=/chartjsdemo/",
		"javax.portlet.init-param.view-template=/chartjsdemo/view.jsp",
		"javax.portlet.resource-bundle=content.Language",
		"com.liferay.portlet.header-portlet-css=/chartjsdemo/css/main.css",
		"com.liferay.portlet.header-portlet-javascript=/chartjsdemo/js/main.js",
		"com.liferay.portlet.css-class-wrapper=chart-js-demo-portlet",
		"javax.portlet.security-role-ref=power-user,user"
	},
	service = Portlet.class
)
public class ChartJsDemoPortlet extends MVCPortlet {
	
	
	
	
	
}