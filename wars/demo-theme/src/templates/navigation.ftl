
<div class="header-nav">
	<button class="btn header-btn-collapse-nav" data-toggle="collapse" data-target=".header-nav-main">
		<i class="fa fa-bars"></i>
	</button>
	<ul class="header-social-icons social-icons hidden-xs">
		<li class="social-icons-facebook"><a href="http://www.facebook.com/" target="_blank" title="Facebook"><i class="fa fa-facebook"></i></a></li>
		<li class="social-icons-twitter"><a href="http://www.twitter.com/" target="_blank" title="Twitter"><i class="fa fa-twitter"></i></a></li>
		<li class="social-icons-linkedin"><a href="http://www.linkedin.com/" target="_blank" title="Linkedin"><i class="fa fa-linkedin"></i></a></li>
	</ul>
	<div class="header-nav-main header-nav-main-effect-1 header-nav-main-sub-effect-1 collapse">
		<nav id="navigation" role="navigation">
			<ul class="nav nav-pills" id="mainNav" aria-label="<@liferay.language key="site-pages" />" role="menubar">
				<#list nav_items as nav_item>
					<#assign
						nav_item_attr_has_popup = ""
						nav_item_attr_selected = ""
						nav_item_css_class = ""
						nav_item_layout = nav_item.getLayout()
					/>

					<#if nav_item.isSelected()>
						<#assign
							nav_item_attr_has_popup = "aria-haspopup='true'"
							nav_item_attr_selected = "aria-selected='true'"
							nav_item_css_class = "active"
						/>
					</#if>

					<li ${nav_item_attr_selected} class="${nav_item_css_class} dropdown" id="layout_${nav_item.getLayoutId()}" role="presentation">
						<a class="dropdown-toggle" aria-labelledby="layout_${nav_item.getLayoutId()}" ${nav_item_attr_has_popup} href="${nav_item.getURL()}" ${nav_item.getTarget()} role="menuitem"><span><@liferay_theme["layout-icon"] layout=nav_item_layout /> ${nav_item.getName()}</span></a>

						<#if nav_item.hasChildren()>
							<ul class="dropdown-menu" role="menu">
								<#list nav_item.getChildren() as nav_child>
									<#assign
										nav_child_attr_selected = ""
										nav_child_css_class = "dropdown-submenu"
									/>

									<#if nav_item.isSelected()>
										<#assign
											nav_child_attr_selected = "active"
										/>
									</#if>

									<li id="layout_${nav_child.getLayoutId()}" role="presentation">
										<a href="${nav_child.getURL()}" ${nav_child.getTarget()} role="menuitem">${nav_child.getName()}</a>
									</li>
								</#list>
							</ul>
						</#if>
					</li>
				</#list>
			</ul>
		</nav>	
	</div>
</div>