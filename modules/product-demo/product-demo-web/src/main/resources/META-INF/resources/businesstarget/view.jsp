<%@ include file="/init.jsp" %>

<div class="col-md-12 col-lg-12 col-xl-6" style="padding-left: 0px; padding-right: 0px;">
	<section class="panel">
		<header class="panel-heading">
			<h4><liferay-ui:message key="business.target.pie1.title"/></h4>
			<p class="panel-subtitle"><liferay-ui:message key="business.target.pie1.subtitle"/></p>
		</header>
		<div class="panel-body">
			<div class="row text-center">
				<div class="circular-bar">
					<div class="circular-bar-chart" data-percent="${businessTargetFacade.indicator1}" data-plugin-options='{ "barColor": "${businessTargetFacade.indicator1Color}", "delay": 300 }'>
						<strong><liferay-ui:message key="business.target.pie1.pietitle"/></strong>
						<label><span class="percent">${businessTargetFacade.indicator1}</span>%</label>
					</div>
				</div>
			</div>
		</div>
	</section>
	
	<section class="panel">
		<header class="panel-heading">
			<p class="panel-subtitle"><liferay-ui:message key="business.target.pie2.subtitle"/></p>
		</header>
		<div class="panel-body">
			<div class="row text-center">
				<div class="circular-bar">
					<div class="circular-bar-chart" data-percent="${businessTargetFacade.indicator2}" data-plugin-options='{ "barColor": "${businessTargetFacade.indicator2Color}", "delay": 600 }'>
						<strong><liferay-ui:message key="business.target.pie2.pietitle"/></strong>
						<label><span class="percent">${businessTargetFacade.indicator2}</span>%</label>
					</div>
				</div>
			</div>
		</div>
	</section>
</div>