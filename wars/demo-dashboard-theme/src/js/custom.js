
$('#navbar-closed').change(function() {
	$("body").toggleClass("canvas-menu");
	
	if($(this).is(":checked")) {
		$(".navbar-closed").html('<a class="close-canvas-menu"><i class="fa fa-times"></i></a>');
		$('.close-canvas-menu').click( function() {
		    $("body").toggleClass("mini-navbar");
		    SmoothlyMenu();
		});
      return;
    }
	$(".navbar-closed").html('');
});
