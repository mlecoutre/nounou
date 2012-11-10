(function ($) {
//    $(document).ajaxSend(function() {
//        // Show your spinner
//    }).ajaxComplete(function() {
//        // Hide your spinner
//    });

$("#loadingIndicator")
.bind('ajaxStart', function() {
   $(this).show();
})
.bind('ajaxStop', function() {
   $(this).hide();
});
})(jQuery);