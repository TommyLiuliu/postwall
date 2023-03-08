<template>
  <el-dropdown @command="handleCommand">
    <span class="avatar-dropdown">
      <el-avatar
        size="40"
        :src="userInfo.avatar"
        @error="true"
      >
        <img v-if="randomBoolean()" src="@/assets/avatar/avatar1.gif">
        <img v-else src="@/assets/avatar/avatar2.gif">
      </el-avatar>
      <div class="user-name">
        {{ userInfo.nickname }}
        <el-icon class="el-icon--right">
          <arrow-down />
        </el-icon>
      </div>
    </span>
    <template #dropdown>
      <el-dropdown-menu>
        <el-dropdown-item command="logout">退出登录</el-dropdown-item>
      </el-dropdown-menu>
    </template>
  </el-dropdown>
</template>

<script>
import { ArrowDown } from "@element-plus/icons-vue";
import { removeAccessToken } from "@/utils/accessToken"
import { mapGetters } from 'vuex'
export default {
  name: "avatar",
  computed: {
    ...mapGetters({
      userInfo: 'userInfo',
  })
  },
  components: {
    ArrowDown,
  },
  methods: {
    handleCommand(command) {
      switch (command) {
        case "logout":
          this.logout();
          break;
      }
    },
    logout() {
      this.$confirm("你确定要退出登录？", "提示", {
        confirmButtonText: "退出",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        removeAccessToken()
        this.$router.push("/login")
      })
    },
    randomBoolean() {
      const random = Math.floor(Math.random() * 10);
      return random % 2 == 0;
    }
  },
};
</script>

<style scoped>
.avatar-dropdown {
  display: flex;
  align-content: center;
  align-items: center;
  justify-content: center;
  justify-items: center;
  height: 50px;
  padding: 0;
}
.avatar-dropdown .user-name {
  position: relative;
  margin-left: 10px;
  cursor: pointer;
}
.avatar-dropdown .el-icon {
  font-size: 12px;
  margin: 0;
  transition: all 0.5s;
}
.el-dropdown div[aria-describedby] .avatar-dropdown .el-icon {
  transform: rotate(180deg);
}
</style>
