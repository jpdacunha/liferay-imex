<!DOCTYPE html>

<#include init />

<html class="${root_css_class}" dir="<@liferay.language key="lang.dir" />" lang="${w3c_language_id}">

<head>
	<title>${the_title} - ${company_name}</title>
	
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">	

	<meta content="initial-scale=1.0, width=device-width" name="viewport" />
	
	<meta http-equiv="cache-control" content="max-age=0" />
	<meta http-equiv="cache-control" content="no-cache" />
	<meta http-equiv="expires" content="0" />
	<meta http-equiv="expires" content="Tue, 01 Jan 1980 1:00:00 GMT" />
	<meta http-equiv="pragma" content="no-cache" />	
	
	<link href="https://fonts.googleapis.com/css?family=Open+Sans:300,400,600,700,800%7CShadows+Into+Light" rel="stylesheet" type="text/css">
		
	<script src="${javascript_folder}/template/modernizr.min.js"></script>	
	
	<@liferay_util["include"] page=top_head_include />
	
	<style>
		${demo_main_css}
	
	</style>
	
</head>

<body class="${css_class}">

<@liferay_ui["quick-access"] contentId="#main-content" />

<@liferay_util["include"] page=body_top_include />

<@liferay.control_menu />

<div class="container-fluid" id="wrapper">
	<div id="demo-wrapper">
		<header id="header">
					<div class="header-body">
						<div class="header-container container">
							<div class="header-row">
								<div class="header-column">
									<div class="header-logo">
										<a href="#">
											<#if site_logo?? && site_logo?has_content>
											<img alt="${logo_description}" height="${site_logo_height}" src="${site_logo}" width="${site_logo_width}" />
											<#else>
											<img alt="Porto" width="111" height="54" src="${images_folder}/template/logo.png">
											</#if>
										</a>
									</div>
								</div>
								<div class="header-column">
									<div class="header-row">
										<div class="header-search hidden-xs">
										<#assign delimiter = current_url?contains('/group/')?then('/group/','/web/') />
										<#assign 
											uri = current_url?keep_after(delimiter)
											context_uri = uri?keep_before('/')	
											full_uri = portal_url + delimiter + context_uri
											sign_in_url = portal_url + '/group/' + context_uri + '/home' />
											<form id="_com_liferay_portal_search_web_portlet_SearchPortlet_fm" action="${full_uri}/search-results?p_p_id=com_liferay_portal_search_web_portlet_SearchPortlet&p_p_lifecycle=0&p_p_state=maximized&p_p_mode=view&p_p_col_id=column-1&_com_liferay_portal_search_web_portlet_SearchPortlet_mvcPath=/search.jsp&_com_liferay_portal_search_web_portlet_SearchPortlet_redirect=${full_uri}/search_results?p_p_id=com_liferay_portal_search_web_portlet_SearchPortlet&p_p_lifecycle=0&p_p_state=normal&p_p_mode=view&p_p_col_id=column-1" method="get" class="form" data-fm-namespace="_com_liferay_portal_search_web_portlet_SearchPortlet_" id="_com_liferay_portal_search_web_portlet_SearchPortlet_fm" method="get" name="_com_liferay_portal_search_web_portlet_SearchPortlet_fm">
												<div class="input-group">
													<input type="text" class="form-control" name="_com_liferay_portal_search_web_portlet_SearchPortlet_keywords" id="_com_liferay_portal_search_web_portlet_SearchPortlet_keywords" placeholder="Search..." required>
													<input name="_com_liferay_portal_search_web_portlet_SearchPortlet_formDate" type="hidden" value="${.now?long}">
													<input name="p_p_state" type="hidden" value="maximized">
													<input name="p_p_id" type="hidden" value="com_liferay_portal_search_web_portlet_SearchPortlet">
													<input name="p_p_lifecycle" type="hidden" value="0">
													<input name="p_p_mode" type="hidden" value="view">
													<input name="p_p_col_id" type="hidden" value="column-1">
													<input name="p_p_col_count" type="hidden" value="5">
													<input name="_com_liferay_portal_search_web_portlet_SearchPortlet_mvcPath" type="hidden" value="/search.jsp">
													<input name="_com_liferay_portal_search_web_portlet_SearchPortlet_redirect" type="hidden" value="${full_uri}/search_results?p_p_id=com_liferay_portal_search_web_portlet_SearchPortlet&p_p_lifecycle=0&p_p_state=normal&p_p_mode=view&p_p_col_id=column-1&p_p_col_count=5">
													<input name="_com_liferay_portal_search_web_portlet_SearchPortlet_scope" type="hidden" value="this-site">
													<span class="input-group-btn">
														<button class="btn btn-default" type="submit"><i class="fa fa-search"></i></button>
													</span>
												</div>
											</form>
										</div>
										<nav class="header-nav-top">
											<ul class="nav nav-pills">
												<#if is_signed_in>
												<#else>
												<#--<li class="hidden-xs">
													<a href='${current_url?replace("/web/", "/group/")}'><i class="fa fa-sign-in"></i> Sign-In</a>
												</li>--> 
												<li><@liferay_portlet["runtime"]
														defaultPreferences=""
														portletName="com_liferay_product_navigation_user_personal_bar_web_portlet_ProductNavigationUserPersonalBarPortlet"
													/></li>
												</#if>
												<li>
													<@liferay.languages default_preferences="${freeMarkerPortletPreferences}" />
												</li>
											</ul>
										</nav>
									</div>
									<div class="header-row">
	
	
										<#if has_navigation && is_setup_complete>
											<#include "${full_templates_path}/navigation.ftl" />
										</#if>
										
									</div>								
								</div>
								
							</div>
							
						</div>
					</div>
		</header>
	
		<#if demo_is_regular_page = "true">
			<section class="theme-page-header">
				<div class="container">
					<div class="row">
						<div class="col-md-12">
							<ul class="breadcrumb">
								<li><a href="#">Home</a></li>
								<li class="active">Pages</li>
							</ul>
						</div>
					</div>
					<div class="row">
						<div class="col-md-12">
							<h1>${page_name}</h1>
						</div>
					</div>
				</div>
			</section>	
		</#if>
		
		<section id="content" class="container-fluid-1280">
			<#if selectable>
				<@liferay_util["include"] page=content_include />
			<#else>
				${portletDisplay.recycle()}
	
				${portletDisplay.setTitle(the_title)}
	
				<@liferay_theme["wrap-portlet"] page="portlet.ftl">
					<@liferay_util["include"] page=content_include />
				</@>
			</#if>
		</section>
	
		<footer id="footer">
			<div class="container">
				<div class="row">
					<div class="footer-ribbon">
						<span>Get in Touch</span>
					</div>
					<div class="col-md-3">
						<div class="newsletter">
							<h4>Newsletter</h4>
							<p>Keep up on our always evolving product features and technology. Enter your e-mail and subscribe to our newsletter.</p>
		
							<div class="alert alert-success hidden" id="newsletterSuccess">
								<strong>Success!</strong> You've been added to our email list.
							</div>
		
							<div class="alert alert-danger hidden" id="newsletterError"></div>
		
							<form id="newsletterForm" action="php/newsletter-subscribe.php" method="POST">
								<div class="input-group">
									<input class="form-control" placeholder="Email Address" name="newsletterEmail" id="newsletterEmail" type="text">
									<span class="input-group-btn">
										<button class="btn btn-primary" type="submit">Go!</button>
									</span>
								</div>
							</form>
						</div>
					</div>
					<div class="col-md-3">
						<h4>Latest Tweets</h4>
						<div id="tweet" class="twitter" data-plugin-tweets data-plugin-options='{"username": "", "count": 2}'>
							<p>Please wait...</p>
						</div>
					</div>
					<div class="col-md-4">
						<div class="contact-details">
							<h4>Contact Us</h4>
							<ul class="contact">
								<li><p><i class="fa fa-map-marker"></i> <strong>Address:</strong> 1234 Street Name, City Name, United States</p></li>
								<li><p><i class="fa fa-phone"></i> <strong>Phone:</strong> (123) 456-789</p></li>
								<li><p><i class="fa fa-envelope"></i> <strong>Email:</strong> <a href="mailto:mail@example.com">mail@example.com</a></p></li>
							</ul>
						</div>
					</div>
					<div class="col-md-2">
						<h4>Follow Us</h4>
						<ul class="social-icons">
							<li class="social-icons-facebook"><a href="http://www.facebook.com/" target="_blank" title="Facebook"><i class="fa fa-facebook"></i></a></li>
							<li class="social-icons-twitter"><a href="http://www.twitter.com/" target="_blank" title="Twitter"><i class="fa fa-twitter"></i></a></li>
							<li class="social-icons-linkedin"><a href="http://www.linkedin.com/" target="_blank" title="Linkedin"><i class="fa fa-linkedin"></i></a></li>
						</ul>
					</div>
				</div>
			</div>
			<div class="footer-copyright">
				<div class="container">
					<div class="row">
						<div class="col-md-8">
							<p class="powered-by">
								<@liferay.language key="powered-by" /> <a href="http://www.liferay.com" rel="external">Liferay</a>
							</p>							
						</div>
						<div class="col-md-4">
							<nav id="sub-menu">
								<ul>
									<li><a href="page-faq.html">FAQ's</a></li>
									<li><a href="sitemap.html">Sitemap</a></li>
									<li><a href="contact-us.html">Contact</a></li>
								</ul>
							</nav>
						</div>
					</div>
				</div>
			</div>
		</footer>
	</div>	
</div>

<@liferay_util["include"] page=body_bottom_include />

<@liferay_util["include"] page=bottom_include />

<!-- inject:js -->
		<!-- Hide the AMD loader so that JS libraries register themselves as globals (rather than AMD modules) -->
		<script>
			define._amd = define.amd;
			define.amd = false;
		</script>
		<script src="${javascript_folder}/template/jquery.browser.mobile.js"></script>
		<script src="${css_folder}/template/rs-plugin/js/jquery.themepunch.tools.min.js"></script>
		<script src="${css_folder}/template/rs-plugin/js/jquery.themepunch.revolution.min.js"></script>
		<script src="${javascript_folder}/template/jquery.appear.min.js"></script>
		<script src="${javascript_folder}/template/jquery.easing.min.js"></script>
		<script src="${javascript_folder}/template/jquery.gmap.min.js"></script>
		<script src="${css_folder}/template/circle-flip-slideshow/js/jquery.flipshow.min.js"></script>
		<script src="${javascript_folder}/template/jquery.easy-pie-chart/jquery.easy-pie-chart.js"></script>
		<script src="${javascript_folder}/template/raphael/raphael.js"></script>
		<script src="${javascript_folder}/template/morris.js/morris.js"></script>
		<script src="${javascript_folder}/template/chartist/chartist.js"></script>
		<script src="${javascript_folder}/template/jquery-sparkline/jquery-sparkline.js"></script>
		
		<script src="${css_folder}/template/select2/js/select2.js"></script>
		<script src="${javascript_folder}/template/jquery-datatables/media/js/jquery.dataTables.js"></script>
		<script src="${javascript_folder}/template/jquery-datatables/extras/TableTools/js/dataTables.tableTools.min.js"></script>
		<script src="${javascript_folder}/template/jquery-datatables-bs3/assets/js/datatables.js"></script>	  

		<script src="${javascript_folder}/template/tables/examples.datatables.default.js"></script>
		<script src="${javascript_folder}/template/tables/examples.datatables.row.with.details.js"></script>
		<script src="${javascript_folder}/template/tables/examples.datatables.tabletools.js"></script>
		

		<script src="${javascript_folder}/template/bootstrap-multiselect.js"></script>		
		<script src="${javascript_folder}/template/flot/jquery.flot.js"></script>
		<script src="${javascript_folder}/template/flot.tooltip/flot.tooltip.js"></script>
		<script src="${javascript_folder}/template/flot/jquery.flot.pie.js"></script>
		<script src="${javascript_folder}/template/flot/jquery.flot.categories.js"></script>
		<script src="${javascript_folder}/template/flot/jquery.flot.resize.js"></script>

		<script src="${javascript_folder}/template/snap.svg.js"></script>
		<script src="${javascript_folder}/template/liquid.meter.js"></script>

		<script src="${javascript_folder}/template/view.home.js"></script>	
		<script src="${javascript_folder}/template/common.min.js"></script>
		
		<script src="${javascript_folder}/template/theme.js"></script>	
		<script src="${javascript_folder}/template/theme.init.js"></script>			
		
		<script src="${javascript_folder}/template/examples.dashboard.js"></script>
		<script src="${javascript_folder}/template/examples.charts.js"></script>
		
		<script src="${javascript_folder}/template/owl.carousel/owl.carousel.min.js"></script>
		
		
		<!-- Restore the AMD loader -->
		<script>
			define.amd = define._amd;
		</script>		
		
		
<!-- endinject -->

		<script src="http://maps.google.com/maps/api/js?key=AIzaSyDykVPzP8KGMO0h_SFEm-2Mm6DDoMXaZxk"></script>
		<script>

			/*
			Map Settings

				Find the Latitude and Longitude of your address:
					- http://universimmedia.pagesperso-orange.fr/geo/loc.htm
					- http://www.findlatitudeandlongitude.com/find-address-from-latitude-and-longitude/

			*/

			// Map Markers
			var mapMarkers = [{
				address: "New York, NY 10017",
				html: "<strong>New York Office</strong><br>New York, NY 10017",
				icon: {
					image: "img/pin.png",
					iconsize: [26, 46],
					iconanchor: [12, 46]
				},
				popup: true
			}];

			// Map Initial Location
			var initLatitude = 40.75198;
			var initLongitude = -73.96978;

			// Map Extended Settings
			var mapSettings = {
				controls: {
					draggable: (($.browser.mobile) ? false : true),
					panControl: true,
					zoomControl: true,
					mapTypeControl: true,
					scaleControl: true,
					streetViewControl: true,
					overviewMapControl: true
				},
				scrollwheel: false,
				markers: mapMarkers,
				latitude: initLatitude,
				longitude: initLongitude,
				zoom: 16
			};

			var map = $("#googlemaps").gMap(mapSettings);

			// Map Center At
			var mapCenterAt = function(options, e) {
				e.preventDefault();
				$("#googlemaps").gMap("centerAt", options);
			}

		</script>	

</body>

</html>