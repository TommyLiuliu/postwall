<template>
    <div class="main-container">
        <el-container>
            <el-aside width="200px">
                <span class="nav-title">Post Wall</span>
                <el-menu
                        :default-active="this.$route.path"
                        class="el-menu-vertical-demo"
                        router
                >
                    <el-menu-item index="/main">
                        <span>首页</span>
                    </el-menu-item>
                    <el-menu-item index="/category">
                        <span>分类</span>
                    </el-menu-item>
                    <el-menu-item index="/search">
                        <span>搜索</span>
                    </el-menu-item>
                    <div v-if="Object.keys(userInfo).length != 0">
                        <el-menu-item index="/myArticle">
                            <span>我的文章</span>
                        </el-menu-item>
                        <el-menu-item index="/person">
                            <span>个人资料</span>
                        </el-menu-item>
                        <el-menu-item index="/admin" v-if="Object.keys(userInfo).length != 0 && userInfo.role != 'user'">
                            <span>后台管理</span>
                        </el-menu-item>
                    </div>
                </el-menu>
            </el-aside>
            <el-main>
                <el-row justify="center">
                    <el-col :span="10">
                        <router-view />
                    </el-col>
                    <el-col :span="4" style="margin: 50px 20px">
                        <el-card class="userInfo">
                            <template #header>
                                <div class="card-header" v-if="Object.keys(userInfo).length==0">
                                    <el-avatar :size="50" src="https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png" />
                                    <span class="no-login">未登录</span>
                                    <el-button type="primary" class="button" text @click="$router.push('/login')">登 录</el-button>
                                </div>
                                <div class="card-header" v-else>
                                    <el-avatar :size="50" :src="userInfo.avatar" />
                                    <span class="username">{{userInfo.nickname}}</span>
                                    <el-button type="primary" class="button" text @click="logout">注销</el-button>
                                </div>
                            </template>
                            <a href="javascript: void(0)"
                               v-for="(tag, index) in tags"
                               :key="index">
                                <el-tag effect="dark" :style="'background:' + this.randomColor()" @click="$router.push('/tagList/' + tag.name)">
                                    {{tag.name}}
                                </el-tag>
                            </a>
                        </el-card>
                    </el-col>
                </el-row>
            </el-main>
        </el-container>
    </div>
</template>

<script>
    import { getTagList } from "@/api/user/tag"
    import { mapGetters } from 'vuex'
    import { removeAccessToken } from "@/utils/accessToken"
    export default {
        name: "index",
        computed: {
            ...mapGetters({
                userInfo: 'userInfo',
            })
        },
        data() {
            return {
                tags: []
            }
        },
        created() {
          this.getTagList()
        },
        methods: {
            getTagList() {
                getTagList().then((res) => {
                    if (res.code == 200) {
                        this.tags = res.data
                    }
                })
            },
            randomColor() {
                let colorArr = [
                    "#1ABC9C",
                    "#2ECC71",
                    "#3498DB",
                    "#9B59B6",
                    "#34495E",
                    "#F1C40F",
                    "#E67E22",
                    "#E74C3C",
                    "#000",
                ];
                let index = Math.floor(Math.random() * colorArr.length);
                return colorArr[index];
            },
            logout() {
                this.$confirm("你确定要退出登录？", "提示", {
                    confirmButtonText: "退出",
                    cancelButtonText: "取消",
                    type: "warning",
                }).then(() => {
                    removeAccessToken()
                    this.$store.dispatch("setUserInfo", {});
                    this.$router.push("/")
                })
            }
        }
    }
</script>

<style scoped>
.main-container {
    height: 100vh;
    background-color: #FAFAFA;
}
.el-container {
    height: 100%;
}
.el-container .el-aside {
    background-color: white;
    box-shadow: 0 1px 4px rgb(0 21 41 / 8%);
}
.nav-title {
    font-size: 35px;
    display: block;
    margin: 20px;
    font-family: fantasy;
    text-align: center;
}
.el-menu.el-menu--vertical {
    width: 200px;
}
.el-menu {
    border-right: none !important;
}
.el-menu-item.is-active{
    border-radius: 5px !important;
    color: #000000;
    font-weight: bold;
    font-size: 14px;
}
.el-menu-item:hover {
    background-color: rgba(0, 0, 0, .1) !important;
}

.no-login {
    vertical-align: top;
    display: inline-block;
    height: 50px;
    width: 80px;
    line-height: 50px;
    font-size: 18px;
    font-weight: bold;
}
.username {
    display: inline-block;
    height: 50px;
    width: 80px;
    font-size: 14px;
    font-weight: bold;
    text-align: center;
    line-height: 50px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
}

.login {
    vertical-align: top;
    margin-top: 10px;
}

.card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
}
.el-tag {
    margin: 0.65rem;
}
    .userInfo {
        position: fixed;
        width: 275px;
    }
</style>
