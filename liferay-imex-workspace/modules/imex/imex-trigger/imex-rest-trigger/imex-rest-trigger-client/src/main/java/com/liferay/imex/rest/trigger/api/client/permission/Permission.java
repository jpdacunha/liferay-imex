package com.liferay.imex.rest.trigger.api.client.permission;

import com.liferay.imex.rest.trigger.api.client.json.BaseJSONParser;

import java.util.Objects;

import javax.annotation.Generated;

/**
 * @author jpdacunha
 * @generated
 */
@Generated("")
public class Permission {

	public static Permission toDTO(String json) {
		PermissionJSONParser<Permission> permissionJSONParser =
			new PermissionJSONParser();

		return permissionJSONParser.parseToDTO(json);
	}

	public Object[] getActionIds() {
		return actionIds;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setActionIds(Object[] actionIds) {
		this.actionIds = actionIds;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("{");

		if (actionIds != null) {
			sb.append("\"actionIds\": [");

			for (int i = 0; i < actionIds.length; i++) {
				sb.append("\"");
				sb.append(actionIds[i]);
				sb.append("\"");

				if ((i + 1) < actionIds.length) {
					sb.append(", ");
				}
			}

			sb.append("]");
		}

		if (roleName != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"roleName\": \"");
			sb.append(roleName);
			sb.append("\"");
		}

		sb.append("}");

		return sb.toString();
	}

	protected Object[] actionIds;
	protected String roleName;

	private static class PermissionJSONParser<T>
		extends BaseJSONParser<Permission> {

		@Override
		protected Permission createDTO() {
			return new Permission();
		}

		@Override
		protected Permission[] createDTOArray(int size) {
			return new Permission[size];
		}

		@Override
		protected void setField(
			Permission permission, String jsonParserFieldName,
			Object jsonParserFieldValue) {

			if (Objects.equals(jsonParserFieldName, "actionIds")) {
				if (jsonParserFieldValue != null) {
					permission.setActionIds((Object[])jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "roleName")) {
				if (jsonParserFieldValue != null) {
					permission.setRoleName((String)jsonParserFieldValue);
				}
			}
			else {
				throw new IllegalArgumentException(
					"Unsupported field name " + jsonParserFieldName);
			}
		}

	}

}