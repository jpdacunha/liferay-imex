<#assign uniqueId = stringUtil.randomId() />

<#assign nb_elements = Images.getSiblings()?size>

<#assign owlCss = "" />
<#if (nb_elements > 1)>
	<#assign owlCss = "owl-carousel owl-theme manual" />
</#if>

<#assign modifiedDate = .vars['reserved-article-modified-date'].data>
<#assign modificationDate = modifiedDate?datetime("EEE, d MMM yyyy HH:mm:ss Z")>
<#assign author = .vars['reserved-article-author-name'].data>

<div id="blog-posts-${uniqueId}-${randomNamespace}">
	<article class="post post-large">
		<div class="post-image ">
    		<#if Images.getSiblings()?has_content>
    		    <div class="slide-image ${owlCss}">
                	<#list Images.getSiblings() as cur_Images>
                	    
                    	<#if cur_Images.getData()?? && cur_Images.getData() != "">
                		    <div class="img-thumbnail">
                			    <img 
                			        class="img-responsive" 
                			        alt="${cur_Images.getAttribute("alt")}" 
                			        src="${cur_Images.getData()}" />
                		    </div>
                		</#if>
                	
                	</#list>
                </div>
            </#if>
		</div>

		<div class="post-date">
			<span class="day">${modificationDate?string["dd"]}</span>
			<span class="month">
			    ${dateUtil.getDate(modificationDate, "MMM", locale, timeZone)}
			</span>
		</div>

		<div class="post-content">

			<h2>${Title.getData()}</h2>
			<p>${Description.getData()}</p>

			<div class="post-meta">
				<span>
				    <i class="fa fa-user"></i> <@liferay.language key="custom-by" /> 
				    <a href="#">${author}</a> 
				</span>
				<span><i class="fa fa-tag"></i> <a href="#">Duis</a>, <a href="#">News</a> </span>
				<span><i class="fa fa-comments"></i> <a href="#">12 Comments</a></span>
			</div>
		</div>
	</article>
</div>

<#if (nb_elements > 1)>
    <script>
    	$(window).load(function() {
    	
    		var $this = $(this);
    		var $slider_${uniqueId} = $('#blog-posts-${uniqueId}-${randomNamespace} .slide-image');
    		
    		$slider_${uniqueId}.owlCarousel({
    			items: 1,
    			nav: false,
    			autoplay: true,
    			pagination: false,
    			autoplayHoverPause: true,
    			mouseDrag: true,
    			lazyLoad: true,
    			autoHeight:true,
    			animateOut: 'fadeOut',
                animateIn: 'fadeIn',
    			//fallbackEasing: "fade",
    			loop: true
    		});
    		
    	});
    </script>
</#if>