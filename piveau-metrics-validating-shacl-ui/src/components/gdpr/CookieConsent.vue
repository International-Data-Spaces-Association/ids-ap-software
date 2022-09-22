// Displays a banner asking the user for consent to use cookies and trackers.
// Helps with complying privacy laws.

<template>
  <vue-cookie-accept-decline
    class="cookie-consent"
    :class="`bg-${variant}`"
    :ref="'cookie-accept-decline-banner'"
    no-cookies-until-consent
    :elementId="'cookie-accept-decline-banner'"
    :debug="false"
    :position="'top'"
    :type="'bar'"
    :disableDecline="false"
    :transitionName="'slideFromTop'"
    :showPostponeButton="true"
    @status="cookieStatus"
    @clicked-accept="cookieClickedAccept"
    @clicked-decline="cookieClickedDecline"
    @removed-cookie="cookieRemoved"
  >
    <!-- Optional -->
    <template #postponeContent>
      <span class="text-white">
        &times;
      </span>
    </template>

    <!-- Optional -->
    <template #message>
      <span class="text-white font-weight-bold">
        <!-- TODO improve and translate me -->
        We use cookies on this site to enhance your user experience
      </span>
    </template>

    <!-- Optional -->
    <template #declineContent>
      <span class="text-dark">
        No, I want to find out more
      </span>
    </template>

    <!-- Optional -->
    <template #acceptContent>
      <span class="text-dark">
        Yes, I agree
      </span>
    </template>
  </vue-cookie-accept-decline>
</template>

<script>
import VueCookieAcceptDecline from './vue-cookie-accept-decline.vue';

export default {
  name: 'CookieConsent',
  components: {
    VueCookieAcceptDecline,
  },
  props: {
    variant: {
      // Bootstrap background color theme (ex. 'primary', 'light')
      type: String,
      default: 'primary',
    },
    declineJumpToHref: {
      // If set, jumps to the specified href when the decline button is clicked
      type: String,
      default: '',
    },
    trackPageImmediately: {
      // If true, tracks a page view immediately after consent is given
      // Set it to false if you have initial page
      // tracking somewhere else to avoid duplicate tracking
      type: Boolean,
      default: false,
    },
    piwikId: {
      type: String,
      required: true,
    },
  },
  created() {
    /* eslint-disable no-underscore-dangle */
    window._paq = window._paq || [];
  },
  methods: {
    cookieClickedAccept() {
      // Authorize Matomo tracker
      window._paq.push(['rememberConsentGiven']);
      // Piwik Pro Tracker
      /* eslint-disable */
      this.initTracker();
      var new_consent = { consents:{ } };
      new_consent.consents = {analytics: { status: 1 } };
      ppms.cm.api('setComplianceSettings', new_consent, function(new_consent){console.log(new_consent)}); 
    },
    cookieClickedDecline () {
      window._paq.push(['forgetConsentGiven']);
      this.initTracker();
      if (this.declineJumpToHref) {
        window.open(this.declineJumpToHref, '_blank');
      }
      // Piwik Pro Tracker
      /* eslint-disable */
      var new_consent = {consents:{}};
      new_consent.consents = {analytics: {status: 0}};
      ppms.cm.api('setComplianceSettings', new_consent, function(new_consent){console.log(new_consent)});
    },
    cookieStatus (status) {
      if (status === 'accept') {
        this.initTracker();
      }
    },
    cookieRemoved () {
      window._paq.push(['forgetConsentGiven'])
    },
    initTracker () {
      // Get Matomo
      // window._paq = window._paq || []
      // this.tracker = window._paq
      // window._paq.push(['requireConsent'])
      // if (this.trackPageImmediately) window._paq.push(['trackPageView']);
      // (() => {
      //   const u = this.matomoUrl.slice(-1) === '/' ? this.matomoUrl : `${this.matomoUrl}/`
      //   window._paq.push(['setTrackerUrl', `${u}matomo.php`])
      //   window._paq.push(['setSiteId', '1'])
      //   const d = document
      //   const g = d.createElement('script')
      //   const s = d.getElementsByTagName('script')[0]
      //   g.type = 'text/javascript'
      //   g.async = true
      //   g.defer = true
      //   g.src = `${u}matomo.js`
      //   s.parentNode.insertBefore(g, s)
      // })(),
      /* eslint-disable */
       ((window, document, dataLayerName, id) => { 
        window[dataLayerName] = window[dataLayerName] || [], 
        window[dataLayerName].push({ start: (new Date).getTime(), event: "stg.start" }); 
        var scripts = document.getElementsByTagName('script')[0], tags = document.createElement('script');
        function stgCreateCookie(a, b, c) { var d = ""; if (c) { var e = new Date; e.setTime(e.getTime() + 24 * c * 60 * 60 * 1e3), d = "; expires=" + e.toUTCString() } document.cookie = a + "=" + b + d + "; path=/" }
        var isStgDebug = (window.location.href.match("stg_debug") || document.cookie.match("stg_debug")) && !window.location.href.match("stg_disable_debug"); stgCreateCookie("stg_debug", isStgDebug ? 1 : "", isStgDebug ? 14 : -1);
        var qP = []; dataLayerName !== "dataLayer" && qP.push("data_layer_name=" + dataLayerName), isStgDebug && qP.push("stg_debug"); var qPString = qP.length > 0 ? ("?" + qP.join("&")) : "";
        tags.async = !0, tags.src = "//opanalytics.containers.piwik.pro/" + id + ".js" + qPString, scripts.parentNode.insertBefore(tags, scripts);
        !function (a, n, i) { a[n] = a[n] || {}; for (var c = 0; c < i.length; c++)!function (i) { a[n][i] = a[n][i] || {}, a[n][i].api = a[n][i].api || function () { var a = [].slice.call(arguments, 0); "string" == typeof a[0] && window[dataLayerName].push({ event: n + "." + i + ":" + a[0], parameters: [].slice.call(arguments, 1) }) } }(i[c]) }(window, "ppms", ["tm", "cm"]);     
        })(window, document, 'dataLayer', this.piwikId);
    }
  }
}
</script>

<style scoped lang="scss">


.cookie-consent {
  background-color: #4A82FA !important;
  color: #fff;
}
</style>
