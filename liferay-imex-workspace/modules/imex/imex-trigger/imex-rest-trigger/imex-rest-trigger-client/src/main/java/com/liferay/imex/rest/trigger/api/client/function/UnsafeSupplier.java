package com.liferay.imex.rest.trigger.api.client.function;

import javax.annotation.Generated;

/**
 * @author jpdacunha
 * @generated
 */
@FunctionalInterface
@Generated("")
public interface UnsafeSupplier<T, E extends Throwable> {

	public T get() throws E;

}