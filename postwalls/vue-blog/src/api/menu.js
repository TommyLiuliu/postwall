export function getRoleMenu() {
  const menu = [
    {
      path: "/admin",
      name: 'layout',
      redirect: '/admin/blog',
      icon: "fa fa-pencil",
      children: [
        {
          path: '/admin/blog',
          name: 'adminBlog',
          title: '博文管理',
          icon: "fa fa-pencil",
        },
      ],
    },
    {
      path: "/admin",
      name: 'layout',
      redirect: '/admin/category',
      icon: "fa fa-pencil",
      children: [
        {
          path: '/admin/category',
          name: 'adminCategory',
          title: '分类管理',
          icon: "fa fa-archive",
        },
      ],
    },
    {
      path: "/admin",
      name: 'layout',
      redirect: '/admin/tag',
      icon: "fa fa-pencil",
      children: [
        {
          path: '/admin/tag',
          name: 'adminTag',
          title: '标签管理',
          icon: "fa fa-tags",
        },
      ],
    },
    {
      path: "/admin",
      name: 'layout',
      redirect: '/admin/comment',
      icon: "fa fa-pencil",
      children: [
        {
          path: '/admin/comment',
          name: 'adminComment',
          title: '评论管理',
          icon: "fa fa-comments",
        },
      ],
    },
    {
      path: "/admin",
      name: 'layout',
      redirect: '/admin/user',
      icon: "fa fa-pencil",
      children: [
        {
          path: '/admin/user',
          name: 'adminUser',
          title: '用户管理',
          icon: "fa fa-users",
        },
      ],
    },
  ];
  return menu;
}
