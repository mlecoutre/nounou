(function ($) {


    $(document).ready(function () {
    $('#authBox').initAuthBox();
        var value = sessionStorage.getItem('apptoken');
        if (value != null && value != "") {
            var token = $.parseJSON(value);
            accountId = token.accountId;
            userId = token.userId;
        }
    });    
})(jQuery);