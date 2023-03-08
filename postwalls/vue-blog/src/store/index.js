import { createStore } from "vuex";
import settings from "./modules/settings";
import user from "./modules/user";
export default createStore({
  modules: {
    settings,
    user
  },
});
