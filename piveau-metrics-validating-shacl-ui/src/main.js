import Vue from 'vue';
import VueMeta from 'vue-meta';
import router from './router';
import App from './App.vue';
import store from './store';
import vuetify from './plugins/vuetify';
import RuntimeConfiguration from './plugins/runtimeconfig';
import CorsproxyService from './plugins/corsproxy-service';

// Must be loaded for linting to work
window.jsyaml = require('js-yaml');
window.jsonlint = require('jsonlint-mod');

Vue.config.productionTip = false;

Vue.use(RuntimeConfiguration, { baseConfig: process.env, debug: process.env.NODE_ENV === 'development' });
Vue.use(CorsproxyService, Vue.prototype.$env.VUE_APP_CORSPROXY_API_URL);

Vue.use(VueMeta);

new Vue({
  router,
  store,
  vuetify,
  render: (h) => h(App),
}).$mount('#app');
