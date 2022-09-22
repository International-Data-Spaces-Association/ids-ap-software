<template>
  <v-app id="app">
    <div
      id="block-edp3-branding"
      class="edp-header__branding"
    >
      <cookie-consent
        variant="medium-blue"
        :decline-jump-to-href="`${link}/legal-notice`"
        :piwik-id="piwikId"
      />
    <h1>IDS Connector Validator</h1>
    </div>
    <v-main>
      <Main />
    </v-main>
  </v-app>
</template>

<script>
import CookieConsent from '@/components/gdpr/CookieConsent.vue';
import Main from './components/Main.vue';

import { config as CONFIG } from '../config/config';

export default {
  name: 'App',
  components: {
    Main,
    CookieConsent,
  },
  data() {
    return {
      title: CONFIG.title,
      link: CONFIG.link,
      piwikId: this.$env.VUE_APP_PIWIK_ID,
    };
  },
};
</script>

<style lang="scss">
/***********************************
  SITE BRANDING
***********************************/

#block-edp3-branding {
  padding: 20px 20px 0px 20px;
  @media only screen and (max-width: 599px) {
    display: flex;
    align-items: center;
  }
}

#block-edp3-branding a {
  text-decoration: none;
  display: inline-block;
  overflow: hidden;
}

#block-edp3-branding a img {
  height: 45px;
  margin-right: 10px;

  @media only screen and (max-width: 599px) {
    height: 35px;
  }
}

#block-edp3-branding .site-branding__text {
  display: inline-block;
}

#block-edp3-branding div a {
  line-height: 45px !important;
  font-size: 15px !important;
  color: #0F62AE !important;
  padding-bottom: 5px !important;
  padding-left: 5px !important;
  display: inline-block !important;
  text-transform: uppercase !important;

  @media only screen and (max-width: 599px) {
    font-size: 10px !important;
    overflow: unset;
    line-height: 20px !important;
  }

  @media only screen and (max-width: 400px) {
    width: 140px;
    font-size: 9px !important;
  }
}

#block-edp3-branding div a h1 {
  font-weight: 300 !important;
}

#block-edp3-branding div a strong {
  font-weight: bold;
}

#block-edp3-branding .edp-logo {
  width: 417px;
  height: auto;

  &--three-rows {
    height: 50px;
    width: auto;
  }
}
</style>
