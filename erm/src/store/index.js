import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    user: (() => {
      try { return JSON.parse(localStorage.getItem('user')) } catch (e) { return null }
    })()
  },
  getters: {
    getUser(state) {
      return state.user
    }
  },
  mutations: {
    setUser(state, user) {
      state.user = user
      localStorage.setItem('user', JSON.stringify(user))
    },
    clearUser(state) {
      state.user = null
      localStorage.removeItem('user')
      localStorage.removeItem('token')
    }
  },
  actions: {},
  modules: {}
})