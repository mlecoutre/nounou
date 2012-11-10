(function ($) {


    var accountId = null;
    var userId = null;
    var value = sessionStorage.getItem('apptoken');

         if(value != null && value != ""){
             var token = $.parseJSON(value);
             accountId = token.accountId;
             userId  = token.userId;
             $('#authBox').html('Logged in as <a href="#" class="navbar-link" id="signOut">'+token.userName+'</a>');
             $('#authBox_request').hide();
         } else {
                $('#authBox_request').show();
                $('#authBox').hide();
         }



    $('#signOut').click(function(e){
         sessionStorage.setItem('apptoken', '');

         $('#authBox_request').show();
         $('#authBox').hide();
         //reload the location in order to reinitialize the content
                                  $.ajax({
                                    url: "",
                                    context: document.body,
                                    success: function(s,x){
                                      $(this).html(s);
                                    }
                                  });
    });

    $('#signIn').click(function (e) {
        var token = {
            uid      :  $('#uid').val(),
            password :  $('#pwd').val()
        };
        var data = $.toJSON(token);
        console.log("Auth: " + token.uid)
        var req = $.ajax({
            type: 'POST',
            contentType: 'application/json',
            url: '/services/auth',
            dataType: "json",
            data: data,
        });

        req.done(function (token) {
            console.log("Auth successfully " + token);
               $('#authBox').html("Welcome "+ token.userName);
               data = $.toJSON(token);

               sessionStorage.setItem('apptoken', data);
               //reload the location in order to reinitialize the content
                         $.ajax({
                           url: "",
                           context: document.body,
                           success: function(s,x){
                             $(this).html(s);
                           }
                         });
        });
    });
})(jQuery);