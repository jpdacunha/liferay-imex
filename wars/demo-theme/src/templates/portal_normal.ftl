<!DOCTYPE html>

<#include init />

<html class="${root_css_class}" dir="<@liferay.language key="lang.dir" />" lang="${w3c_language_id}">

<head>
	<title>${the_title} - ${company_name} - My demo</title>

	<meta content="initial-scale=1.0, width=device-width" name="viewport" />

	<@liferay_util["include"] page=top_head_include />
</head>

<body class="${css_class}">

<@liferay_ui["quick-access"] contentId="#main-content" />

<@liferay_util["include"] page=body_top_include />

<@liferay.control_menu />

<div class="container-fluid" id="wrapper">
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
										<form id="_com_liferay_portal_search_web_portlet_SearchPortlet_fm" action="${full_uri}/search_results?p_p_id=com_liferay_portal_search_web_portlet_SearchPortlet&p_p_lifecycle=0&p_p_state=maximized&p_p_mode=view&p_p_col_id=column-1&_com_liferay_portal_search_web_portlet_SearchPortlet_mvcPath=/search.jsp&_com_liferay_portal_search_web_portlet_SearchPortlet_redirect=${full_uri}/search_results?p_p_id=com_liferay_portal_search_web_portlet_SearchPortlet&p_p_lifecycle=0&p_p_state=normal&p_p_mode=view&p_p_col_id=column-1" method="get" class="form" data-fm-namespace="_com_liferay_portal_search_web_portlet_SearchPortlet_" id="_com_liferay_portal_search_web_portlet_SearchPortlet_fm" method="get" name="_com_liferay_portal_search_web_portlet_SearchPortlet_fm">
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

	<#if show_page_section_header>
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
	
	<section id="content">
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
									<button class="btn btn-default" type="submit">Go!</button>
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
						</p>							</div>
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

<@liferay_util["include"] page=body_bottom_include />

<@liferay_util["include"] page=bottom_include />

<!-- inject:js -->
<!-- endinject -->

</body>

</html>