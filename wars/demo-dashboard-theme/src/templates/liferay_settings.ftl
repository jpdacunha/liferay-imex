	<#if  (getterUtil.getBoolean(theme_settings['collapse-menu']!"", false)  == true)>
		<script type="text/javascript">
	        $("body").addClass('mini-navbar');
	        SmoothlyMenu();
	
	        if (localStorageSupport) {
	            localStorage.setItem("collapse_menu",'on');
	        }
		</script>
	<#else>
		<script type="text/javascript">
	        $("body").removeClass('mini-navbar');
	        SmoothlyMenu();
	
	        if (localStorageSupport) {
	            localStorage.setItem("collapse_menu",'off');
	        }
		</script>       
	</#if>
	
	
	<#if  (getterUtil.getBoolean(theme_settings['fixed-sidebar']!"", false)  == true)>
		<script type="text/javascript">
	        $("body").addClass('fixed-sidebar');
	        $('.sidebar-collapse').slimScroll({
	            height: '100%',
	            railOpacity: 0.9
	        });
	
	        if (localStorageSupport) {
	            localStorage.setItem("fixedsidebar",'on');
	        } 
		</script>
	<#else>
		<script type="text/javascript">
			$('.sidebar-collapse').attr('style', '');
			$("body").removeClass('fixed-sidebar');
			
			if (localStorageSupport) {
			    localStorage.setItem("fixedsidebar",'off');
			}
		</script>       
	</#if>
	
	<#if  (getterUtil.getBoolean(theme_settings['top-navbar']!"", false)  == true)>
		<script type="text/javascript">
			$(".navbar-static-top").removeClass('navbar-static-top').addClass('navbar-fixed-top');
			$("body").removeClass('boxed-layout');
			$("body").addClass('fixed-nav');
			
			if (localStorageSupport) {
			    localStorage.setItem("boxedlayout",'off');
			}
			
			if (localStorageSupport) {
			    localStorage.setItem("fixednavbar",'on');
			}
		</script>
	<#else>
		<script type="text/javascript">
		   	$(".navbar-fixed-top").removeClass('navbar-fixed-top').addClass('navbar-static-top');
			$("body").removeClass('fixed-nav');
		
		    if (localStorageSupport) {
		        localStorage.setItem("fixednavbar",'off');
		    }
		</script>       
	</#if>
	
	<#if  (getterUtil.getBoolean(theme_settings['boxed-layout']!"", false)  == true)>
		<script type="text/javascript">
		    $("body").addClass('boxed-layout');
		    $(".navbar-fixed-top").removeClass('navbar-fixed-top').addClass('navbar-static-top');
		    $("body").removeClass('fixed-nav');
		    $(".footer").removeClass('fixed');
		
		    if (localStorageSupport) {
		        localStorage.setItem("fixednavbar",'off');
		    }
		
		    if (localStorageSupport) {
		        localStorage.setItem("fixedfooter",'off');
		    }
	
	        if (localStorageSupport) {
	            localStorage.setItem("boxedlayout",'on');
	        }
		</script>
	<#else>
		<script type="text/javascript">
	    	$("body").removeClass('boxed-layout');
	
	        if (localStorageSupport) {
	            localStorage.setItem("boxedlayout",'off');
	        }
		</script>       
	</#if>
	
	<#if  (getterUtil.getBoolean(theme_settings['fixed-footer']!"", false)  == true)>
		<script type="text/javascript">
		    $("body").removeClass('boxed-layout');
		    $(".footer").addClass('fixed');
		
		    if (localStorageSupport) {
		        localStorage.setItem("boxedlayout",'off');
		    }
		
		    if (localStorageSupport) {
		        localStorage.setItem("fixedfooter",'on');
		    }
		</script>
	<#else>
		<script type="text/javascript">
	        $(".footer").removeClass('fixed');
	
	        if (localStorageSupport) {
	            localStorage.setItem("fixedfooter",'off');
	        }
		</script>       
	</#if>
		    
		
	<script type="text/javascript">
		try{
		    if (localStorageSupport) {
		        var collapse = localStorage.getItem("collapse_menu");
		        var fixedsidebar = localStorage.getItem("fixedsidebar");
		        var fixednavbar = localStorage.getItem("fixednavbar");
		        var boxedlayout = localStorage.getItem("boxedlayout");
		        var fixedfooter = localStorage.getItem("fixedfooter");
		
		        if (collapse == 'on') {
		            $('#collapsemenu').prop('checked','checked');
		        }
		        if (fixedsidebar == 'on') {
		            $('#fixedsidebar').prop('checked','checked');
		        }
		        if (fixednavbar == 'on') {
		            $('#fixednavbar').prop('checked','checked');
		        }
		        if (boxedlayout == 'on') {
		            $('#boxedlayout').prop('checked','checked');
		        }
		        if (fixedfooter == 'on') {
		            $('#fixedfooter').prop('checked','checked');
		        }
		    }
	    }catch(exception){
	    	console.log(exception);
	    }
	    
	</script>
	