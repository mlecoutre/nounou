(function ($) {

    /** SECURITY PART **/
    var accountId = null;
    var userId = null;

    /** GLOBAL FUNCTIONS **/
    /*
     * Used for initialization and also for actions  in the page.
     **/
    var displayKidData = function () {
        if (accountId == null) {
            return;
        }
        var reqKid = $.ajax({
            type: 'GET',
            contentType: 'application/json',
            url: '/services/children/account/' + accountId,
        });
        reqKid.done(function (children) {
            $('#kidList').html(Mustache.to_html($('#kid-template').html(), children));
            $(".kid-template").i18n();
            //DELETE a kid
            $(".delKid").click(function () {
                var kidId = $(this).attr('data-target');
                console.log('Delete a kid:' + kidId);
                var reqDel = $.ajax({
                    type: 'GET',
                    contentType: 'application/json',
                    url: '/services/children/delete/' + kidId
                });
                reqDel.done(function (e) {
                    console.log("Kid deleted");
                    displayKidData();
                });
            });
            $(".editKid").click(function () {
                var kidId = $(this).attr('data-target');
                console.log('Edit a kid:' + kidId);
                var reqEdit = $.ajax({
                    type: 'GET',
                    contentType: 'application/json',
                    url: '/services/children/' + kidId
                });
                reqEdit.done(function (kid) {
                    $('#kidFirstName').val(kid.firstName);
                    $('#kidLastName').val(kid.lastName);
                    $('#kidBirthday').val(kid.birthday);
                    $('#kidPicture').val(kid.pictureUrl);
                    $('#nurseId').val(kid.firstName);
                    $('#editKidId').val(kid.childId);
                    $('#cancelKid').show();
                    $('#updateKid').show();
                    $('#addKid').hide();
                });
            });
        });
    }

    var displayNurseData = function () {
        if (accountId == null) {
            return;
        }
        //get Nurse data.
        var reqNurse = $.ajax({
            type: 'GET',
            contentType: 'application/json',
            url: '/services/nurses/account/' + accountId,
        });
        reqNurse.done(function (nurses) {
            $('#nurseList').html(Mustache.to_html($('#nurse-template').html(), nurses)); // update list nurse
            $('#nurseId').html(Mustache.to_html($('#selectNurse-template').html(), nurses)); //populate kids nurse list
            $(".nurse-template").i18n();
            //DELETE a nurse in the list
            $(".delNurse").click(function () {
                var nurseId = $(this).attr('data-target');
                console.log('Delete a nurse:' + nurseId);
                var reqDel = $.ajax({
                    type: 'GET',
                    contentType: 'application/json',
                    url: '/services/nurses/delete/' + nurseId + '/accountId/' + accountId
                });
                reqDel.done(function (e) {
                    console.log("Nurse deleted");
                    displayNurseData();
                });

            });
            $(".editNurse").click(function () {
                var nurseId = $(this).attr('data-target');
                console.log('Edit a nurse:' + nurseId);
                var reqEdit = $.ajax({
                    type: 'GET',
                    contentType: 'application/json',
                    url: '/services/nurses/' + nurseId
                });
                reqEdit.done(function (nurse) {
                    $('#nurseFirstName').val(nurse.firstName);
                    $('#nurseLastName').val(nurse.lastName);
                    $('#nursePhoneNumber').val(nurse.phoneNumber);
                    $('#editNurseId').val(nurse.nurseId);
                    $('#cancelNurse').show();
                    $('#updateNurse').show();
                    $('#addNurse').hide();
                });
            });
        });
    }

    var displayAccessListData = function () {
        if (accountId == null) {
            return;
        }
        var reqAccount = $.ajax({
            type: 'GET',
            contentType: 'application/json',
            url: '/services/users/account/' + accountId,
        });
        reqAccount.done(function (users) {
            $('#whiteAccessList').html(Mustache.to_html($('#whiteAccesUser-template').html(), users));
            $(".user-template").i18n();
            //DELETE a kid
            $(".delUser").click(function () {
                var userId = $(this).attr('data-target');
                console.log('Delete a user:' + userId);
                var reqDel = $.ajax({
                    type: 'GET',
                    contentType: 'application/json',
                    url: '/services/users/delete/' + userId
                });
                reqDel.done(function (e) {
                    console.log("User deleted");
                    displayAccessListData();
                });
            });
            $(".editUser").click(function () {
                var userId = $(this).attr('data-target');
                console.log('Edit a user:' + userId);
                var reqEdit = $.ajax({
                    type: 'GET',
                    contentType: 'application/json',
                    url: '/services/users/' + userId
                });
                reqEdit.done(function (user) {
                    $('#userFirstName').val(user.firstName);
                    $('#userLastName').val(user.lastName);
                    $('#userPhoneNumber').val(user.phoneNumber);
                    $('#userEmail').val(user.email);
                    $('#userType').val(user.type);
                    $('#editUserId').val(user.userId);
                    $('#cancelUser').show();
                    $('#updateUser').show();
                    $('#addUser').hide();
                });
            });
        });
    }

    /** PAGE INITIALIZATION **/
    $.i18n.init({
        resGetPath: '/locales/__lng__/__ns__.json',
        ns: {
            namespaces: ['registration', "commons"],
            defaultNs: 'registration',
        },
        fallbackLng: 'en'
    }).done(function () {
        $(".registration").i18n();
        $(".navbar").i18n();
    });

    $("#kidBirthday").datepicker({
        changeYear: true,
        dateFormat: 'dd/mm/yy'
    });

    $(document).ready(function () {
        $('#authBox').initAuthBox();
        var value = sessionStorage.getItem('apptoken');
        if (value != null && value != "") {
            var token = $.parseJSON(value);
            accountId = token.accountId;
            userId = token.userId;
            $('#registerTab a[href="#you"]').tab('show');
        }

        $("#formUser").validate({
            rules: {
                userPassword: "required",
                secondUserPassword: {
                    equalTo: "#userPassword"
                }
            }
        });
        $("#formNurse").validate();
        $("#formKid").validate();

        displayAccessListData();
        displayNurseData();
        displayKidData();
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
    $('#addUser').click(function (e) {
        console.log("[START] registerUser " + $('#userFirstName').val());
        var mUser = {
            firstName: $('#userFirstName').val(),
            lastName: $('#userLastName').val(),
            email: $('#userEmail').val(),
            phoneNumber: $('#userPhoneNumber').val(),
            password: $('#userPassword').val(),
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
            console.log("User created " + user);
            $('#whiteAccessList').append(Mustache.to_html($('#whiteAccesUser-template').html(), user));
            if (user.newUser) {
                $('#authBox').autologin(user);
                 //set global data
                 accountId = user.accountId;
                 userId = user.userId;
            }
            displayAccessListData();
        });
        console.log("[END] register user");
    })

    $('#addNurse').click(function (e) {
        var mNurse = {
            firstName: $('#nurseFirstName').val(),
            lastName: $('#nurseLastName').val(),
            phoneNumber: $('#nursePhoneNumber').val(),
            accountId: accountId,
        };
        var data = $.toJSON(mNurse);
        var req = $.ajax({
            type: 'POST',
            contentType: 'application/json',
            url: '/services/nurses',
            dataType: "json",
            data: data,
        });
        req.done(function (nurse) {
            console.log("Nurse registered: " + nurse.nurseId);
            displayNurseData();
        });
    })

    $('#addKid').click(function (e) {
        var mKid = {
            firstName: $('#kidFirstName').val(),
            lastName: $('#kidLastName').val(),
            birthday: $('#kidBirthday').val(),
            nurseId: $('#nurseId').val(),
            pictureUrl: $('#kidPicture').val(),
            accountId: accountId
        };
        var data = $.toJSON(mKid);
        var reqKid = $.ajax({
            type: 'POST',
            contentType: 'application/json',
            url: '/services/children',
            dataType: "json",
            data: data,
        });
        reqKid.done(function (kid) {
            displayKidData();
        });
    });

    $('#cancelKid').click(function (e) {
        $('#cancelKid').hide();
        $('#updateKid').hide();
        $('#addKid').show();
    });
    $('#cancelUser').click(function (e) {
        $('#cancelUser').hide();
        $('#updateUser').hide();
        $('#adsUser').show();
    });
    $('#cancelNurse').click(function (e) {
        $('#cancelNurse').hide();
        $('#updateNurse').hide();
        $('#addNurse').show();
    });

    $('#updateKid').click(function (e) {
        var kidId = $('#editKidId').val();
        if (kidId == null) {
            alert('no kidId');
            return;
        }
        var mKid = {
            firstName: $('#kidFirstName').val(),
            lastName: $('#kidLastName').val(),
            birthday: $('#kidBirthday').val(),
            nurseId: $('#nurseId').val(),
            pictureUrl: $('#kidPicture').val(),
            accountId: accountId
        };
        var data = $.toJSON(mKid);
        var reqKid = $.ajax({
            type: 'POST',
            contentType: 'application/json',
            url: '/services/children/' + kidId,
            dataType: "json",
            data: data,
        });
        reqKid.done(function (kid) {
            displayKidData();
            $('#cancelKid').hide();
            $('#updateKid').hide();
            $('#addKid').show();
        });
    });

      $('#updateNurse').click(function (e) {
            var nurseId = $('#editNurseId').val();
            if (nurseId == null) {
                alert('no nurseId');
                return;
            }
            var mNurse = {
                firstName: $('#nurseFirstName').val(),
                lastName: $('#nurseLastName').val(),
                phoneNumber: $('#nursePhoneNumber').val(),
                nurseId: $('#nurseId').val(),
                accountId: accountId,
            };
            var data = $.toJSON(mNurse);
            var reqNurse = $.ajax({
                type: 'POST',
                contentType: 'application/json',
                url: '/services/nurses/' + nurseId,
                dataType: "json",
                data: data,
            });
            reqNurse.done(function (nurse) {
                displayNurseData();
                $('#cancelNurse').hide();
                $('#updateNurse').hide();
                $('#addNurse').show();
            });
        });

      $('#updateUser').click(function (e) {
            var userId = $('#editUserId').val();
            if (userId == null) {
                alert('no userUd');
                return;
            }
            var mUser = {
                firstName: $('#userFirstName').val(),
                lastName: $('#userLastName').val(),
                phoneNumber: $('#userPhoneNumber').val(),
                email: $('#userEmail').val(),
                password: $('#userPassword').val(),
                userId: $('#editUserId').val(),
                type: $('#userType').val(),
                accountId: accountId,
            };
            var data = $.toJSON(mUser);
            var reqUser = $.ajax({
                type: 'POST',
                contentType: 'application/json',
                url: '/services/users/' + userId,
                dataType: "json",
                data: data,
            });
            reqUser.done(function (user) {
                displayAccessListData();
                $('#cancelUser').hide();
                $('#updateUser').hide();
                $('#addUser').show();
            });
        });
})(jQuery);