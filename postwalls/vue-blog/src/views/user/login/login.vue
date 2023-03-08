<template>
<div class="login-container">
    <el-row justify="center">
        <div class="logo">
            <a href="/">
                <h1>
                    Post Wall
                    <span style="font-size: 26px;color: #79BBFF">登录</span>
                </h1>
            </a>
            <span>联系你我，分享生活</span>
        </div>
        <div class="login">
            <el-form ref="login" :model="loginForm" :rules="loginRules">
                <el-form-item prop="username">
                    <el-input type="text" size="large" v-model="loginForm.username" placeholder="用户名、邮箱或手机号" />
                </el-form-item>
                <el-form-item prop="password">
                    <el-input type="password" size="large" show-password v-model="loginForm.password" placeholder="密码"/>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="login" style="width: 100%">
                        <span style="font-size: 16px">登 录</span>
                    </el-button>
                </el-form-item>
                <el-form-item style="align-items: center;flex-direction: column;border-bottom: 1px solid #A7AAAF">
                    <a href="javascript: void(0)" style="color: #3f9ce7;">忘记密码</a>
                </el-form-item>
                <el-form-item style="align-items: center;flex-direction: column;">
                    <el-button type="success" @click="$router.push('/register')">
                        新建账号
                    </el-button>
                </el-form-item>
            </el-form>
        </div>
    </el-row>
</div>
</template>

<script>
    export default {
        name: "login",
        data() {
            return {
                loginForm: {
                    username: '',
                    password: ''
                },
                loginRules: {
                    username: [
                        {
                            required: true,
                            trigger: "blur",
                            message: "请输入用户名",
                        },
                    ],
                    password: [
                        {
                            required: true,
                            trigger: "blur",
                            message: "请输入密码",
                        },
                    ],
                }
            }
        },
        methods: {
            login() {
                this.$refs.login.validate((valid) => {
                    if (valid) {
                        this.$store.dispatch("login", this.loginForm).then( (res) => {
                          if (res) {
                              this.$router.push("/")
                          }
                        })
                    }
                });
            }
        }
    }
</script>

<style scoped>
.login-container {
    height: 100vh;
}
.login-container .el-row {
    display: flex;
    flex-wrap: wrap;
    position: relative;
    box-sizing: border-box;
    flex-direction: row;
    align-content: center;
    align-items: center;
    height: 100%;
}
.logo {
    margin-right: 300px;
    height: 300px;
}
.logo h1{
    font-size: 64px;
    color: #409EFF;
    padding: 20px 0;
}
.logo span {
    font-size: 22px;
}
.login {
    width: 280px;
    height: 250px;
    background-color: white;
    padding: 20px;
    border-radius: 10px;
    box-shadow: 2px 2px 10px rgb(0 21 41 / 8%);
}
</style>
