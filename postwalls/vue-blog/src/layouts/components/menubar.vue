<template>
  <div v-if="device === 'desktop'">
    <div
      class="logo-container"
      :style="{
        width: isCollapse ? '64px' : '256px',
      }"
    >
      <a href="/">
        <i class="iconfont icon-logo-vue"></i>
      </a>
      <span>POSTWALL 后台</span>
    </div>
    <el-menu router :default-active="this.$route.path" :collapse="isCollapse" unique-opened>
      <template v-for="(menu, index) in menus" :key="index">
        <el-menu-item v-if="menu.children == null || menu.children.length <= 1" :index="menu.children[0].path">
          <i :class="menu.children[0].meta.icon"></i>
          <span>{{menu.children[0].meta.title}}</span>
        </el-menu-item>
        <el-sub-menu v-else :index="menu.path">
          <template #title>
            <i :class="menu.meta.icon"></i>
            <span>{{menu.meta.title}}</span>
          </template>
          <template v-for="(children, index) in menu.children" :key="index">
            <el-menu-item :index="children.path">
              <i :class="children.meta.icon"></i>
              <span>{{ children.meta.title }}</span>
            </el-menu-item>
          </template>
        </el-sub-menu>
      </template>
    </el-menu>
  </div>
  <div v-else>
    <el-drawer v-model="layout" direction="ltr" :with-header="false" @close="closeDrawer">
      <div class="logo-container">
        <a href="/">
          <i class="iconfont icon-logo-vue"></i>
        </a>
        <span>后台管理系统</span>
      </div>
      <el-menu router :default-active="this.$route.path">
        <template v-for="(menu, index) in menus" :key="index">
          <el-menu-item v-if="menu.children == null || menu.children.length <= 1" :index="menu.children[0].path">
            <i :class="menu.children[0].meta.icon"></i>
            <span>{{menu.children[0].meta.title}}</span>
          </el-menu-item>
          <el-sub-menu v-else :index="menu.path">
            <template #title>
              <i :class="menu.meta.icon"></i>
              <span>{{menu.meta.title}}</span>
            </template>
            <template v-for="(children, index) in menu.children" :key="index">
              <el-menu-item :index="children.path">
                <i :class="children.meta.icon"></i>
                <span>{{ children.meta.title }}</span>
              </el-menu-item>
            </template>
          </el-sub-menu>
        </template>
      </el-menu>
    </el-drawer>
  </div>
</template>

<script>
import { mapState } from "vuex";
export default {
  name: "menubar",
  computed: {
    ...mapState({
      isCollapse: (state) => state.settings.isCollapse,
      layout: (state) => state.settings.layout,
      device: (state)  => state.settings.device,
      menus: (state) => state.user.menus,
    }),
  },
  mounted() {
    const isMobile = this.handleIsMobile();
    if (isMobile) {
      this.$store.dispatch("toggleDevice", "mobile");
    } else {
      this.$store.dispatch("toggleDevice", "desktop");
    }
  },
  beforeMount() {
    window.addEventListener("resize", this.handleResize);
  },
  unmounted() {
    window.removeEventListener("resize", this.handleResize);
  },
  methods: {
    handleIsMobile() {
      return document.body.getBoundingClientRect().width - 1 < 992;
    },
    handleResize() {
      const isMobile = this.handleIsMobile();
      if (isMobile) {
        this.$store.dispatch("toggleDevice", "mobile");
      } else {
        this.$store.dispatch("toggleDevice", "desktop");
      }
    },
    closeDrawer() {
      this.$store.dispatch("changeLayout");
    }
  },
};
</script>

<style scoped>
.logo-container {
  position: relative;
  overflow: hidden;
  background: #282c34;
  height: 75px;
  line-height: 75px;
  text-align: center;
}
.logo-container a {
  text-decoration: none;
  color: #fff;
}
.logo-container a i {
  font-size: 38px;
  padding-right: 3px;
}
.logo-container a i:hover {
  opacity: 0.8;
}
.logo-container span {
  font-size: 24px;
  color: #fff;
  padding: 5px 0;
  vertical-align: top;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  transition: all 0.5s;
}
.el-menu-item i,
.el-sub-menu i {
  padding-right: 5px;
  font-size: 16px;
}
.el-menu {
  background-color: rgba(0 ,0 ,0 ,0) !important;
  border-right: none !important;
}
.el-menu.el-menu--vertical {
  width: 256px;
}
.el-menu.el-menu--vertical.el-menu--collapse {
  width: 64px;
}
.el-sub-menu__title, .el-menu-item {
  color: rgba(255, 255, 255, 0.95) !important;
}
.el-menu-item {
  background-color: #282c34;
  border-radius: 5px !important;
  margin: 7px !important;
}
.el-menu-item.is-active{
  background-color: #3f9ce7 !important;
  border-radius: 5px !important;
}
.el-menu-item:hover {
  background-color: #3f9ce7 !important;
  border-radius: 5px !important;
}
.el-sub-menu {
  margin: 7px !important;
}
.el-sub-menu__title:hover {
  background-color: #20232A !important;
  border-radius: 5px !important;
}
.el-menu--vertical.el-menu--popup-container {
  background-color: #282c34;
  border-radius: 5px !important;
}
</style>
