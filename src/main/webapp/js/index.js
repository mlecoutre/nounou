(function ($) {
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
})(jQuery);