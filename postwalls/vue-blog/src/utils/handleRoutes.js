import { layoutsRouters } from "@/router/index"
import { getRoleMenu } from "@/api/menu"
import router from "../router";
import { asyncRoutes } from "../router";
import store from '@/store'

/**
 * 通过api获取的菜单渲染路由
 * @param routes
 * @param permissions
 */
export function filterAsyncRoutes(routes, permissions) {
  const finallyRoutes = new Array();
  permissions.forEach((router) => {
    let r = null;
    // 判断是否顶级菜单
    if (router.name == "layout") {
      r = Object.assign({}, layoutsRouters);
    } else {
      r = Object.assign({}, routes.find((item) => item.name == router.name));
    }
    // 判断是否找到路由
    if (Object.keys(r).length == 0) {
      return true;
    }
    r.path = router.path;
    r.meta = {};
    if (router.title) {
      r.meta.title = router.title;
    }
    if (router.icon) {
      r.meta.icon = router.icon;
    }
    if (router.redirect) {
      r.redirect = router.redirect
    }
    if (router.children) {
      r.children = filterAsyncRoutes(routes, router.children);
    }
    if (r.children != null && r.children.length == 0) {
      return true;
    }
    finallyRoutes.push(r);
  });
  return finallyRoutes;
}


export function setRouter() {
  const finallyRoutes = filterAsyncRoutes(asyncRoutes, getRoleMenu());
  store.dispatch("setPermissions", finallyRoutes);
  // 添加路由
  finallyRoutes.forEach((item) => {
    router.addRoute(item)
  });
  // 添加匹配不到404页面
  router.addRoute({
    path: '/:catchAll(.*)',
    redirect: '/404',
  });
}
