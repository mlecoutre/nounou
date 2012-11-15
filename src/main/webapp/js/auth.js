(function ($) {

    var accountId = null;
    var userId = null;



    jQuery.extend(jQuery.fn, {
        autologin: function (user) {
            console.log("autologin: " + $.toJSON(user));
            var token = {
                userId: user.userId,
                accountId: user.accountId,
                userName: user.firstName,
            };
            data = $.toJSON(token);
            sessionStorage.setItem('apptoken', data);
            $(this).initAuthBox();
        },
        initAuthBox: function () {
            var value = sessionStorage.getItem('apptoken');

            if (value != null && value != "") {
                var token = $.parseJSON(value);
                accountId = token.accountId;
                userId = token.userId;
                $(this).html('Logged in as <a href="#" class="navbar-link" id="signOut">' + token.userName + '</a>');

            } else {
                $(this).html('<input class="span2" type="text" id="uid" placeholder="Email"><input class="span2" type="password" id="password" placeholder="Password"><button type="submit" class="btn signin" id="signIn" data-i18n="commons:auth.signin"></button>');
            }

            $('#signOut').click(function (e) {
                sessionStorage.setItem('apptoken', '');
                //reload the location in order to reinitialize the content
                $.ajax({
                    url: "",
                    context: document.body,
                    success: function (s, x) {
                        $(this).html(s);
                    }
                });
            });

            $('#signIn').click(function (e) {
                var token = {
                    uid: $('#uid').val(),
                    password: $('#password').val()
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
                    data = $.toJSON(token);
                    sessionStorage.setItem('apptoken', data);
                    //reload the location in order to reinitialize the content
                    $.ajax({
                        url: "",
                        context: document.body,
                        success: function (s, x) {
                            $(this).html(s);
                        }
                    });
                });
            });
        }})


    })(jQuery);