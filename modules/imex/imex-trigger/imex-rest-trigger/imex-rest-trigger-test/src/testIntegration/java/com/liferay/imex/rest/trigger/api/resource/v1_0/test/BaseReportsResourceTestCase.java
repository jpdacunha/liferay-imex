package com.liferay.imex.rest.trigger.api.resource.v1_0.test;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;

import com.liferay.imex.rest.trigger.api.client.dto.v1_0.Report;
import com.liferay.imex.rest.trigger.api.client.dto.v1_0.ReportFiles;
import com.liferay.imex.rest.trigger.api.client.http.HttpInvoker;
import com.liferay.imex.rest.trigger.api.client.pagination.Page;
import com.liferay.petra.reflect.ReflectionUtil;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.service.CompanyLocalServiceUtil;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.DateFormatFactoryUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.odata.entity.EntityField;
import com.liferay.portal.odata.entity.EntityModel;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.vulcan.resource.EntityModelResource;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import java.text.DateFormat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Generated;

import javax.ws.rs.core.MultivaluedHashMap;

import org.apache.commons.beanutils.BeanUtilsBean;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author jpdacunha
 * @generated
 */
@Generated("")
public abstract class BaseReportsResourceTestCase {

	@ClassRule
	@Rule
	public static final LiferayIntegrationTestRule liferayIntegrationTestRule =
		new LiferayIntegrationTestRule();

	@BeforeClass
	public static void setUpClass() throws Exception {
		_dateFormat = DateFormatFactoryUtil.getSimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ss'Z'");
	}

	@Before
	public void setUp() throws Exception {
		irrelevantGroup = GroupTestUtil.addGroup();
		testGroup = GroupTestUtil.addGroup();

		testCompany = CompanyLocalServiceUtil.getCompany(
			testGroup.getCompanyId());

		_reportsResource.setContextCompany(testCompany);

		ReportsResource.Builder builder = ReportsResource.builder();

		reportsResource = builder.authentication(
			"test@liferay.com", "test"
		).locale(
			LocaleUtil.getDefault()
		).build();
	}

	@After
	public void tearDown() throws Exception {
		GroupTestUtil.deleteGroup(irrelevantGroup);
		GroupTestUtil.deleteGroup(testGroup);
	}

	@Test
	public void testClientSerDesToDTO() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper() {
			{
				configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true);
				configure(
					SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
				enable(SerializationFeature.INDENT_OUTPUT);
				setDateFormat(new ISO8601DateFormat());
				setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
				setSerializationInclusion(JsonInclude.Include.NON_NULL);
				setVisibility(
					PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
				setVisibility(
					PropertyAccessor.GETTER, JsonAutoDetect.Visibility.NONE);
			}
		};

		Reports reports1 = randomReports();

		String json = objectMapper.writeValueAsString(reports1);

		Reports reports2 = ReportsSerDes.toDTO(json);

		Assert.assertTrue(equals(reports1, reports2));
	}

	@Test
	public void testClientSerDesToJSON() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper() {
			{
				configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true);
				configure(
					SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
				setDateFormat(new ISO8601DateFormat());
				setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
				setSerializationInclusion(JsonInclude.Include.NON_NULL);
				setVisibility(
					PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
				setVisibility(
					PropertyAccessor.GETTER, JsonAutoDetect.Visibility.NONE);
			}
		};

		Reports reports = randomReports();

		String json1 = objectMapper.writeValueAsString(reports);
		String json2 = ReportsSerDes.toJSON(reports);

		Assert.assertEquals(
			objectMapper.readTree(json1), objectMapper.readTree(json2));
	}

	@Test
	public void testEscapeRegexInStringFields() throws Exception {
		String regex = "^[0-9]+(\\.[0-9]{1,2})\"?";

		Reports reports = randomReports();

		String json = ReportsSerDes.toJSON(reports);

		Assert.assertFalse(json.contains(regex));

		reports = ReportsSerDes.toDTO(json);
	}

	@Test
	public void testGetReportsFilesPage() throws Exception {
		Assert.assertTrue(false);
	}

	@Test
	public void testGetReportsFileFileName() throws Exception {
		Reports postReports = testGetReports_addReports();

		ReportFiles postReportFiles = testGetReportsFileFileName_addReportFiles(
			postReports.getId(), randomReportFiles());

		ReportFiles getReportFiles = reportsResource.getReportsFileFileName(
			postReports.getId());

		assertEquals(postReportFiles, getReportFiles);
		assertValid(getReportFiles);
	}

	protected ReportFiles testGetReportsFileFileName_addReportFiles(
			long reportsId, ReportFiles reportFiles)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGetReports() throws Exception {
		Reports postReports = testGetReports_addReports();

		Report postReport = testGetReports_addReport(
			postReports.getId(), randomReport());

		Report getReport = reportsResource.getReports(postReports.getId());

		assertEquals(postReport, getReport);
		assertValid(getReport);
	}

	protected Report testGetReports_addReport(long reportsId, Report report)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected void assertHttpResponseStatusCode(
		int expectedHttpResponseStatusCode,
		HttpInvoker.HttpResponse actualHttpResponse) {

		Assert.assertEquals(
			expectedHttpResponseStatusCode, actualHttpResponse.getStatusCode());
	}

	protected void assertEquals(Reports reports1, Reports reports2) {
		Assert.assertTrue(
			reports1 + " does not equal " + reports2,
			equals(reports1, reports2));
	}

	protected void assertEquals(
		List<Reports> reportses1, List<Reports> reportses2) {

		Assert.assertEquals(reportses1.size(), reportses2.size());

		for (int i = 0; i < reportses1.size(); i++) {
			Reports reports1 = reportses1.get(i);
			Reports reports2 = reportses2.get(i);

			assertEquals(reports1, reports2);
		}
	}

	protected void assertEquals(
		ReportFiles reportFiles1, ReportFiles reportFiles2) {

		Assert.assertTrue(
			reportFiles1 + " does not equal " + reportFiles2,
			equals(reportFiles1, reportFiles2));
	}

	protected void assertEquals(Report report1, Report report2) {
		Assert.assertTrue(
			report1 + " does not equal " + report2, equals(report1, report2));
	}

	protected void assertEqualsIgnoringOrder(
		List<Reports> reportses1, List<Reports> reportses2) {

		Assert.assertEquals(reportses1.size(), reportses2.size());

		for (Reports reports1 : reportses1) {
			boolean contains = false;

			for (Reports reports2 : reportses2) {
				if (equals(reports1, reports2)) {
					contains = true;

					break;
				}
			}

			Assert.assertTrue(
				reportses2 + " does not contain " + reports1, contains);
		}
	}

	protected void assertValid(
			com.liferay.imex.rest.trigger.api.client.dto.v1_0.Reports reports)
		throws Exception {

		boolean valid = true;

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			throw new IllegalArgumentException(
				"Invalid additional assert field name " +
					additionalAssertFieldName);
		}

		Assert.assertTrue(valid);
	}

	protected void assertValid(Page<Reports> page) {
		boolean valid = false;

		java.util.Collection<Reports> reportses = page.getItems();

		int size = reportses.size();

		if ((page.getLastPage() > 0) && (page.getPage() > 0) &&
			(page.getPageSize() > 0) && (page.getTotalCount() > 0) &&
			(size > 0)) {

			valid = true;
		}

		Assert.assertTrue(valid);
	}

	protected void assertValid(ReportFiles reportFiles) {
		boolean valid = true;

		for (String additionalAssertFieldName :
				getAdditionalReportFilesAssertFieldNames()) {

			if (Objects.equals("creationDate", additionalAssertFieldName)) {
				if (reportFiles.getCreationDate() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("lastModifiedDate", additionalAssertFieldName)) {
				if (reportFiles.getLastModifiedDate() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("name", additionalAssertFieldName)) {
				if (reportFiles.getName() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("size", additionalAssertFieldName)) {
				if (reportFiles.getSize() == null) {
					valid = false;
				}

				continue;
			}

			throw new IllegalArgumentException(
				"Invalid additional assert field name " +
					additionalAssertFieldName);
		}

		Assert.assertTrue(valid);
	}

	protected void assertValid(Report report) {
		boolean valid = true;

		for (String additionalAssertFieldName :
				getAdditionalReportAssertFieldNames()) {

			if (Objects.equals("content", additionalAssertFieldName)) {
				if (report.getContent() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("identifier", additionalAssertFieldName)) {
				if (report.getIdentifier() == null) {
					valid = false;
				}

				continue;
			}

			throw new IllegalArgumentException(
				"Invalid additional assert field name " +
					additionalAssertFieldName);
		}

		Assert.assertTrue(valid);
	}

	protected String[] getAdditionalAssertFieldNames() {
		return new String[0];
	}

	protected String[] getAdditionalReportFilesAssertFieldNames() {
		return new String[0];
	}

	protected String[] getAdditionalReportAssertFieldNames() {
		return new String[0];
	}

	protected List<GraphQLField> getGraphQLFields() throws Exception {
		List<GraphQLField> graphQLFields = new ArrayList<>();

		return graphQLFields;
	}

	protected List<GraphQLField> getGraphQLFields(Field... fields)
		throws Exception {

		List<GraphQLField> graphQLFields = new ArrayList<>();

		for (Field field : fields) {
			com.liferay.portal.vulcan.graphql.annotation.GraphQLField
				vulcanGraphQLField = field.getAnnotation(
					com.liferay.portal.vulcan.graphql.annotation.GraphQLField.
						class);

			if (vulcanGraphQLField != null) {
				Class<?> clazz = field.getType();

				if (clazz.isArray()) {
					clazz = clazz.getComponentType();
				}

				List<GraphQLField> childrenGraphQLFields = getGraphQLFields(
					ReflectionUtil.getDeclaredFields(clazz));

				graphQLFields.add(
					new GraphQLField(field.getName(), childrenGraphQLFields));
			}
		}

		return graphQLFields;
	}

	protected String[] getIgnoredEntityFieldNames() {
		return new String[0];
	}

	protected boolean equals(Reports reports1, Reports reports2) {
		if (reports1 == reports2) {
			return true;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			throw new IllegalArgumentException(
				"Invalid additional assert field name " +
					additionalAssertFieldName);
		}

		return true;
	}

	protected boolean equals(
		Map<String, Object> map1, Map<String, Object> map2) {

		if (Objects.equals(map1.keySet(), map2.keySet())) {
			for (Map.Entry<String, Object> entry : map1.entrySet()) {
				if (entry.getValue() instanceof Map) {
					if (!equals(
							(Map)entry.getValue(),
							(Map)map2.get(entry.getKey()))) {

						return false;
					}
				}
				else if (!Objects.deepEquals(
							entry.getValue(), map2.get(entry.getKey()))) {

					return false;
				}
			}

			return true;
		}

		return false;
	}

	protected boolean equals(
		ReportFiles reportFiles1, ReportFiles reportFiles2) {

		if (reportFiles1 == reportFiles2) {
			return true;
		}

		for (String additionalAssertFieldName :
				getAdditionalReportFilesAssertFieldNames()) {

			if (Objects.equals("creationDate", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						reportFiles1.getCreationDate(),
						reportFiles2.getCreationDate())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("lastModifiedDate", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						reportFiles1.getLastModifiedDate(),
						reportFiles2.getLastModifiedDate())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("name", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						reportFiles1.getName(), reportFiles2.getName())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("size", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						reportFiles1.getSize(), reportFiles2.getSize())) {

					return false;
				}

				continue;
			}

			throw new IllegalArgumentException(
				"Invalid additional assert field name " +
					additionalAssertFieldName);
		}

		return true;
	}

	protected boolean equals(Report report1, Report report2) {
		if (report1 == report2) {
			return true;
		}

		for (String additionalAssertFieldName :
				getAdditionalReportAssertFieldNames()) {

			if (Objects.equals("content", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						report1.getContent(), report2.getContent())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("identifier", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						report1.getIdentifier(), report2.getIdentifier())) {

					return false;
				}

				continue;
			}

			throw new IllegalArgumentException(
				"Invalid additional assert field name " +
					additionalAssertFieldName);
		}

		return true;
	}

	protected java.util.Collection<EntityField> getEntityFields()
		throws Exception {

		if (!(_reportsResource instanceof EntityModelResource)) {
			throw new UnsupportedOperationException(
				"Resource is not an instance of EntityModelResource");
		}

		EntityModelResource entityModelResource =
			(EntityModelResource)_reportsResource;

		EntityModel entityModel = entityModelResource.getEntityModel(
			new MultivaluedHashMap());

		Map<String, EntityField> entityFieldsMap =
			entityModel.getEntityFieldsMap();

		return entityFieldsMap.values();
	}

	protected List<EntityField> getEntityFields(EntityField.Type type)
		throws Exception {

		java.util.Collection<EntityField> entityFields = getEntityFields();

		Stream<EntityField> stream = entityFields.stream();

		return stream.filter(
			entityField ->
				Objects.equals(entityField.getType(), type) &&
				!ArrayUtil.contains(
					getIgnoredEntityFieldNames(), entityField.getName())
		).collect(
			Collectors.toList()
		);
	}

	protected String getFilterString(
		EntityField entityField, String operator, Reports reports) {

		StringBundler sb = new StringBundler();

		String entityFieldName = entityField.getName();

		sb.append(entityFieldName);

		sb.append(" ");
		sb.append(operator);
		sb.append(" ");

		throw new IllegalArgumentException(
			"Invalid entity field " + entityFieldName);
	}

	protected String invoke(String query) throws Exception {
		HttpInvoker httpInvoker = HttpInvoker.newHttpInvoker();

		httpInvoker.body(
			JSONUtil.put(
				"query", query
			).toString(),
			"application/json");
		httpInvoker.httpMethod(HttpInvoker.HttpMethod.POST);
		httpInvoker.path("http://localhost:8080/o/graphql");
		httpInvoker.userNameAndPassword("test@liferay.com:test");

		HttpInvoker.HttpResponse httpResponse = httpInvoker.invoke();

		return httpResponse.getContent();
	}

	protected JSONObject invokeGraphQLMutation(GraphQLField graphQLField)
		throws Exception {

		GraphQLField mutationGraphQLField = new GraphQLField(
			"mutation", graphQLField);

		return JSONFactoryUtil.createJSONObject(
			invoke(mutationGraphQLField.toString()));
	}

	protected JSONObject invokeGraphQLQuery(GraphQLField graphQLField)
		throws Exception {

		GraphQLField queryGraphQLField = new GraphQLField(
			"query", graphQLField);

		return JSONFactoryUtil.createJSONObject(
			invoke(queryGraphQLField.toString()));
	}

	protected Reports randomReports() throws Exception {
		return new Reports() {
			{
			}
		};
	}

	protected Reports randomIrrelevantReports() throws Exception {
		Reports randomIrrelevantReports = randomReports();

		return randomIrrelevantReports;
	}

	protected Reports randomPatchReports() throws Exception {
		return randomReports();
	}

	protected ReportFiles randomReportFiles() throws Exception {
		return new ReportFiles() {
			{
				creationDate = RandomTestUtil.nextDate();
				lastModifiedDate = RandomTestUtil.nextDate();
				name = RandomTestUtil.randomString();
				size = RandomTestUtil.randomString();
			}
		};
	}

	protected Report randomReport() throws Exception {
		return new Report() {
			{
				content = RandomTestUtil.randomString();
				identifier = RandomTestUtil.randomString();
			}
		};
	}

	protected ReportsResource reportsResource;
	protected Group irrelevantGroup;
	protected Company testCompany;
	protected Group testGroup;

	protected class GraphQLField {

		public GraphQLField(String key, GraphQLField... graphQLFields) {
			this(key, new HashMap<>(), graphQLFields);
		}

		public GraphQLField(String key, List<GraphQLField> graphQLFields) {
			this(key, new HashMap<>(), graphQLFields);
		}

		public GraphQLField(
			String key, Map<String, Object> parameterMap,
			GraphQLField... graphQLFields) {

			_key = key;
			_parameterMap = parameterMap;
			_graphQLFields = Arrays.asList(graphQLFields);
		}

		public GraphQLField(
			String key, Map<String, Object> parameterMap,
			List<GraphQLField> graphQLFields) {

			_key = key;
			_parameterMap = parameterMap;
			_graphQLFields = graphQLFields;
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder(_key);

			if (!_parameterMap.isEmpty()) {
				sb.append("(");

				for (Map.Entry<String, Object> entry :
						_parameterMap.entrySet()) {

					sb.append(entry.getKey());
					sb.append(": ");
					sb.append(entry.getValue());
					sb.append(", ");
				}

				sb.setLength(sb.length() - 2);

				sb.append(")");
			}

			if (!_graphQLFields.isEmpty()) {
				sb.append("{");

				for (GraphQLField graphQLField : _graphQLFields) {
					sb.append(graphQLField.toString());
					sb.append(", ");
				}

				sb.setLength(sb.length() - 2);

				sb.append("}");
			}

			return sb.toString();
		}

		private final List<GraphQLField> _graphQLFields;
		private final String _key;
		private final Map<String, Object> _parameterMap;

	}

	private static final Log _log = LogFactoryUtil.getLog(
		BaseReportsResourceTestCase.class);

	private static BeanUtilsBean _beanUtilsBean = new BeanUtilsBean() {

		@Override
		public void copyProperty(Object bean, String name, Object value)
			throws IllegalAccessException, InvocationTargetException {

			if (value != null) {
				super.copyProperty(bean, name, value);
			}
		}

	};
	private static DateFormat _dateFormat;

	@Inject
	private com.liferay.imex.rest.trigger.api.resource.v1_0.ReportsResource
		_reportsResource;

}