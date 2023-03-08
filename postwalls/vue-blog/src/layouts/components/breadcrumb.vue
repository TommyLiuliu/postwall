<template>
  <el-breadcrumb separator=">">
    <el-breadcrumb-item v-for="item in breadcrumbs" :key="item.path">
      {{ item.title }}
    </el-breadcrumb-item>
  </el-breadcrumb>
</template>

<script>
export default {
  name: "breadcrumb",
  data() {
    return {
      breadcrumbs: this.getBreadcrumb(),
    };
  },
  watch: {
    $route() {
      this.breadcrumbs = this.getBreadcrumb();
    },
  },
  methods: {
    getBreadcrumb() {
      const breadcrumb = []
      this.$route.matched.forEach((item) => {
        if (item.meta.title && item.path) {
          breadcrumb.push({ path: item.path, title: item.meta.title })
        }
      })
      return breadcrumb;
    },
  },
};
</script>

<style scoped>
.el-breadcrumb {
  line-height: 60px;
  padding: 0 20px;
  position: absolute;
  pointer-events: none;
}
</style>
