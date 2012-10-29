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

         var AccessList = Backbone.Collection.extend({
           model: User
         });

         var Nurses = Backbone.Collection.extend({
           model: Nurse
         });

         var Children = Backbone.Collection.extend({
           model: Child
         });

       $(document).ajaxError(function(e, xhr, settings, exception) {
       alert('error in: ' + settings.url + ' \\n'+'error:\\n' + exception);
       });

      /*  $('#nav-main ul li').hoverIntent(function () {
            $(this).find('.dropdown').stop(true,true).slideToggle('500');
        }, function(){});*/

        $('#registerTab a').click(function (e) {
            e.preventDefault();
            $(this).tab('show');
        })

        $( "#kidBirthday" ).datepicker();

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
    		    "click #registerUser"     : "registerUser",
    		    "click #addNurse"         : "registerNurse",
    		    "click #addKid"           : "registerKid"
    		},
    		registerKid: function(){
    		    var mKid = new Child();
    		       mKid.set({
                      firstName   : $('#kidName').val(),
                      birthday    : $('#kidBirthday').val(),
                   });
                   var data = JSON.stringify(mKid);
                      $.ajax({
                        type: 'POST',
                        contentType: 'application/json',
                        url: '/services/children',
                        dataType: "json",
                        data: data,
                        success: function(data, textStatus, jqXHR){
                            $('#kidList').append(Mustache.to_html($('#kid-template').html(), mKid.toJSON()));
                        },
                        error: function (jqXHR, textStatus, errorThrown){
                            alert("error: "+textStatus+'/'+errorThrown);
                        }
                   });
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
                console.log("[START] registerUser "+$('#userFirstName').val());
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
                        data: data
                }).done(function(){
                    console.log('User created successfully');
                    $('#whiteAccessList').append(Mustache.to_html($('#whiteAccesUser-template').html(), mUser.toJSON()));
                }).fail(function(){
                        alert("error: ");
                });
                console.log("[END] register user");
    		},
    		showWhatTab: function(){
              $('#registerTab a[href="#home"]').tab('show');
            },
    		showYouTab: function(){
    		  $('#registerTab a[href="#you"]').tab('show');
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
