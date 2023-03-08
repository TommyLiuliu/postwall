<template>
  <div class="nav-bar-container">
    <el-row :gutter="15">
      <el-col :xs="4" :sm="12" :md="12" :lg="12" :xl="12">
        <div class="left-panel">
          <i :class="isCollapse? 'fa fa-indent':'fa fa-outdent'"
             :title="isCollapse ? '展开' : '收起'"
             @click="handleCollapse"
          ></i>
          <breadcrumb />
        </div>
      </el-col>
      <el-col :xs="20" :sm="12" :md="12" :lg="12" :xl="12">
        <div class="right-panel">
          <i :class="fullscreen" @click="screen" @change="screenChange"></i>
          <i class="fa fa-repeat" @click="refresh($event)"></i>
          <avatar />
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import avatar from "./avatar";
import breadcrumb from "./breadcrumb";
import screenfull from 'screenfull'
import { mapActions, mapState } from 'vuex'
export default {
  name: "navbar",
  components: {
    avatar,
    breadcrumb,
  },
  computed: {
    ...mapState({
      isCollapse: (state) => state.settings.isCollapse
    })
  },
  data() {
    return {
      rotateNum: 1,
      fullscreen: 'fa fa-arrows-alt',
    }
  },
  created() {
    screenfull.on('change', this.screenChange)
  },
  methods: {
    ...mapActions({
      changeCollapse: 'changeCollapse'
    }),
    handleCollapse() {
      this.changeCollapse()
    },
    screen() {
      if (!screenfull.isEnabled) {
        console.log("浏览器不支持全屏!");
      } else {
        screenfull.toggle();
      }
    },
    screenChange() {
      if (screenfull.isFullscreen) {
        this.fullscreen = 'iconfont icon-tuichuquanping'
      } else {
        this.fullscreen = 'fa fa-arrows-alt'
      }
    },
    refresh(event) {
      event.currentTarget.style.transform = 'rotate('+ this.rotateNum * 360+'deg)';
      this.rotateNum = this.rotateNum + 1;
      this.$store.dispatch("refreshRoute");
    }
  },
};
</script>

<style scoped>
.nav-bar-container i {
  line-height: 60px;
  cursor: pointer;
  color: rgba(0, 0, 0, 0.65);
}
.nav-bar-container i:hover {
  opacity: 0.8;
}
.left-panel {
  display: flex;
  align-items: center;
  justify-items: center;
  height: 60px;
}
.right-panel {
  display: flex;
  align-items: center;
  justify-items: center;
  justify-content: flex-end;
  height: 60px;
}
.right-panel i {
  margin-right: 20px;
}
.right-panel i[class='fa fa-repeat'] {
  transition:all 0.5s;
}
</style>
