	<div class="row border-bottom">		

 		<nav class="navbar navbar-static-top " role="navigation" style="margin-bottom: 0;  z-index: 111;">
    
	        <div class="navbar-header navbar-search">
	            <a class="navbar-minimalize minimalize-styl-2 btn btn-primary " id="btnClose" href="#"><i class="fa fa-bars"></i> </a>
	            
	            <div class="navbar-form-custom" role="search"> <@liferay.search /></div>
	        </div>
	        <div class="header-right right">
	        
		        <ul class="nav navbar-top-links navbar-right">
		            
		            <#if !is_signed_in>
						<li><a data-redirect="false" href="${sign_in_url}" rel="nofollow" id="sign-in">${sign_in_text}</a></li>
		          	</#if>
		            
	            	<li>
						<a class="right-sidebar-toggle">
					         <i class="fa fa-tasks"></i>
					     </a>
					</li>
					
					<li>
						<a href="javascript:;" class="info-number">
		                    <i class="fa fa-envelope-o"></i>
		                    <span class="badge bg-green">6</span>
	                    </a>
                    </li>
                  </a>
		
		        </ul>
		        
			 </div>
			 
        </nav>
        
    </div>

	<div id="top-indicator" lass="row tile_count">
	
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
