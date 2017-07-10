<#setting locale="${locale}">

<#assign nb_elements = entries?size>
<#if entries?has_content>
    <div class="timeline-wrapper clearfix">
        <section class="timeline">
            <#list entries as entry>
                <#assign renderer = entry.getAssetRenderer()>
                <#assign className = renderer.getClassName()>
                
                <#assign contentTitle = entry.getTitle(locale)>
                <#assign contentSummary = entry.getDescription(locale)>
                
                <#assign smallImageUrl = "">
                <#if renderer.getThumbnailPath(renderRequest)??>
                    <#assign smallImageUrl = renderer.getThumbnailPath(renderRequest)>
                </#if>
                
                <#assign viewURL = assetPublisherHelper.getAssetViewURL(renderRequest, renderResponse, entry)>
                <#assign viewURL = renderer.getURLViewInContext(renderRequest, renderResponse, viewURL)>
        
                <#assign author = renderer.getUserName()>
                
                <#assign alignementCSS = "right">
                
                <#if entry?is_odd_item>
                    <#assign alignementCSS = "left">
                </#if>
        
                <article class="timeline-box ${alignementCSS} post post-medium">
                
                    <#if smallImageUrl != "">
                        <div class="row">
                        
                        	<div class="col-md-12">
                        	    <a href="${viewURL}">
                            	    <span class="thumb-info thumb-info-lighten">
                            	        <span class="thumb-info-wrapper">
                                    		<div class="post-image">
                								<div class="img-thumbnail ">
                									<img 
                									    class="img-responsive" 
                									    src="${smallImageUrl}" 
                									    alt="${contentTitle}">
                								</div>
                                    		</div>
                                		</span>
                            		</span>
                        		</a>
                        	</div>
                        
                        </div>
                    </#if>
                    <div class="row">
                    	<div class="col-md-12">
                    
                    		<div class="post-content">
                    			<h4 class="heading-primary"><a href="${viewURL}"> ${contentTitle}</a></h4>
                    			<p>${contentSummary}</p>
                    		</div>
                    
                    	</div>
                    </div>
                    <div class="row">
                    	<div class="col-md-12">
                    		<div class="post-meta">
                    			<span>
                    			    <i class="fa fa-calendar"></i> 
                    			    ${entry.getModifiedDate()?date?string.short}  
                    			</span><br>
                    		</div>
                    	</div>
                    </div>
                    <div class="row">
                    	<div class="col-md-12">
                    		<div class="post-meta">
                    			<span><i class="fa fa-user"></i> <@liferay.language key="custom-by" /> <a href="#">${author}</a> </span>
                    			<span><i class="fa fa-tag"></i> <a href="#">Duis</a>, <a href="#">News</a> </span>
                    			<span><i class="fa fa-comments"></i> <a href="#">12 Comments</a></span>
                    		</div>
                    	</div>
                    </div>
                    <div class="row">
                    	<div class="col-md-12">
                    		<a href="${viewURL}" class="btn btn-xs btn-primary pull-right">Read more...</a>
                    	</div>
                    </div>
                </article>
            </#list>
        </section>
    </div>
<#else>
    <div class="alert alert-info"><@liferay.language key="custom-no-content-found" /> </span>
</#if>