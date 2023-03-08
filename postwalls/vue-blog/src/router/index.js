import { createRouter, createWebHistory } from "vue-router";
import store from '@/store'
import { setRouter } from '@/utils/handleRoutes'

const constantRoutes = [
  {
    path: '/',
    name: 'home',
    redirect: '/main',
    component: () => import('@/views/user/main/index'),
    children:[
      {
        path: '/main',
        name: 'main',
        component: () => import('@/views/user/main/main'),
      },
      {
        path: '/category',
        name: 'category',
        component: () => import('@/views/user/main/category'),
      },
      {
        path: '/search',
        name: 'search',
        component: () => import('@/views/user/main/search'),
      },
      {
        path: '/categoryList/:id/:name',
        name: 'categoryList',
        component: () => import('@/views/user/main/categoryList'),
      },
      {
        path: '/tagList/:name',
        name: 'tagList',
        component: () => import('@/views/user/main/tagList'),
      },
      {
        path: '/myArticle',
        name: 'myArticle',
        component: () => import('@/views/user/main/my-article'),
      },
      {
        path: '/person',
        name: 'person',
        component: () => import('@/views/user/main/person'),
      },
      {
        path: '/articleView/:year/:month/:day/:title',
        name: 'articleView',
        component: () => import('@/views/user/main/article-view'),
      },
    ]
  },
  {
    path: "/login",
    name: 'userLogin',
    component: () => import('@/views/user/login/login'),
  },
  {
    path: "/register",
    name: 'userRegister',
    component: () => import('@/views/user/login/register'),
  },
  {
    path: "/401",
    name: 'page401',
    component: () => import('@/views/401'),
  },
  {
    path: "/404",
    name: 'page404',
    component: () => import('@/views/404'),
  },
];

export const layoutsRouters = { component: () => import('@/layouts') };

export const asyncRoutes = [
  {
    name: 'adminBlog',
    component: () => import('@/views/admin/blog/index'),
  },
  {
    name: 'adminCategory',
    component: () => import('@/views/admin/category/index'),
  },
  {
    name: 'adminTag',
    component: () => import('@/views/admin/tag/index'),
  },
  {
    name: 'adminComment',
    component: () => import('@/views/admin/comment/index'),
  },
  {
    name: 'adminUser',
    component: () => import('@/views/admin/user/index'),
  },
]


const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes: constantRoutes,
});

router.beforeResolve(async (to, from, next) => {
  const hasPermissions = store.getters.menus && store.getters.menus.length > 0
  if (hasPermissions) {
    next()
  } else {
    await store.dispatch("getUserInfo")
    setRouter()
    next({ ...to, replace: true })
  }
});

export default router;
