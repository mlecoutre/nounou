(function($) {

         User = Backbone.Model.extend({
         		userId     : null,
         		firstName  : null,
         		lastName   : null,
         		phoneNumber: null,
         		password   : null,
         		type       : null
          });


    	AppView = Backbone.View.extend({
    		el : $("body"),
    		initialize : function() {
              // $( "#birthday" ).datepicker();

    			console.log('Initialize registration View');
    		},
    		events : {
    		    "click #registrationStart": "showYouTab",
    		    "click #youPrevious"      : "showWhatTab",
    		    "click #youNext"          : "showNurseTab",
    		    "click #nursePrevious"    : "showYouTab",
    		    "click #kidsPrevious"     : "showNurseTab",
    		    "click #nurseNext"        : "showKidsTab",
    		    "click #you"              : "startRegistration",
    		    "click #registerUser"     : "registerUser"
    		},
    		registerUser: function(){
                console.log("registerUser "+$('#userFirstName').val());
                var mUser = new User();
                mUser.set({
                	firstName   : $('#userFirstName').val(),
                    lastName    : $('#userFirstName').val(),
                    phoneNumber : $('#userPhoneNumber').val(),
                    password    : $('#userPassword').val(),
                    type        : $('#userType').val()
                });
                console.log("create mUser object "+JSON.stringify(mUser))
    		},
    		showWhatTab: function(){
              $('#registerTab a[href="#home"]').tab('show');
            },
    		showYouTab: function(){
    		  $('#registerTab a[href="#you"]').tab('show');
    		},
    		showYouTab: function() {
              $('#registerTab a[href="#you"]').tab('show');
    		},
    		showHomeTab: function() {
    		  $('#registerTab a[href="#home"]').tab('show');
    		},
            showKidsTab: function() {
              $('#registerTab a[href="#kids"]').tab('show');
            },
            showNurseTab: function() {
              $('#registerTab a[href="#nurse"]').tab('show');
            }
    	});

    	var appview = new AppView;



})(jQuery);
