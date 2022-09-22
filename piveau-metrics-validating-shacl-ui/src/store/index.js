import Vue from 'vue';
import Vuex from 'vuex';

import violations from './modules/violations';
import snackbar from './modules/snackbar';

Vue.use(Vuex);

const store = new Vuex.Store({
  modules: {
    violations,
    snackbar,
  },
});

export default store;
