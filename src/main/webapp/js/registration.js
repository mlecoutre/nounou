(function ($) {

    //TODO initialize accountId using cookie and authentication mechanism
    //should be null when the user is not authentified
    var accountId = 1;

    /** PAGE INITIALIZATION **/
    $("#kidBirthday").datepicker();

    $(document).ready(function () {
        //get Account
        var reqAccount = $.ajax({
            type: 'GET',
            contentType: 'application/json',
            url: '/services/users/account/' + accountId,
            //       async: false
        });
        reqAccount.done(function (users) {
            for (i = 0; i < users.length; i++) {
                var user = users[i];
                $('#whiteAccessList').append(Mustache.to_html($('#whiteAccesUser-template').html(), user));
            };
        });

        //get Nurse data.
        var reqNurse = $.ajax({
            type: 'GET',
            contentType: 'application/json',
            url: '/services/nurses',
            //       async: false
        });
        reqNurse.done(function (nurses) {
            for (i = 0; i < nurses.length; i++) {
                var nurse = nurses[i]
                $('#nurseList').append(Mustache.to_html($('#nurse-template').html(), nurse));// update list nurse
                $('#nurseId').append(Mustache.to_html($('#selectNurse-template').html(), nurse)); //populate kids nurse list
            };
        });



        // Get Kid data
        var reqKid = $.ajax({
            type: 'GET',
            contentType: 'application/json',
            url: '/services/children/account/' + accountId,
            //       async: false
        });
        reqKid.done(function (children) {
            console.log("populate kids list");
            for (i = 0; i < children.length; i++) {
                var child = children[i];
                //var nurse = child.nurse;
                //var accountUser = child.accountUser;
               // $('#whiteAccessList').append(Mustache.to_html($('#whiteAccesUser-template').html(), accountUser));

                $('#kidList').append(Mustache.to_html($('#kid-template').html(), child));

               // $('#nurseList').append(Mustache.to_html($('#nurse-template').html(), nurse));
            }
        });
    });

    /** PAGE NAVIGATION **/
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


    /** PAGE ACTIONS **/
    $('#registerUser').click(function (e) {
        console.log("[START] registerUser " + $('#userFirstName').val());
        var mUser = {
            firstName: $('#userFirstName').val(),
            lastName: $('#userLastName').val(),
            email: $('#userEmail').val(),
            phoneNumber: $('#userPhoneNumber').val(),
            password: 'toto',
            type: $('#userType').val(),
            accountId: accountId,

        };

        var data = $.toJSON(mUser);
        console.log("create mUser object " + data)
        var req = $.ajax({
            type: 'POST',
            contentType: 'application/json',
            url: '/services/users',
            dataType: "json",
            data: data,
        });

        req.done(function (user) {
            console.log("User created "+user);
            $('#whiteAccessList').append(Mustache.to_html($('#whiteAccesUser-template').html(), user));
        });

        console.log("[END] register user");
    })

    $('#addNurse').click(function (e) {
        var mNurse = {
            firstName: $('#nurseFirstName').val(),
            lastName: $('#nurseLastName').val(),
            phoneNumber: $('#nursePhoneNumber').val()
        };
        var data = $.toJSON(mNurse);
        var req = $.ajax({
            type: 'POST',
            contentType: 'application/json',
            url: '/services/nurses',
            dataType: "json",
            data: data,
            // async: false
        });
        req.done(function (nurse) {
            $('#nurseList').append(Mustache.to_html($('#nurse-template').html(), nurse));
            $('#nurseId').append(Mustache.to_html($('#selectNurse-template').html(), nurse));
        });
    })

    $('#addKid').click(function (e) {
        var mKid = {
            firstName : $('#kidFirstName').val(),
            lastName : $('#kidLastName').val(),
            birthday  : $('#kidBirthday').val(),
            nurseId   : $('#nurseId').val(),
            accountId : accountId
        };
        var data = $.toJSON(mKid);
        var reqKid = $.ajax({
            type: 'POST',
            contentType: 'application/json',
            url: '/services/children',
            dataType: "json",
            data: data,
            //async: false
        });
        reqKid.done(function (kid) {
            $('#kidList').append(Mustache.to_html($('#kid-template').html(), kid));
        });
    })

})(jQuery);