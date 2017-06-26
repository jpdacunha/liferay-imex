(function($) {

	'use strict';
	if (document.getElementById("ordersPerfsFlotDashBasic")) {

		/* if (typeof(flotDashBasicData) != 'undefined') { */
	
			var flotDashBasic = $.plot('#ordersPerfsFlotDashBasic', ordersPerfsData, {
				series: {
					lines: {
						show: true,
						fill: true,
						lineWidth: 1,
						fillColor: {
							colors: [{
								opacity: 0.45
							}, {
								opacity: 0.45
							}]
						}
					},
					points: {
						show: true
					},
					shadowSize: 0
				},
				grid: {
					hoverable: true,
					clickable: true,
					borderColor: 'rgba(0,0,0,0.1)',
					borderWidth: 1,
					labelMargin: 15,
					backgroundColor: 'transparent'
				},
				yaxis: {
					min: 0,
					max: 500,
					color: 'rgba(0,0,0,0.1)'
				},
				xaxis: {
					color: 'rgba(0,0,0,0)'
				},
				tooltip: true,
				tooltipOpts: {
					content: '%s: Value of %x is %y',
					shifts: {
						x: -60,
						y: 25
					},
					defaultTheme: false
				}
			});
		}
		
}).apply(this, [jQuery]);
