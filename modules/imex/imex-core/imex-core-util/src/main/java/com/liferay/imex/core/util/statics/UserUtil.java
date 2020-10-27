package com.liferay.imex.core.util.statics;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.role.RoleConstants;
import com.liferay.portal.kernel.service.RoleLocalServiceUtil;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.util.PortalUtil;

import java.util.List;

/**
 * Utility User methods
 * @author jpdacunha
 *
 */
public class UserUtil {
	
	private static final Log _log = LogFactoryUtil.getLog(UserUtil.class);
	
	/**
	 * Retrieve the omniadmin : cross company admin
	 * @param companyId
	 * @return
	 * @throws PortalException
	 * @throws SystemException
	 */
	public static User getDefaultAdmin(long companyId) {
		
		try {
			Role r = RoleLocalServiceUtil.getRole(companyId, RoleConstants.ADMINISTRATOR);
			List<User> users = UserLocalServiceUtil.getRoleUsers(r.getRoleId());
			User omniadmin = null;
			
			for (User user : users) {
				if (PortalUtil.isOmniadmin(user.getUserId())) {
					omniadmin = user;
					break;
				}
			}
			
			if (omniadmin == null) {
				throw new SystemException("Omniadmin user cannot be found for this portal instance");
			}
			
			return omniadmin;
		} catch (SystemException | PortalException e) {
			_log.error(e,e);
		}
		return null;
		
	}

}
