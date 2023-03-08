import { login, userInfo } from "@/api/user";
import { setAccessToken, getAccessToken } from "@/utils/accessToken.js"
import { ElNotification } from "element-plus";

const state = {
  accessToken: "",
  userInfo: {},
  menus: [],
};
const getters = {
  accessToken: (state) => state.accessToken,
  userInfo: (state) => state.userInfo,
  menus: (state) => state.menus,
};
const mutations = {
  setAccessToken(state, accessToken) {
    state.accessToken = accessToken;
  },
  setUserInfo(state, userInfo) {
    state.userInfo = userInfo;
  },
  setMenu(state, menus) {
    state.menus = menus;
  },
};
const actions = {
  setPermissions({ commit }, menus) {
    commit("setMenu", menus);
  },
  setUserInfo({ commit }, userInfo) {
    commit("setUserInfo", userInfo);
  },
  async login({commit}, loginForm) {
    const res = await login(loginForm);
    if (res.code == 200) {
      const token = res.data.token;
      setAccessToken(token);
      commit("setAccessToken", token);
      commit("setUserInfo", res.data.userInfo);
      const hour = new Date().getHours();
      const thisTime =
          hour < 8
              ? "早上好"
              : hour <= 11
              ? "上午好"
              : hour <= 13
                  ? "中午好"
                  : hour < 18
                      ? "下午好"
                      : "晚上好";
      const role = {
        "user": "用户",
        "admin": "管理员",
        "sysadmin": "系统管理员"
      }
      ElNotification({
        title: "尊敬的" + role[state.userInfo.role],
        message: thisTime + "！欢迎登录Post Wall",
        type: "success",
      });
      return true;
    } else {
      ElNotification({ type: "error", message: res.message });
      return false;
    }
  },
  async getUserInfo({ commit }) {
    const token = getAccessToken()
    if (token) {
      commit("setAccessToken", token);
    }
    if (!state.accessToken) {
      return false;
    }
    const res = await userInfo(state.accessToken)
    if (res.code == 200) {
      commit("setUserInfo", res.data);
      return true
    } else {
      ElNotification({
        title: '错误！',
        message: res.message,
        type: 'error',
      })
      return false;
    }
  }
};
export default {
  state,
  getters,
  mutations,
  actions,
};
