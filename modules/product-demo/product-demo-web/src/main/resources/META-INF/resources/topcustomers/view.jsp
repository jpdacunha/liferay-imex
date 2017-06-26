<%@ include file="/init.jsp" %>

<div class="col-md-12 col-lg-12 col-xl-6" style="padding-left: 0px; padding-right: 0px;">
	<section class="panel">
		<header class="panel-heading">
			<div class="panel-actions">
				<a href="#" class="panel-action panel-action-toggle" data-panel-toggle></a>
				<a href="#" class="panel-action panel-action-dismiss" data-panel-dismiss></a>
			</div>

			<h4><liferay-ui:message key="top.customer.chart.title"/></h4>
		</header>
		<div class="panel-body">

			<!-- Flot: Bars -->
			<div class="chart chart-md" id="flotBars"></div>
			
			<script>

				var flotBarsData = ${barArray};

				// See: assets/javascripts/ui-elements/examples.charts.js for more settings.

			</script>

		</div>
	</section>
</div>