(function ($) {

    jQuery.extend(jQuery.fn, {
        checkNetworkStatus: function() {
            if (navigator.onLine) {
            // Just because the browser says we're online doesn't mean we're online. The browser lies.
            // Check to see if we are really online by making a call for a static JSON resource on
            // the originating Web site. If we can get to it, we'resourcee online. If not, assume we're
            // offline.
            $.ajaxSetup({
                async: true,
                cache: false,
                context: $("#status"),
                dataType: "json",
                error: function (req, status, ex) {
                    console.log("Error: " + ex);
                // We might not be technically "offline" if the error is not a timeout, but
                // otherwise we're getting some sort of error when we shouldn't, so we're
                // going to treat it as if we're offline.
                // Note: This might not be totally correct if the error is because the
                // manifest is ill-formed.
                this.showNetworkStatus(false);
            },
            success: function (data, status, req) {
                this.showNetworkStatus(true);
            },
            timeout: 5000,
            type: "GET",
            url: "/js/ping.js"
        });
            $.ajax();
        }
        else {
            this.showNetworkStatus(false);
        }
    },
    showNetworkStatus: function (online) {
        if (online) {
            $(this).html("<img src='/img/online.png' class='online-status' />");
        }else {
           $(this).html("<img src='/img/offline.jpeg' class='online-status' />");
       }

       console.log("Online status: " + online);
   }
})


})(jQuery);