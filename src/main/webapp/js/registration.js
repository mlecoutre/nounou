(function($) {

         Child = Backbone.Model.extend({
         		childId     : null,
           		firstName   : null,
                nurseId     : null
         });

         User = Backbone.Model.extend({
         		userId     : null,
         		firstName  : null,
         		lastName   : null,
         		phoneNumber: null,
         		password   : null,
         		type       : null
          });

         Nurse = Backbone.Model.extend({
           		nurseId     : null,
           		firstName  : null,
           		lastName   : null,
           		phoneNumber: null
         });

      /*  $('#nav-main ul li').hoverIntent(function () {
            $(this).find('.dropdown').stop(true,true).slideToggle('500');
        }, function(){});*/

        $('#registerTab a').click(function (e) {
            e.preventDefault();
            $(this).tab('show');
        })

        $( "#birthday" ).datepicker();

    	AppView = Backbone.View.extend({
    		el : $("body"),
    		initialize : function() {
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
    		    "click #registerUser"     : "registerUser",
    		    "click #addNurse"         : "registerNurse"
    		},
    		registerNurse: function(){
    		    var mNurse = new Nurse();
                 mNurse.set({
                 	firstName   : $('#nurseFirstName').val(),
                    lastName    : $('#nurseLastName').val(),
                    phoneNumber : $('#nursePhoneNumber').val()
                  });
                 var data = JSON.stringify(mNurse);
                 $.ajax({
                        type: 'POST',
                        contentType: 'application/json',
                        url: '/services/nurses',
                        dataType: "json",
                        data: data,
                        success: function(data, textStatus, jqXHR){
                            $('#nurseList').append(Mustache.to_html($('#nurse-template').html(), mNurse.toJSON()));
                        },
                        error: function (jqXHR, textStatus, errorThrown){
                            alert("error: "+textStatus+'/'+errorThrown);
                        }
                 });
    		}
    		,registerUser: function(){
                console.log("registerUser "+$('#userFirstName').val());
                var mUser = new User();
                mUser.set({
                	firstName   : $('#userFirstName').val(),
                    lastName    : $('#userLastName').val(),
                    phoneNumber : $('#userPhoneNumber').val(),
                    password    : $('#userPassword').val(),
                    type        : $('#userType').val()
                });
                var data = JSON.stringify(mUser);
                console.log("create mUser object "+data)
                $.ajax({
                        type: 'POST',
                        contentType: 'application/json',
                        url: '/services/users',
                        dataType: "json",
                        data: data,
                        success: function(data, textStatus, jqXHR){
                            //console.log('User created successfully');
                            $('#whiteAccessList').append(Mustache.to_html($('#whiteAccesUser-template').html(), mUser.toJSON()));
                        },
                        error: function (jqXHR, textStatus, errorThrown){
                            alert("error: "+textStatus+'/'+errorThrown);
                        }
                });
                console.log("[END] register user");
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
