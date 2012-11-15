(function ($) {
  $(document).ready(function () {
    $.i18n.init( {
          debug:"true",
           resGetPath: '/locales/__lng__/__ns__.json',
           ns : {
                   namespaces: ['index', "commons"],
                   defaultNs: 'index'
                }
           }
       ).done(function(){
             $(".index").i18n();
             $(".navbar").i18n();
       });

        $('#authBox').initAuthBox();
        var value = sessionStorage.getItem('apptoken');
        if (value != null && value != "") {
            var token = $.parseJSON(value);
            accountId = token.accountId;
            userId = token.userId;
            $('#registerTab a[href="#you"]').tab('show');
        }
  });
})(jQuery);