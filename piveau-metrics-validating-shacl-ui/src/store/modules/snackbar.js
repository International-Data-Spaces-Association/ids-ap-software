export default {
  namespaced: true,

  state: {
    text: '',
    color: '',
    timeout: '',
  },

  mutations: {
    SHOW_MESSAGE(state, payload) {
      state.text = payload.text;
      state.color = payload.color;
      state.timeout = payload.timeout;
    },
  },

  actions: {
    showSnackbar({ commit }, { text = '', color = 'error', timeout = -1 }) {
      commit('SHOW_MESSAGE', { text, color, timeout });
    },
    showError({ commit }, text) {
      const payload = {
        text,
        color: 'error',
        timeout: -1,
      };
      commit('SHOW_MESSAGE', payload);
    },
  },
};
