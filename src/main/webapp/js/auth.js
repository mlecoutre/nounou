/**
 * Authentication plugin
 * Main usage:
 *        var token = ${'#authBox'}.initAuthBox();
 *
 * @author mlecoutre@gmail.com
 */
 (function ($) {

    var storageName = null;

    jQuery.extend(jQuery.fn, {
        /**
         * Allows you to get the authentication token if user is already authentified
         */
        getToken: function (localStorageName) {
            var value = null;
            if (localStorageName != null) {
                value = localStorage.getItem(localStorageName);
            } else {
                value = sessionStorage.getItem('apptoken');
            }
            return $.parseJSON(value);
        },
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
        /**
         * Append 'userName' to the HTML element where it's applied
         */
        displayUserName: function (localStorageName) {
            var value = null;
            if (localStorageName != null) {
                value = localStorage.getItem(localStorageName);
            } else {
                value = sessionStorage.getItem('apptoken');
            }
            if (value != null && value != "") {
                var token = $.parseJSON(value);
                var userName = token.userName;
                $(this).append(userName);
                return userName;
            } else {
                return null;
            }
        },
        /**
         * Allow to initialize the authentication plugin. 
         * Show a form if user not already authenfied else, display it's username
         * @param localStorageName optional: when setted, search on local storage the authentication token.
         */
        initAuthBox: function (localStorageName) {
            var value = null;
            storageName = localStorageName;
            if (localStorageName != null) {
                value = localStorage.getItem(localStorageName);
            } else {
                value = sessionStorage.getItem('apptoken');
            }
            if (value != null && value != "") {
                var token = $.parseJSON(value);
                accountId = token.accountId;
                userId = token.userId;
                $(this).html('Logged in as <a href="#" class="navbar-link" id="signOut">' + token.userName + '</a>');
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
                return token;
            } else {
                $(this).html('<input class="span2" type="text" id="uid" placeholder="Email"><input class="span2" type="password" id="password" placeholder="Password"><button type="submit" class="btn signin" id="signIn">ok</button>');
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
                        console.log("Auth successfully " + token );
                        data = $.toJSON(token);
                        if (storageName != null ){
                            console.log('Store into ' + storageName);
                            localStorage.setItem(storageName, data);
                        }else{
                            sessionStorage.setItem('apptoken', data);
                        }
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
                return null;
            }
        }
    })
})(jQuery);