<template>
    <div class="person-container">
        <el-breadcrumb separator="/" style="margin-bottom: 20px">
            <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>个人资料</el-breadcrumb-item>
        </el-breadcrumb>
        <el-tabs type="border-card">
            <el-tab-pane label="个人信息">
                <div style="text-align: center">
                    <el-upload
                            v-if="userInfo.base64"
                            ref="uploadRef"
                            action="#"
                            :show-file-list="false"
                            accept="image/*"
                            :on-change="selectImage"
                            :auto-upload="false"
                    >
                        <template #trigger>
                            <el-avatar :size="120" :src="userInfo.base64"/>
                        </template>
                    </el-upload>

                    <el-upload
                            v-else
                            ref="uploadRef"
                            action="#"
                            :show-file-list="false"
                            accept="image/*"
                            :on-change="selectImage"
                            :auto-upload="false"
                    >
                        <template #trigger>
                            <el-avatar :size="120" :src="userInfo.avatar"/>
                        </template>
                    </el-upload>
                </div>
                <el-form ref="userInfoForm" :model="userInfo" :rules="userInfoRules" label-width="80px">
                    <el-form-item label="用户名">
                        <el-input disabled :value="userInfo.username"/>
                    </el-form-item>
                    <el-form-item label="昵称" prop="nickname">
                        <el-input v-model="userInfo.nickname"/>
                    </el-form-item>
                    <el-form-item label="手机">
                        <el-input v-model="userInfo.phone"/>
                    </el-form-item>
                    <el-form-item label="邮箱">
                        <el-input disabled :value="userInfo.email">
                            <template #append>
                                <el-button link @click="updateEmail">修改邮箱</el-button>
                            </template>
                        </el-input>
                    </el-form-item>
                    <el-form-item label="最近登录">
                        <el-input disabled :value="userInfo.lastTimeLogin"/>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" @click="onSubmit">保存</el-button>
                    </el-form-item>
                </el-form>
            </el-tab-pane>
            <el-tab-pane label="修改密码">
                <el-form
                        ref="passwordForm"
                        :model="passwordForm"
                        :rules="passwordRules"
                        label-width="80px"
                        style="width: 400px"
                >
                    <el-form-item label="旧密码" prop="oldPassword">
                        <el-input
                                v-model.trim="passwordForm.oldPassword"
                                type="password"
                                autocomplete="off"
                        ></el-input>
                    </el-form-item>
                    <el-form-item label="新密码" prop="newPassword">
                        <el-input
                                v-model.trim="passwordForm.newPassword"
                                type="password"
                                autocomplete="off"
                        ></el-input>
                    </el-form-item>
                    <el-form-item label="再次密码" prop="rePassword">
                        <el-input
                                v-model.trim="passwordForm.rePassword"
                                type="password"
                                autocomplete="off"
                        ></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" class="save-btn" @click="modifyPassword">
                            <i class="fa fa-paper-plane"></i>
                            修改
                        </el-button>
                    </el-form-item>
                </el-form>
            </el-tab-pane>
        </el-tabs>
        <el-dialog v-model="dialogFormVisible" width="400px" @close="close" :close-on-click-modal="false">
            <template #header>
                <div style="font-size: 16px">
                    <i class="fa fa-edit"></i>
                    <span v-text="title"></span>
                </div>
            </template>
            <el-form ref="emailForm" :model="emailForm" :rules="emailRules" label-width="80px">
                <el-form-item label="新邮箱" prop="email">
                    <el-input v-model.trim="emailForm.email" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="验证码" prop="verify">
                    <el-input type="text" v-model="emailForm.verify" placeholder="验证码">
                        <template #append>
                            <el-button text @click="sendEmailVerify">{{verify.text}}</el-button>
                        </template>
                    </el-input>
                </el-form-item>
            </el-form>
            <template #footer>
                <el-button @click="close">取 消</el-button>
                <el-button type="primary" @click="saveEmail">修改邮箱</el-button>
            </template>
        </el-dialog>
    </div>
</template>

<script>
    import { getMailVerify, updateEmail, modifyPassword, updateUserInfo } from "@/api/user"
    export default {
        name: "person",
        created() {
          this.setUserInfo()
        },
        data() {
            const validateemail = (rule, value, callback) => {
                const regEmail =
                    /^([a-zA-Z]|[0-9])(\w|-)+@[a-zA-Z0-9]+\.([a-zA-Z]{2,4})$/;
                if (
                    this.emailForm.email != "" &&
                    !regEmail.test(this.emailForm.email)
                ) {
                    callback(new Error("请输入有效的邮箱"));
                } else {
                    callback();
                }
            };
            const validatepassword = (rule, value, callback) => {
                if ("" == value) {
                    callback(new Error("密码不能为空"));
                } else if (
                    this.passwordForm.rePassword != "" &&
                    this.passwordForm.rePassword != value
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
                    this.passwordForm.newPassword != "" &&
                    this.passwordForm.newPassword != value
                ) {
                    callback(new Error("两次密码输入不一致"));
                } else {
                    callback();
                }
            };
            return {
                userInfo: {},
                userInfoRules: {
                    nickname: [
                        {
                            required: true,
                            trigger: "blur",
                            message: "昵称不能为空",
                        },
                    ],
                },
                dialogFormVisible: false,
                title: "修改邮箱",
                emailForm: {
                    email: '',
                    verify: ''
                },
                emailRules: {
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
                    verify: [
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
                passwordForm: {
                    oldPassword: "",
                    newPassword: "",
                    rePassword: "",
                },
                passwordRules: {
                    oldPassword: [
                        { required: true, trigger: "blur", message: "请输入新密码" },
                    ],
                    newPassword: [
                        { required: true, trigger: "blur", validator: validatepassword },
                    ],
                    rePassword: [
                        { required: true, trigger: "blur", validator: validaterepassword },
                    ],
                },
            }
        },
        methods: {
            setUserInfo() {
                const user = Object.assign({}, this.$store.state.user.userInfo);
                this.userInfo.avatar = user.avatar;
                this.userInfo.username = user.username;
                this.userInfo.role = user.role;
                this.userInfo.nickname = user.nickname;
                this.userInfo.email = user.email;
                this.userInfo.phone = user.phone;
                this.userInfo.lastTimeLogin = user.lastTimeLogin;
            },
            updateEmail() {
              this.dialogFormVisible = true
            },
            selectImage(file) {
                this.getBase64(file.raw).then(res=>{
                    this.userInfo.base64 = res
                })
            },
            //base64编码转换方法
            getBase64(file){
                return new Promise((resolve)=>{
                    let reader = new FileReader()	//定义方法读取文件
                    reader.readAsDataURL(file)	//开始读文件  本身是图片的二进制数据 进行base64加密形成字符串
                    reader.onload = ()=> resolve(reader.result)//成功返回对应的值 reader.result可以直接放在img标签中使用
                })
            },
            sendEmailVerify() {
                if (this.verify.status) {
                    this.verify.status = false;
                    this.$refs.emailForm
                        .validateField(["email"])
                        .then(() => {
                            getMailVerify(this.emailForm.email)
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
            },
            close() {
                this.$refs["emailForm"].resetFields();
                this.emailForm = this.$options.data().emailForm;
                this.dialogFormVisible = false;
            },
            saveEmail() {
                this.$refs["emailForm"].validate(async (valid) => {
                    if (valid) {
                        updateEmail(this.emailForm.email, this.emailForm.verify).then((res) => {
                            if (res.code == 200) {
                                this.$store.dispatch("setUserInfo", res.data);
                                this.setUserInfo();
                                this.$message({ type: "success", message: res.message });
                                this.close();
                            } else {
                                this.$message({ type: "error", message: res.message });
                            }
                        })
                    }
                })
            },
            modifyPassword() {
                this.$refs["passwordForm"].validate(async (valid) => {
                    if (valid) {
                        modifyPassword(this.passwordForm).then((res) => {
                            if (res.code == 200) {
                                this.$message({ type: "success", message: res.message });
                            } else {
                                this.$message({ type: "error", message: res.message });
                            }
                        });
                    }
                });
            },
            onSubmit() {
                this.$refs["userInfoForm"].validate(async (valid) => {
                  if (valid) {
                      updateUserInfo(this.userInfo).then((res) => {
                          if (res.code == 200) {
                              this.$store.dispatch("setUserInfo", res.data);
                              this.userInfo = {}
                              this.setUserInfo();
                              this.$message({ type: "success", message: res.message });
                          } else {
                              this.$message({ type: "error", message: res.message });
                          }
                      })
                  }
                })
            }
        }
    }
</script>

<style scoped>
.el-avatar {
    display: block;
    margin: 0 auto 20px;
}
</style>
