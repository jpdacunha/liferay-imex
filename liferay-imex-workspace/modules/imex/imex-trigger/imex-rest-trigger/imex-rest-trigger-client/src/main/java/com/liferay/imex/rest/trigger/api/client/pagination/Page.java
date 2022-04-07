package com.liferay.imex.rest.trigger.api.client.pagination;

import com.liferay.imex.rest.trigger.api.client.aggregation.Facet;
import com.liferay.imex.rest.trigger.api.client.json.BaseJSONParser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Generated;

/**
 * @author jpdacunha
 * @generated
 */
@Generated("")
public class Page<T> {

	public static <T> Page<T> of(
		String json, Function<String, T> toDTOFunction) {

		PageJSONParser pageJSONParser = new PageJSONParser(toDTOFunction);

		return (Page<T>)pageJSONParser.parseToDTO(json);
	}

	public T fetchFirstItem() {
		Iterator<T> iterator = _items.iterator();

		if (iterator.hasNext()) {
			return iterator.next();
		}

		return null;
	}

	public Map<String, Map> getActions() {
		return _actions;
	}

	public List<Facet> getFacets() {
		return _facets;
	}

	public Collection<T> getItems() {
		return _items;
	}

	public long getLastPage() {
		if (_totalCount == 0) {
			return 1;
		}

		return -Math.floorDiv(-_totalCount, _pageSize);
	}

	public long getPage() {
		return _page;
	}

	public long getPageSize() {
		return _pageSize;
	}

	public long getTotalCount() {
		return _totalCount;
	}

	public boolean hasNext() {
		if (getLastPage() > _page) {
			return true;
		}

		return false;
	}

	public boolean hasPrevious() {
		if (_page > 1) {
			return true;
		}

		return false;
	}

	public void setActions(Map<String, Map> actions) {
		_actions = actions;
	}

	public void setFacets(List<Facet> facets) {
		_facets = facets;
	}

	public void setItems(Collection<T> items) {
		_items = items;
	}

	public void setPage(long page) {
		_page = page;
	}

	public void setPageSize(long pageSize) {
		_pageSize = pageSize;
	}

	public void setTotalCount(long totalCount) {
		_totalCount = totalCount;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("{\"actions\": ");

		sb.append(_toString((Map)_actions));
		sb.append(", \"items\": [");

		Iterator<T> iterator = _items.iterator();

		while (iterator.hasNext()) {
			sb.append(iterator.next());

			if (iterator.hasNext()) {
				sb.append(", ");
			}
		}

		sb.append("], \"page\": ");
		sb.append(_page);
		sb.append(", \"pageSize\": ");
		sb.append(_pageSize);
		sb.append(", \"totalCount\": ");
		sb.append(_totalCount);
		sb.append("}");

		return sb.toString();
	}

	public static class PageJSONParser<T> extends BaseJSONParser<Page> {

		public PageJSONParser() {
			_toDTOFunction = null;
		}

		public PageJSONParser(Function<String, T> toDTOFunction) {
			_toDTOFunction = toDTOFunction;
		}

		@Override
		protected Page createDTO() {
			return new Page();
		}

		@Override
		protected Page[] createDTOArray(int size) {
			return new Page[size];
		}

		@Override
		protected void setField(
			Page page, String jsonParserFieldName,
			Object jsonParserFieldValue) {

			if (Objects.equals(jsonParserFieldName, "actions")) {
				if (jsonParserFieldValue != null) {
					PageJSONParser pageJSONParser = new PageJSONParser(
						_toDTOFunction);

					page.setActions(
						pageJSONParser.parseToMap(
							(String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "facets")) {
				if (jsonParserFieldValue != null) {
					page.setFacets(
						Stream.of(
							toStrings((Object[])jsonParserFieldValue)
						).map(
							this::parseToMap
						).map(
							facets -> new Facet(
								(String)facets.get("facetCriteria"),
								Stream.of(
									(Object[])facets.get("facetValues")
								).map(
									object -> (String)object
								).map(
									this::parseToMap
								).map(
									facetValues -> new Facet.FacetValue(
										Integer.valueOf(
											(String)facetValues.get(
												"numberOfOccurrences")),
										(String)facetValues.get("term"))
								).collect(
									Collectors.toList()
								))
						).collect(
							Collectors.toList()
						));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "items")) {
				if (jsonParserFieldValue != null) {
					page.setItems(
						Stream.of(
							toStrings((Object[])jsonParserFieldValue)
						).map(
							string -> _toDTOFunction.apply(string)
						).collect(
							Collectors.toList()
						));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "lastPage")) {
			}
			else if (Objects.equals(jsonParserFieldName, "page")) {
				if (jsonParserFieldValue != null) {
					page.setPage(Long.valueOf((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "pageSize")) {
				if (jsonParserFieldValue != null) {
					page.setPageSize(
						Long.valueOf((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "totalCount")) {
				if (jsonParserFieldValue != null) {
					page.setTotalCount(
						Long.valueOf((String)jsonParserFieldValue));
				}
			}
			else {
				throw new IllegalArgumentException(
					"Unsupported field name " + jsonParserFieldName);
			}
		}

		private final Function<String, T> _toDTOFunction;

	}

	private String _toString(Map<String, Object> map) {
		StringBuilder sb = new StringBuilder("{");

		Set<Map.Entry<String, Object>> entries = map.entrySet();

		Iterator<Map.Entry<String, Object>> iterator = entries.iterator();

		while (iterator.hasNext()) {
			Map.Entry<String, Object> entry = iterator.next();

			sb.append("\"");
			sb.append(entry.getKey());
			sb.append("\": ");

			Object value = entry.getValue();

			if (value instanceof Map) {
				sb.append(_toString((Map)value));
			}
			else {
				sb.append("\"");
				sb.append(value);
				sb.append("\"");
			}

			if (iterator.hasNext()) {
				sb.append(", ");
			}
		}

		sb.append("}");

		return sb.toString();
	}

	private Map<String, Map> _actions;
	private List<Facet> _facets = new ArrayList<>();
	private Collection<T> _items;
	private long _page;
	private long _pageSize;
	private long _totalCount;

}