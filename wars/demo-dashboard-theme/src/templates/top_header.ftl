	<div class="row border-bottom">		

 		<nav id="top-nav" class="navbar navbar-static-top " role="navigation" style="margin-bottom: 0;  z-index: 111;">
    
	        <div class="navbar-header navbar-search">
	            <a class="navbar-minimalize minimalize-styl-2 btn btn-primary " id="btnClose" href="#"><i class="fa fa-bars"></i> </a>
	            
	            <div class="navbar-form-custom" role="search"> <@liferay.search /></div>
	        </div>
	        <div class="header-right right">
	        
		        <ul class="nav navbar-top-links navbar-right">
	
					<#if !is_signed_in>
						<li title="${sign_in_text}">
							<a data-redirect="false" href="${sign_in_url}" rel="nofollow" id="sign-in"><i class="fa fa-power-off"></i></a>
						</li>
						
					<#else>
						<li title="<@liferay.language key="custom-theme-mails" />">
							<a href="javascript:;" class="info-number">
			                    <i class="fa fa-envelope-o"></i>
			                    <span class="badge bg-green animated flash">6</span>
		                    </a>
	                    </li>
	                    
	                    <li title="<@liferay.language key="custom-theme-calendar-events" />">
							<a href="javascript:;" class="info-number">
			                    <i class="fa fa-calendar"></i>
			                    <span class="badge bg-green animated flash">2</span>
		                    </a>
	                    </li>
			            		            
		            	<li title="<@liferay.language key="custom-theme-tasks" />">
							<a href="javascript:;" class="info-number">
						         <i class="fa fa-tasks"></i>
						         <span class="badge bg-green animated flash">9</span>
						     </a>
						</li>
						<li title="${sign_out_text}">
							<a data-redirect="false" href="${sign_out_url}" rel="nofollow" id="sign-in">
								<i class="fa fa-power-off"></i>
							</a>
						</li>
		          	</#if>

                  </a>
		
		        </ul>
		        
			 </div>
			 
        </nav>
        
    </div>

	<#if is_signed_in>

		<div id="top-indicator" class="row tile_count animated slideInDown">

	            <div class="col-md-2 col-sm-4 col-xs-6 tile_stats_count">
	              <span class="count_top"><i class="fa fa-user"></i> Total Users</span>
	              <div class="count">2500</div>
	              <span class="count_bottom"><i class="green">4% </i> From last Week</span>
	            </div>
	            <div class="col-md-2 col-sm-4 col-xs-6 tile_stats_count">
	              <span class="count_top"><i class="fa fa-clock-o"></i> Average Time</span>
	              <div class="count">123.50</div>
	              <span class="count_bottom"><i class="green"><i class="fa fa-sort-asc"></i>3% </i> From last Week</span>
	            </div>
	            <div class="col-md-2 col-sm-4 col-xs-6 tile_stats_count">
	              <span class="count_top"><i class="fa fa-user"></i> Total Males</span>
	              <div class="count green">2,500</div>
	              <span class="count_bottom"><i class="green"><i class="fa fa-sort-asc"></i>34% </i> From last Week</span>
	            </div>
	            <div class="col-md-2 col-sm-4 col-xs-6 tile_stats_count">
	              <span class="count_top"><i class="fa fa-user"></i> Total Females</span>
	              <div class="count">4,567</div>
	              <span class="count_bottom"><i class="red"><i class="fa fa-sort-desc"></i>12% </i> From last Week</span>
	            </div>
	            <div class="col-md-2 col-sm-4 col-xs-6 tile_stats_count">
	              <span class="count_top"><i class="fa fa-user"></i> Total Collections</span>
	              <div class="count">2,315</div>
	              <span class="count_bottom"><i class="green"><i class="fa fa-sort-asc"></i>34% </i> From last Week</span>
	            </div>
	            <div class="col-md-2 col-sm-4 col-xs-6 tile_stats_count">
	              <span class="count_top"><i class="fa fa-user"></i> Total Connections</span>
	              <div class="count">7,325</div>
	              <span class="count_bottom"><i class="green"><i class="fa fa-sort-asc"></i>34% </i> From last Week</span>
	            </div>
	            
	    </div>
    </#if>
