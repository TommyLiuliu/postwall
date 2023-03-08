<template>
    <div class="register-container">
        <el-row justify="center">
            <div class="logo">
                <a href="/">
                    <h1>
                        Post Wall
                        <span style="font-size: 26px;color: #79BBFF">注册</span>
                    </h1>
                </a>
                <span>联系你我，分享生活</span>
            </div>
            <div class="register">
                <el-form ref="register" :model="registerForm" :rules="registerRules">
                    <el-form-item prop="username">
                        <el-input type="text" size="large" v-model="registerForm.username" placeholder="用户名" />
                    </el-form-item>
                    <el-form-item prop="password">
                        <el-input type="password" size="large" show-password v-model="registerForm.password" placeholder="密码"/>
                    </el-form-item>
                    <el-form-item prop="repassword">
                        <el-input type="password" size="large" show-password v-model="registerForm.repassword" placeholder="再次密码"/>
                    </el-form-item>
                    <el-form-item prop="phone">
                        <el-input type="text" size="large" v-model="registerForm.phone" placeholder="手机号码"/>
                    </el-form-item>
                    <el-form-item prop="email">
                        <el-input type="text" size="large" v-model="registerForm.email" placeholder="邮箱"/>
                    </el-form-item>
                    <el-form-item prop="emailVerify">
                        <el-input type="text" size="large" v-model="registerForm.emailVerify" placeholder="验证码">
                            <template #append>
                                <el-button text @click="sendEmailVerify">{{verify.text}}</el-button>
                            </template>
                        </el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" @click="register" style="width: 100%">
                            <span style="font-size: 16px">注 册</span>
                        </el-button>
                    </el-form-item>
                    <el-form-item style="align-items: center;flex-direction: column;">
                        <a href="javascript: void(0)" style="color: #3f9ce7;" @click="$router.push('/login')">返回登陆</a>
                    </el-form-item>
                </el-form>
            </div>
        </el-row>
    </div>
</template>

<script>
    import { register, getMailVerify } from "@/api/user"
    export default {
        name: "register",
        data() {
            const validatepassword = (rule, value, callback) => {
                if ("" == value) {
                    callback(new Error("密码不能为空"));
                } else if (
                    this.registerForm.repassword != "" &&
                    this.registerForm.repassword != value
                ) {
                    callback(new Error("两次密码输入不一致"));
                } else {
                    callback();
                }
            };
            const validaterepassword = (rule, value, callback) => {
                if ("" == value) {
                    callback(new Error("再次密码不能为空"));
                } else if (
                    this.registerForm.password != "" &&
                    this.registerForm.password != value
                ) {
                    callback(new Error("两次密码输入不一致"));
                } else {
                    callback();
                }
            };
            const validateemail = (rule, value, callback) => {
                const regEmail =
                    /^([a-zA-Z]|[0-9])(\w|-)+@[a-zA-Z0-9]+\.([a-zA-Z]{2,4})$/;
                if (
                    this.registerForm.email != "" &&
                    !regEmail.test(this.registerForm.email)
                ) {
                    callback(new Error("请输入有效的邮箱"));
                } else {
                    callback();
                }
            };
            const validatePhone = (rule, value, callback) => {
                if (value === '') {
                    callback(new Error('请输入电话号码'))
                }
                if (value.length !== 11) {
                    callback(new Error('请输入正确电话号码'))
                }
                if (value.length !== 11) {
                    callback(new Error('请输入正确电话号码'))
                }
                // 验证电话号码手机号码，包含至今所有号段? ?
                var ab = /^[1][3,4,5,7,8][0-9]{9}$/
                if (ab.test(value) === false) {
                    callback(new Error('请输入正确电话号码'))
                }
                callback()
            }
            return {
                registerForm: {
                    username: '',
                    password: '',
                    repassword: '',
                    phone: '',
                    email: '',
                    emailVerify: ''
                },
                registerRules: {
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
                            validator: validatepassword,
                        },
                    ],
                    repassword: [
                        {
                            required: true,
                            trigger: "blur",
                            validator: validaterepassword,
                        },
                    ],
                    phone: [
                        {
                            required: true,
                            trigger: "blur",
                            validator: validatePhone,
                        },
                    ],
                    email: [
                        {
                            required: true,
                            trigger: "blur",
                            message: "邮箱不能为空",
                        },
                        {
                            validator: validateemail,
                            trigger: "blur",
                        },
                    ],
                    emailVerify: [
                        {
                            required: true,
                            trigger: "blur",
                            message: "请输入验证码",
                        },
                    ],
                },
                verify: {
                    text: "获取验证码",
                    status: true,
                    time: 60,
                },
            }
        },
        methods: {
            register() {
                this.$refs.register.validate((valid) => {
                  if (valid) {
                      register(this.registerForm).then( (res) => {
                          if (res.code == 200) {
                              this.$message({ type: "success", message: res.message });
                              this.$router.push("/login");
                          } else {
                              this.$message({ type: "error", message: res.message });
                          }
                      })
                  }
                })
            },
            sendEmailVerify() {
                if (this.verify.status) {
                    this.verify.status = false;
                    this.$refs.register
                        .validateField(["email"])
                        .then(() => {
                            getMailVerify(this.registerForm.email)
                                .then((res) => {
                                    if (res.code == 200) {
                                        this.verify.text = "重新获取验证码（" + this.verify.time + "s）";
                                        this.timer = setInterval(() => {
                                            this.verify.time--;
                                            this.verify.text = "重新获取验证码（" + this.verify.time + "s）";
                                            if (this.verify.time === 0) {
                                                clearInterval(this.timer);
                                                this.verify.text = "获取验证码";
                                                this.verify.status = true;
                                                this.verify.time = 60;
                                            }
                                        }, 1000);
                                    } else {
                                        this.$message({ type: "error", message: res.message });
                                        clearInterval(this.timer);
                                        this.verify.text = "获取验证码";
                                        this.verify.status = true;
                                        this.verify.time = 60;
                                    }
                                })
                        })
                        .catch((error) => {
                            this.$message({ type: "error", message: error });
                            this.verify.status = true;
                        });
                }
            }
        }
    }
</script>

<style scoped>
    .register-container {
        height: 100vh;
    }

    .register-container .el-row {
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

    .logo h1 {
        font-size: 64px;
        color: #409EFF;
        padding: 20px 0;
    }

    .logo span {
        font-size: 22px;
    }

    .register {
        width: 280px;
        height: 420px;
        background-color: white;
        padding: 20px;
        border-radius: 10px;
        box-shadow: 2px 2px 10px rgb(0 21 41 / 8%);
    }
</style>
