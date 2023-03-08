const state = {
  isCollapse: false,
  layout: true,
  device: 'desktop',
  refreshRoute: true,
};
const getters = {
  isCollapse: (state) => state.isCollapse,
  layout: (state) => state.layout,
  device: (state) => state.device,
  refreshRoute: (state) => state.refreshRoute,
};
const mutations = {
  changeCollapse: (state) => {
    state.isCollapse = !state.isCollapse;
    state.layout = !state.isCollapse
  },
  changeLayout: (state) => {
    state.layout = !state.layout
    state.isCollapse = !state.layout
  },
  toggleDevice: (state, device) => {
    state.device = device
  },
  refreshRoute: (state) => {
    state.refreshRoute = false
    setTimeout(function () {
      state.refreshRoute = true
    }, 50)
  }
};
const actions = {
  changeCollapse({ commit }) {
    commit("changeCollapse");
  },
  changeLayout({ commit }) {
    commit('changeLayout')
  },
  toggleDevice({ commit }, device) {
    commit('toggleDevice', device)
  },
  refreshRoute({ commit }) {
    commit('refreshRoute')
  },
};
export default {
  state,
  getters,
  mutations,
  actions,
};
