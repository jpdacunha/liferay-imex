package com.liferay.product.demo.context.contributor.util;

import java.io.IOException;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;

public class ThemeCssUtil {
	
	private static Log _log = LogFactoryUtil.getLog(ThemeCssUtil.class);
	
	private ThemeCssUtil(){}
 
	private static ThemeCssUtil INSTANCE = null;
 
	public static ThemeCssUtil getInstance() {
		if (INSTANCE == null) {
			synchronized (ThemeCssUtil.class) {
				if (INSTANCE == null) {
					INSTANCE = new ThemeCssUtil();
				}
			}
		}
		return INSTANCE;
	}
	
	public String getCss(String mainColorValue) {
		
		try {
			if (Validator.isNotNull(mainColorValue)) {
				
				String templateFileName = "/content/templates/main-css.ftl";
				
				String mainColor = mainColorValue;
				String mainColorLight = ColorUtil.lighter(mainColor, 1);
				String mainColorLight2  = ColorUtil.lighter(mainColorLight, 1);
				String mainColorDark = ColorUtil.darker(mainColor, 1);
				String colorDisabled = "silver";
				
				if(_log.isDebugEnabled()) {
					_log.info("mainColor : " + mainColor);
					_log.info("mainColorLight : " + mainColorLight);
					_log.info("mainColorLight2 : " + mainColorLight2);
					_log.info("mainColorDark : " + mainColorDark);
					_log.info("colorDisabled : " + colorDisabled);
				}
					
				String content = this.getReplacedContent(
							templateFileName, 
							new String[] {"[$main-color$]", "[$main-color-light$]", "[$main-color-light2$]", "[$main-color-dark$]", "[$color-disabled$]"}, 
							new String[] {mainColor, mainColorLight, mainColorLight2, mainColorDark, colorDisabled}
							);
				
				return content;
				
			}
		} catch (Exception e) {
			_log.error(e,e);
		}
		return StringPool.BLANK;
		
	}
	
	private String getReplacedContent(String fileName, String[] toReplace, String[] replacement) throws IOException {
	
		String replacedValues = StringPool.BLANK;
		
		if (Validator.isNotNull(fileName)) {
			
			String bodyContent = StringUtil.read(this.getClass().getClassLoader(), fileName, true);
			
			if (Validator.isNotNull(toReplace) && Validator.isNotNull(replacement) ) {
				
				StringBuilder builder = new StringBuilder(StringUtil.replace(bodyContent, toReplace, replacement));
				
				return builder.toString();
				
			} else {
				_log.debug("Skipping replacement : nothing to replace");
			}
			
			return bodyContent;
				
		}
		
		return replacedValues;
		
	}


	
}
