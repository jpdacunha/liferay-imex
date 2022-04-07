package com.liferay.imex.gui.panel.application.list;

import com.liferay.imex.gui.panel.constants.ImexGuiPanelAppPanelCategoryKeys;
import com.liferay.imex.gui.panel.constants.ImexGuiPanelAppPortletKeys;

import com.liferay.application.list.BasePanelApp;
import com.liferay.application.list.PanelApp;
import com.liferay.portal.kernel.model.Portlet;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author dev
 */
@Component(
	immediate = true,
	property = {
		"panel.app.order:Integer=100",
		"panel.category.key=" + ImexGuiPanelAppPanelCategoryKeys.CONTROL_PANEL_CATEGORY
	},
	service = PanelApp.class
)
public class ImexGuiPanelAppPanelApp extends BasePanelApp {

	@Override
	public String getPortletId() {
		return ImexGuiPanelAppPortletKeys.IMEXGUIPORTLETID;
	}

	@Override
	@Reference(
		target = "(javax.portlet.name=" + ImexGuiPanelAppPortletKeys.IMEXGUIPORTLETID + ")",
		unbind = "-"
	)
	public void setPortlet(Portlet portlet) {
		super.setPortlet(portlet);
	}

}