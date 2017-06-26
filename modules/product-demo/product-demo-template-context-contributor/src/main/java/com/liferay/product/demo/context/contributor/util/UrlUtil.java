package com.liferay.product.demo.context.contributor.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.portlet.PortletMode;
import javax.portlet.WindowState;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.service.GroupLocalServiceUtil;
import com.liferay.portal.kernel.service.LayoutLocalServiceUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;

public class UrlUtil {
	
	private static Log _log = LogFactoryUtil.getLog(UrlUtil.class);

	private UrlUtil() {
		super();
	}
	
	public static void send301Redirect(HttpServletResponse response, String url) {
		
		if (Validator.isNotNull(url)) {
			response.setStatus(301);
			response.setHeader( "Location", url);
			response.setHeader( "Connection", "close" );
		} else {
			_log.error("URL is null aborting redirect ...");
		}

	}
	
	/**
	 * Return current request URI
	 * @param request
	 * @return
	 * @throws MalformedURLException
	 */
	public static String getCurrentURI(HttpServletRequest request) throws MalformedURLException {
		
		String currentCompleteURL = PortalUtil.getCurrentCompleteURL(request);	
		URL completeURL = new URL(currentCompleteURL);
		return completeURL.getPath();
		
	}

	/**
	 * 
	 * @param groupId
	 * @param isPrivateLayout
	 * @return
	 * @throws SystemException
	 */
	public static Layout getHomeLayout(long groupId) throws SystemException {

		List<Layout> layouts = LayoutLocalServiceUtil.getLayouts(groupId, false, 0);
		Layout layout = null;

		if (!layouts.isEmpty()) {

			layout = layouts.get(0);
		}

		return layout;
	}
	
	/**
	 * 
	 * @param groupId
	 * @param isPrivateLayout
	 * @return
	 * @throws SystemException
	 */
	public static Layout getUserHomeLayout(long groupId) throws SystemException {

		List<Layout> layouts = LayoutLocalServiceUtil.getLayouts(groupId, true, 0);
		Layout layout = null;

		for (Layout currentLayout : layouts) {

			if (!currentLayout.isHidden()) {
				layout = currentLayout;
			}
			
		}

		return layout;
		
	}
	
	/**
	 * User home page URL
	 * @param td
	 * @return
	 * @throws PortalException
	 * @throws SystemException
	 */
	public static String getUserHomeUrl(ThemeDisplay td) throws PortalException, SystemException {
		
		long groupId = td.getScopeGroupId();
		Layout layout = getUserHomeLayout(groupId);
		if (layout != null) {
			return getSitePageUrl(layout, layout.getGroup(), layout.isPublicLayout());	
		}
		return null;
	}

	/**
	 * Return home page url
	 * 
	 * @param vmVariables
	 * @param td
	 * @return
	 * @throws PortalException
	 * @throws SystemException
	 */
	public static String getHomeUrl(ThemeDisplay td) throws PortalException, SystemException {

		long groupId = td.getScopeGroupId();
		return getHomeUrl(groupId);
	}

	/**
	 * Return home page url
	 * 
	 * @param groupId
	 * @return
	 * @throws SystemException
	 * @throws PortalException
	 */
	public static String getHomeUrl(long groupId) throws SystemException, PortalException {
		
		Layout layout = getHomeLayout(groupId);
		if (layout != null) {
			return getSitePageUrl(layout, layout.getGroup(), layout.isPublicLayout());
		}
		return null;
	}
	
	/**
	 * 
	 * @param td
	 * @return
	 * @throws PortalException
	 * @throws SystemException
	 */
	public static String getLayoutUrlIcon(Layout layout) throws PortalException, SystemException {

		if (layout.getIconImage()) {
			
			StringBundler img_url = new StringBundler("/image/layout_icon?img_id=").append(layout.getIconImageId());
			return img_url.toString();
		}
		return null;

	}

	/**
	 * Parse la quesry String pour en extraire les parametres
	 * 
	 * @param query
	 * @return
	 */
	public static Map<String, String> getQueryMap(String query) {

		String[] params = query.split("&");
		Map<String, String> map = new HashMap<>();
		for (String param : params) {
			String[] parameters = param.split("=");
			if (parameters.length == 2) {
				String name = parameters[0];
				String value = parameters[1];
				map.put(name, value);
			}
		}
		return map;

	}

	/**
	 * Retourne le path (Partie sans le protocol, server, port et parametres) Ã
	 * partir d'une URL
	 * 
	 * @param urlStr
	 * @return
	 */
	public static String getPath(String urlStr) {

		URL url;

		if (Validator.isNotNull(urlStr)) {
			try {
				url = new URL(urlStr);
				return url.getPath();
			} catch (MalformedURLException e) {
				_log.error(e.getMessage(), e);

			}

		}

		return null;

	}

	/**
	 * GÃ©nÃ¨re une portlet URL
	 * 
	 * @param request
	 * @param portletId
	 * @param phase
	 * @param plid
	 * @param windowState
	 * @param portletMode
	 * @param strutsAction
	 * @return
	 * @throws SystemException
	 */
	public static String getPortletQueryString(String pageName, String portletId, String phase, WindowState windowState, PortletMode portletMode, String strutsAction) throws SystemException {

		StringBuffer url = new StringBuffer();
		boolean isFisrtParameter = true;

		url.append("/");

		if (pageName != null) {

			// On enleve la premiere occurence du slash
			if (pageName.indexOf("/") == 0) {
				pageName = pageName.replaceFirst("/", "");
			}

			url.append(pageName);
		}

		if (portletId != null) {
			url.append(getSeparator(isFisrtParameter));
			isFisrtParameter = false;
			url.append("p_p_id=");
			url.append(portletId);
		}

		if (phase != null) {
			url.append(getSeparator(isFisrtParameter));
			isFisrtParameter = false;
			url.append("p_p_lifecycle=");
			url.append(phase);
		}

		if (windowState != null) {
			url.append(getSeparator(isFisrtParameter));
			isFisrtParameter = false;
			url.append("p_p_state=");
			url.append(windowState);
		}

		if (portletMode != null) {
			url.append(getSeparator(isFisrtParameter));
			isFisrtParameter = false;
			url.append("p_p_mode=");
			url.append(portletMode);
		}

		if (portletId != null && strutsAction != null) {
			url.append(getSeparator(isFisrtParameter));
			isFisrtParameter = false;
			url.append("_");
			url.append(portletId);
			url.append("_struts_action=");
			url.append(strutsAction);
		}

		return url.toString();

	}

	/**
	 * Récupère l'url d'un espace
	 * 
	 * @param group
	 * @param publique
	 * @return
	 */
	public static String getSiteUrl(Group group, boolean publique) {

		String url = null;
		if (group != null) {

			if (publique) {
				url = PropsUtil.get(PropsKeys.LAYOUT_FRIENDLY_URL_PUBLIC_SERVLET_MAPPING);
			} else {
				url = PropsUtil.get(PropsKeys.LAYOUT_FRIENDLY_URL_PRIVATE_GROUP_SERVLET_MAPPING);
			}

			url += group.getFriendlyURL();
		}
		return url;

	}

	/**
	 * Return a site page URL as String
	 * 
	 * @param layout
	 * @param group
	 * @param publique
	 * @return
	 */
	public static String getSitePageUrl(Layout layout, Group group, boolean publique) {

		String url = null;
		if (group != null && layout != null) {

			url = getSitePageUrl(layout.getFriendlyURL(), group, publique);

		}
		return url;

	}
	
	public static String getSitePageUrl(String friendlyURL, Group group, boolean publique) {
		
		String url = null;
		if (group != null && Validator.isNotNull(friendlyURL)) {

			url = getSiteUrl(group, publique) + friendlyURL;

		}
		return url;
		
	}

	private static String getSeparator(boolean isFirst) {

		if (isFirst) {
			return StringPool.QUESTION;
		}
		return StringPool.AMPERSAND;

	}

	/**
	 * This method builds site url with
	 * 
	 * @param siteFriendlyUrl
	 * @param isPublicSite
	 * @param isPrivatePage
	 * @param companyId
	 * @param locale
	 * @return
	 * @throws SystemException
	 */
	public static String buildSiteUrlWithLanguageCode(String siteFriendlyUrl, boolean isPublicSite, long companyId, Locale locale) throws SystemException {

		return buildPageUrlWithLanguageCode(siteFriendlyUrl, isPublicSite, null, false, companyId, locale);

	}

	/**
	 * This method builds page url with language code
	 * 
	 * @param siteFriendlyUrl
	 * @param isPublicSite
	 * @param pageFriendlyUrl
	 * @param isPrivatePage
	 * @param companyId
	 * @param locale
	 * @return
	 * @throws SystemException
	 */
	public static String buildPageUrlWithLanguageCode(String siteFriendlyUrl, boolean isPublicSite, String pageFriendlyUrl, boolean isPrivatePage, long companyId, Locale locale) throws SystemException {

		if (Validator.isNotNull(locale)) {

			if (Validator.isNotNull(siteFriendlyUrl)) {

				Group site = GroupLocalServiceUtil.fetchFriendlyURLGroup(companyId, siteFriendlyUrl);

				if (site != null) {

					String siteUrl = UrlUtil.getSiteUrl(site, isPublicSite);

					if (siteUrl != null) {

						String languageCode = locale.getLanguage();

						StringBuilder newSiteUrl = new StringBuilder(StringPool.SLASH);
						newSiteUrl.append(languageCode);
						newSiteUrl.append(siteUrl);

						if (Validator.isNotNull(pageFriendlyUrl)) {

							Layout layout = LayoutLocalServiceUtil.fetchLayoutByFriendlyURL(site.getGroupId(), isPrivatePage, pageFriendlyUrl);

							if (layout != null) {

								newSiteUrl.append(layout.getFriendlyURL(locale));

							} else {

								if (_log.isWarnEnabled()) {

									_log.warn("No layout found with groupId = " + site.getGroupId() + " page friendly url = " + pageFriendlyUrl + " private page : " + isPrivatePage);
								}
							}
						}

						return newSiteUrl.toString();

					} else {

						if (_log.isWarnEnabled()) {

							_log.warn("Site url is null....");
						}
					}
				} else {

					if (_log.isWarnEnabled()) {

						_log.warn("No site found with friendly url = " + siteFriendlyUrl + " and company id = " + companyId);
					}
				}
			} else {

				if (_log.isWarnEnabled()) {

					_log.warn("Site friendly url null...Imposible to build footer urls");
				}
			}
		} else {

			if (_log.isWarnEnabled()) {

				_log.warn("Theme display is null...Impossible to build footer urls...");
			}
		}

		return StringPool.BLANK;
	}

	/**
	 * This method returns true if site has page false if not
	 * 
	 * @param group
	 * @param friendlyUrl
	 * @return
	 * @throws SystemException
	 */
	public static boolean hasPage(long groupId, String friendlyUrl, boolean privateLayout) throws SystemException {

		if (friendlyUrl != null) {

			Layout layout = LayoutLocalServiceUtil.fetchLayoutByFriendlyURL(groupId, privateLayout, friendlyUrl);

			return layout != null;
		}

		return false;
	}
	
}
