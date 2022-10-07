import Vue from 'vue';
import axios from 'axios';

function getShaclViolations(body, type, param = 'idsapbasic') {
  const shaclApi = axios.create({
    baseURL: Vue.prototype.$env.VUE_APP_SHACL_API_URL || process.env.VUE_APP_SHACL_API_URL || 'http://localhost:8000',
    headers: {
      Accept: '*/*',
      'Content-Type': `${type}`,
    },
  });
  return shaclApi.post('validation/report', body, { params: { shapeModel: param } });
}

function getShaclViolationsByURL(shaclUrl, type) {
  return getShaclViolations(shaclUrl, type);
}

function getShaclViolationsBySchema(schema, type, param) {
  return getShaclViolations(schema, type, param);
}

export default {
  namespaced: true,
  state: {
    violations: [],
    requestError: null,
    violationsSuccess: false,
    isLoading: false,
  },

  getters: {
    getViolations: (state) => state.violations,
    getViolationsSuccess: (state) => state.violationsSuccess,
    getLoading: (state) => state.isLoading,
    getRequestError: (state) => state.requestError,
  },

  actions: {
    async getViolations({ commit }, {
      body, bySchema = true, type, param,
    }) {
      // Reset violations
      commit('SET_VIOLATIONS', []);
      commit('SET_VIOLATIONS_SUCCESS', true);
      commit('SET_REQUEST_ERROR', null);
      commit('SET_LOADING', true);
      try {
        // Get violations
        const response = bySchema
          ? await getShaclViolationsBySchema(body, type, param)
          : await getShaclViolationsByURL(body, type);

        // Artifically delay response to test loading behavior
        const apiDelay = Vue.prototype.$env.VUE_APP_DEV_API_REQUEST_DELAY
          || process.env.VUE_APP_DEV_API_REQUEST_DELAY
          || 0;
        if (process.env.NODE_ENV !== 'production' && apiDelay) {
          // Make sure this line is never executed in production
          await new Promise((resolve) => setTimeout(resolve, apiDelay));
        }

        if (response.data['@graph']) commit('SET_VIOLATIONS', response.data['@graph']);
        commit('SET_VIOLATIONS_SUCCESS', false);
      } catch (ex) {
        commit('SET_REQUEST_ERROR', ex);
      }
      commit('SET_LOADING', false);
    },

    async getViolationsBySchema({ dispatch }, { schema, type, param }) {
      dispatch('getViolations', {
        body: schema, bySchema: true, type, param,
      });
    },
  },

  mutations: {
    SET_VIOLATIONS(state, payload) {
      state.violations = payload;
    },
    SET_VIOLATIONS_SUCCESS(state, payload) {
      state.violationsSuccess = !!payload;
    },
    SET_LOADING(state, payload) {
      state.isLoading = !!payload;
    },
    SET_REQUEST_ERROR(state, payload) {
      state.requestError = payload;
    },
  },
};
