<%@ include file="/init.jsp" %>

<header class="panel-heading"> 
	<h4><liferay-ui:message key="order.list.incoming.orders"/></h4> 
</header>

<liferay-ui:search-container delta="${searchContainer.delta}" emptyResultsMessage="${searchContainer.emptyResultsMessage}" searchContainer="${searchContainer}" >
	
	<liferay-ui:search-container-results  results="${searchContainer.results}"/>

		
	<liferay-ui:search-container-row className="com.liferay.product.demo.portlet.orderslist.model.OrderUI" keyProperty="orderId" modelVar="cur">
		<liferay-ui:search-container-row-parameter
				name="cur"
				value="<%= cur%>"
			/>
				
		<liferay-ui:search-container-column-text name="orders.list.order.date"  value="${cur.orderDate}" />
		<liferay-ui:search-container-column-text name="orders.list.order.status"  			value="${cur.orderStatus}"   />	
		<liferay-ui:search-container-column-text name="orders.list.customer.id"  	value="${cur.customerId}" orderable="<%= true %>"orderableProperty="customerId" />
		<liferay-ui:search-container-column-text name="orders.list.customer.company" value="${cur.companyName}"/>
		<liferay-ui:search-container-column-text name="orders.list.order.amount" value="${cur.orderAmount} ${cur.currency}" orderable="<%= true %>" orderableProperty="orderAmount"  />
		<liferay-ui:search-container-column-text name="orders.list.order.country" value="${cur.country}" orderable="<%= true %>" orderableProperty="orderCountry"    />

		</liferay-ui:search-container-row>
	
		<liferay-ui:search-iterator searchContainer="${searchContainer}" paginate="true" />
		
</liferay-ui:search-container>
