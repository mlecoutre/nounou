(function ($) {

    $("#loadingIndicator").bind('ajaxStart', function() {
        $(this).show();
    }).bind('ajaxStop', function() {
        $(this).hide();
    });

    $(document).ready(function () {
        var language = window.navigator.userLanguage || window.navigator.language;
        language =  language.substring(0,2);
        if( language !=  "en"){
            console.log("Initialize validation message for "+language);
            $.getScript("../js/validation-locale/messages_"+language+".js");
        }

    });
})(jQuery);