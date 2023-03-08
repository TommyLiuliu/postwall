<template>
  <div class="login-container">
    <el-row>
      <el-col :xs="24" :sm="24" :md="11" :lg="14" :xl="14">
        <div style="color: transparent">占位符</div>
      </el-col>
      <el-col :xs="24" :sm="24" :md="12" :lg="9" :xl="9">
        <el-form
          ref="form"
          :model="form"
          :rules="rules"
          class="login-form"
          label-position="left"
        >
          <div class="title">Hello !</div>
          <div class="title-tips">欢迎来到博客后台登陆！</div>
          <el-form-item style="margin-top: 40px" prop="username">
            <span class="svg-container svg-container-admin">
              <i class="fa fa-user"></i>
            </span>
            <el-input
              v-model.trim="form.username"
              v-focus
              placeholder="请输入用户名"
              tabindex="1"
              type="text"
            />
          </el-form-item>
          <el-form-item prop="password">
            <span class="svg-container">
              <i class="fa fa-lock"></i>
            </span>
            <el-input
              :key="passwordType"
              ref="password"
              v-model.trim="form.password"
              :type="passwordType"
              tabindex="2"
              placeholder="请输入密码"
            />
            <span
              v-if="passwordType === 'password'"
              class="show-password"
              @click="handlePassword"
            >
              <i class="fa fa-eye-slash"></i>
            </span>
            <span v-else class="show-password" @click="handlePassword">
              <i class="fa fa-eye"></i>
            </span>
          </el-form-item>
          <el-form-item prop="verify">
            <span class="svg-container svg-container-admin">
              <i class="fa fa-check-square"></i>
            </span>
            <el-input
              v-model.trim="form.verify"
              placeholder="请输入验证码"
              tabindex="3"
              type="text"
            >
              <template #append>
                <img :src="imageCaptcha" @click="changeCaptcha" />
              </template>
            </el-input>
          </el-form-item>
          <el-button
            :loading="loading"
            class="login-btn"
            type="primary"
            @click="handleLogin"
          >
            登录
          </el-button>
        </el-form>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { isPassword } from "@/utils/validate";
import { getCaptcha } from "@/api/user";

export default {
  name: "Login",
  directives: {
    focus: {
      inserted(el) {
        el.querySelector("input").focus();
      },
    },
  },
  data() {
    const validateusername = (rule, value, callback) => {
      if ("" == value) {
        callback(new Error("用户名不能为空"));
      } else {
        callback();
      }
    };
    const validatePassword = (rule, value, callback) => {
      if (!isPassword(value)) {
        callback(new Error("密码不能少于6位"));
      } else {
        callback();
      }
    };
    const validateVerify = (rule, value, callback) => {
      if ("" == value) {
        callback(new Error("验证码不能为空"));
      } else {
        callback();
      }
    };
    return {
      form: {
        username: "",
        password: "",
        verify: "",
        captchaKey: "",
      },
      rules: {
        username: [
          {
            required: true,
            trigger: "blur",
            validator: validateusername,
          },
        ],
        password: [
          {
            required: true,
            trigger: "blur",
            validator: validatePassword,
          },
        ],
        verify: [
          {
            required: true,
            trigger: "blur",
            validator: validateVerify,
          },
        ],
      },
      loading: false,
      passwordType: "password",
      redirect: undefined,
      imageCaptcha: "",
    };
  },
  watch: {
    $route: {
      handler(route) {
        this.redirect = (route.query && route.query.redirect) || "/";
      },
      immediate: true,
    },
  },
  created() {
    document.body.style.overflow = "hidden";
    this.changeCaptcha();
  },
  beforeUnmount() {
    document.body.style.overflow = "auto";
  },
  methods: {
    handlePassword() {
      this.passwordType === "password"
        ? (this.passwordType = "")
        : (this.passwordType = "password");
      this.$nextTick(() => {
        this.$refs.password.focus();
      });
    },
    handleLogin() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          this.$router.push("/");
        }
      });
    },
    changeCaptcha() {
      this.imageCaptcha = getCaptcha()
    },
  },
};
</script>

<style scoped>
.login-container {
  height: 100vh;
  background: url("~@/assets/login_images/background.jpg") center center fixed
    no-repeat;
  background-size: cover;
}

.login-container .title {
  font-size: 54px;
  font-weight: 500;
  color: #fff;
}

.login-container .title-tips {
  margin-top: 29px;
  font-size: 26px;
  font-weight: 400;
  color: #fff;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.login-container .login-btn {
  display: inherit;
  width: 220px;
  height: 60px;
  margin-top: 5px;
  border: 0;
}

.login-container .login-btn:hover {
  opacity: 0.9;
}

.login-container .login-form {
  position: relative;
  max-width: 100%;
  padding: 4.5vh;
  margin: calc(50vh - 237.5px) 5vw 5vw;
  overflow: hidden;
  background: url("~@/assets/login_images/login_form.png");
  background-size: 100% 100%;
}

.login-container .login-form .forget-password {
  width: 100%;
  margin-top: 40px;
  text-align: left;
}

.login-container .login-form .forget-password .forget-pass {
  width: 129px;
  height: 19px;
  font-size: 20px;
  font-weight: 400;
  color: rgba(92, 102, 240, 1);
}

.login-container .tips {
  margin-bottom: 10px;
  font-size: 14px;
  color: #fff;
}

.login-container .tips span:first-of-type {
  margin-right: 16px;
}

.login-container .title-container {
  position: relative;
}

.login-container .title-container .title {
  margin: 0 auto 40px auto;
  font-size: 34px;
  font-weight: bold;
  color: #3F9CE7;
  text-align: center;
}

.login-container .svg-container {
  position: absolute;
  top: 14px;
  left: 15px;
  z-index: 999;
  font-size: 18px;
  color: #d7dee3;
  cursor: pointer;
  user-select: none;
}

.login-container .show-password {
  position: absolute;
  top: 14px;
  right: 25px;
  font-size: 16px;
  color: #d7dee3;
  cursor: pointer;
  user-select: none;
}

.login-container::v-deep .el-form-item {
  padding-right: 0;
  margin: 20px 0;
  color: #454545;
  background: transparent;
  border: 1px solid transparent;
  border-radius: 2px;
}

.login-container::v-deep .el-form-item__content {
  min-height: 32px;
  line-height: 32px;
}

.login-container::v-deep .el-form-item__error {
  position: absolute;
  top: 100%;
  left: 18px;
  font-size: 12px;
  line-height: 18px;
  color: #f34d37;
}

.login-container::v-deep .el-input {
  box-sizing: border-box;
}

.login-container::v-deep .el-input .el-input__wrapper {
  padding: 0 !important;
}

.login-container::v-deep .el-input input {
  height: 58px;
  padding-left: 45px;
  font-size: 14px;
  line-height: 58px;
  color: #606266;
  background: #f6f4fc;
  border: 0;
  caret-color: #606266;
  border-radius: 4px;
}
.el-input {
  height: 58px;
}
</style>
