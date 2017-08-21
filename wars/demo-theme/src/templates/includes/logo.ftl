<#assign rowPercent = theme_settings["custom-theme-logo-reduction-percent"] />
<#assign stickyJqueryValues = ""/>

<#if rowPercent??>
	<#assign
		sizedHeight = site_logo_height - ((rowPercent?number / 100) * site_logo_height)?round
		sizedWidth = site_logo_width - ((rowPercent?number / 100) * site_logo_width)?round	
		stickyJqueryValues = "data-sticky-width='${sizedWidth}' data-sticky-height='${sizedHeight}' data-sticky-top='25'"
	/>
</#if>

<a href="#">
	<#if site_logo?? && site_logo?has_content>
		<img alt="${logo_description}" height="${site_logo_height}" src="${site_logo}" width="${site_logo_width}" ${stickyJqueryValues} />
	<#else>
		<img alt="Liferay DXP" width="259" height="70" src="${images_folder}/template/logo.png" data-sticky-width='207' data-sticky-height='56' data-sticky-top='25'>
	</#if>
</a>