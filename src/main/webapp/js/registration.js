(function($) {

      /* $(document).ajaxError(function(e, xhr, settings, exception) {
            alert('error in: ' + settings.url + ' \\n'+'error:\\n' + exception);
       });*/

      /*  $('#nav-main ul li').hoverIntent(function () {
            $(this).find('.dropdown').stop(true,true).slideToggle('500');
      }, function(){});*/

       $('#registerTab a').click(function (e) {
            e.preventDefault();
            $(this).tab('show');
       })
       $('#registrationStart').click(function (e) {
            $('#registerTab a[href="#you"]').tab('show');
       })
       $('#youPrevious').click(function (e) {
           $('#registerTab a[href="#what"]').tab('show');
       })
       $('#youNext').click(function (e) {
           $('#registerTab a[href="#nurse"]').tab('show');
       })
       $('#nursePrevious').click(function (e) {
           $('#registerTab a[href="#you"]').tab('show');
       })
       $('#kidsPrevious').click(function (e) {
           $('#registerTab a[href="#nurse"]').tab('show');
       })
       $('#nurseNext').click(function (e) {
            $('#registerTab a[href="#kids"]').tab('show');
       })

        $('#registerUser').click(function (e) {
               console.log("[START] registerUser "+$('#userFirstName').val());
               var mUser ={ firstName   : $('#userFirstName').val(),
                            lastName    : $('#userLastName').val(),
                            phoneNumber : $('#userPhoneNumber').val(),
                            password    : 'toto',
                            type        : $('#userType').val()
               };

               var data = $.toJSON( mUser );
               console.log("create mUser object "+data)
              var req =  $.ajax({
                           type: 'POST',
                           contentType: 'application/json',
                           url: '/services/users',
                           dataType: "json",
                           data: data,

               });

             req.done(function(user){
                    $('#whiteAccessList').append(Mustache.to_html($('#whiteAccesUser-template').html(), user));
               });

               console.log("[END] register user");
        })

        $('#addNurse').click(function (e) {
                var mNurse = {
                               	firstName   : $('#nurseFirstName').val(),
                                lastName    : $('#nurseLastName').val(),
                                phoneNumber : $('#nursePhoneNumber').val()
                };
                var data = $.toJSON(mNurse);
                var req = $.ajax({
                            type: 'POST',
                            contentType: 'application/json',
                            url: '/services/nurses',
                            dataType: "json",
                            data: data,

                });
                req.done(function(nurse){
                            $('#nurseList').append(Mustache.to_html($('#nurse-template').html(), nurse));
                });
        })

        $('#addKid').click(function (e) {
               var mKid = {
                            firstName   : $('#kidName').val(),
                            birthday    : $('#kidBirthday').val(),
               };
               var data = $.toJSON(mKid);
               $.ajax({
                        type: 'POST',
                        contentType: 'application/json',
                        url: '/services/children',
                        dataType: "json",
                        data: data,
               });
               req.done(function(kid){
                         $('#kidList').append(Mustache.to_html($('#kid-template').html(), kid));
               });
        })
       //$( "#kidBirthday" ).datepicker();
})(jQuery);
