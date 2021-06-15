package com.liferay.imex.rest.trigger.api.internal.jaxrs.application;

import javax.annotation.Generated;

import javax.ws.rs.core.Application;

import org.osgi.service.component.annotations.Component;

/**
 * @author jpdacunha
 * @generated
 */
@Component(
	property = {
		"liferay.jackson=false",
		"osgi.jaxrs.application.base=/imex-rest-trigger",
		"osgi.jaxrs.extension.select=(osgi.jaxrs.name=Liferay.Vulcan)",
		"osgi.jaxrs.name=ImexRestTrigger"
	},
	service = Application.class
)
@Generated("")
public class ImexRestTriggerApplication extends Application {
}