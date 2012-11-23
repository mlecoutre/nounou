(function ($) {

	var token  = null;

	$('#synchronize').click(function(){ // store all account configuration in local cache
		console.log("Launch synchronization");
		$('#config').html("<img src='/img/samplevial_empty_256.png' style='width:50px; height:50px;'/>");	
		 // Get Current Appointment data
		 var reqChildren = $.ajax({
		 	type: 'GET',
		 	contentType: 'application/json',
		 	url: '/services/children/account/' + token.accountId
		});
		reqChildren.done(function (children) {
			localStorage.setItem("nounou-config", $.toJSON(children));
			$('#config').html("<img src='/img/samplevial_full_256.png'  style='width:50px; height:50px;'/>");	
			$('#config').effect("bounce", { direction:'down', times:1 }, 500);
		});
	});

	$(document).ready(function () {
		token = $('#authBox').initAuthBox('nounou-auth');
    	var data = localStorage.getItem("nounou-config");
    	config = $.parseJSON(data);
    	if(config == null){
    		$('#config').html("<img src='/img/samplevial_empty_256.png' style='width:50px; height:50px;'/>");	
    	}else{
			$('#config').html("<img src='/img/samplevial_full_256.png' style='width:50px; height:50px;'/>");	
    	}
    });    

})(jQuery);