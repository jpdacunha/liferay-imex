package com.liferay.product.demo.context.contributor;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.template.TemplateContextContributor;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.product.demo.context.contributor.util.ThemeCssUtil;
import com.liferay.product.demo.context.contributor.util.UrlUtil;

@Component(
	immediate = true,
	property = {"type=" + TemplateContextContributor.TYPE_THEME},
	service = TemplateContextContributor.class
)
public class ProductDemoThemeTemplateContextContributor implements TemplateContextContributor {
	
	private static Log _log = LogFactoryUtil.getLog(ProductDemoThemeTemplateContextContributor.class);
	
	@Override
	public void prepare(Map<String, Object> contextObjects, HttpServletRequest request) {
		
		ThemeDisplay td = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
		
		/*td.getTheme().getSettings();*/
		
		try {
			contextObjects.put("demo_is_regular_page", !isHomePage(td.getLayout()) + "");
			
			//Color variation CSS
			String css = ThemeCssUtil.getInstance().getCss("");
			contextObjects.put("demo_main_css", css);
			
		} catch (SystemException | PortalException | IOException e) {
			_log.error(e,e);
		}
		
	}
	
	public boolean isHomePage(Layout currentLayout) throws PortalException, SystemException {
		
		Layout homeLayout = UrlUtil.getHomeLayout(currentLayout.getGroupId());
		return Validator.isNotNull(currentLayout) && Validator.isNotNull(homeLayout) && currentLayout.getFriendlyURL().equals(homeLayout.getFriendlyURL());
		
	}

}