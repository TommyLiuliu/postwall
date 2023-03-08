<template>
  <div class="admin-layout">
    <el-container>
      <el-aside>
        <menubar />
      </el-aside>
      <el-container>
        <el-header>
          <navbar />
        </el-header>
        <div class="app-main-container">
          <el-main>
            <router-view v-if="refreshRoute" />
          </el-main>
          <el-footer>
            <span
              >Copyright
              <i class="fa fa-copyright"></i>
              2022 - 2025 liuhanchao</span
            >
          </el-footer>
        </div>
      </el-container>
    </el-container>
  </div>
</template>

<script>
import navbar from "./components/navbar";
import menubar from "./components/menubar";
import { mapState } from "vuex";
export default {
  name: "index",
  components: {
    navbar,
    menubar,
  },
  computed: {
    ...mapState({
      refreshRoute: (state) => state.settings.refreshRoute,
    }),
  },
};
</script>

<style scoped>
.admin-layout .el-container .el-aside {
  height: 100vh;
  z-index: 999;
  background-color: #282c34;
  box-shadow: 2px 0 6px rgb(0 21 41 / 35%);
  transition: width 0.5s;
  width: unset;
}
.admin-layout .el-container .el-header {
  background-color: #fff;
  box-shadow: 0 1px 4px rgb(0 21 41 / 8%);
  z-index: 99;
}
.admin-layout .el-container .el-container {
  position: relative;
  height: 100vh;
  overflow: hidden;
}
.admin-layout .el-container .el-container .app-main-container {
  overflow: auto;
}
.admin-layout .el-container .el-main {
  margin: 20px 20px 0 20px;
  background-color: #fff;
  padding: 20px 20px 40px 20px;
  border-radius: 5px;
  min-height: calc(100vh - 160px);
}
.admin-layout .el-container .el-footer {
  border-top: 1px dashed #dcdfe6;
  background-color: #fff;
  margin: 0px 20px 20px 20px;
  text-align: center;
  height: 60px;
  line-height: 60px;
  color: rgba(0, 0, 0, 0.45);
  border-bottom-left-radius: 5px;
  border-bottom-right-radius: 5px;
}
</style>
