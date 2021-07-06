package com.liferay.imex.rest.trigger.api.client.aggregation;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Generated;

/**
 * @author jpdacunha
 * @generated
 */
@Generated("")
public class Aggregation {

	public Map<String, String> getAggregationTerms() {
		return _aggregationTerms;
	}

	public void setAggregationTerms(Map<String, String> aggregationTerms) {
		_aggregationTerms = aggregationTerms;
	}

	private Map<String, String> _aggregationTerms = new HashMap<>();

}