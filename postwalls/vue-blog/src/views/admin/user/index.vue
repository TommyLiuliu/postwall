<template>
    <div class="user-container">
        <el-row class="query-form">
            <el-col :xs="24" :sm="24" :md="24" :lg="12" :xl="12" class="left-panel">
                <el-button @click="reloadTable">
                    <i class="fa fa-refresh"></i> 刷新表格
                </el-button>
                <el-switch
                        style="margin-left: 20px"
                        v-model="searchForm.isDelete"
                        size="large"
                        active-text="已删除"
                        :active-value="1"
                        :inactive-value="0"
                        @change="search"
                />
            </el-col>
            <el-col :xs="24" :sm="24" :md="24" :lg="12" :xl="12" class="right-panel">
                <el-form :inline="true" :model="searchForm" @submit.prevent>
                    <el-form-item style="width: 200px">
                        <el-input
                                v-model.trim="searchForm.username"
                                placeholder="用户名"
                                clearable
                                @keyup.enter="search"
                        />
                    </el-form-item>
                    <el-form-item style="width: 200px">
                        <el-input
                                v-model.trim="searchForm.phone"
                                placeholder="电话"
                                clearable
                                @keyup.enter="search"
                        />
                    </el-form-item>
                    <el-form-item style="width: 200px">
                        <el-input
                                v-model.trim="searchForm.email"
                                placeholder="邮箱"
                                clearable
                                @keyup.enter="search"
                        />
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" @click="search">
                            <i class="fa fa-search"></i> 查询
                        </el-button>
                    </el-form-item>
                </el-form>
            </el-col>
        </el-row>
        <el-table
                v-loading="listLoading"
                :data="users"
                :element-loading-text="elementLoadingText"
        >
            <el-table-column show-overflow-tooltip width="60">
                <template #default="scope">
                    {{ (page.curPage - 1) * page.pageSize + scope.$index + 1 }}
                </template>
            </el-table-column>
            <el-table-column label="头像" align="center" header-align="center">
                <template #default="{ row }">
                    <el-avatar :size="50" :src="row.avatar" />
                </template>
            </el-table-column>
            <el-table-column prop="username" label="用户名"></el-table-column>
            <el-table-column prop="nickname" label="昵称"></el-table-column>
            <el-table-column prop="email" label="邮箱"></el-table-column>
            <el-table-column prop="phone" label="手机号"></el-table-column>
            <el-table-column label="角色">
                <template #default="{ row }">
                    <el-tag>{{row.role}}</el-tag>
                </template>
            </el-table-column>
            <el-table-column prop="lastTimeLogin" label="最近登录时间"></el-table-column>
            <el-table-column prop="createTime" label="创建时间"></el-table-column>
            <el-table-column label="操作" width="100" fixed="right">
                <template #default="{ row }">
                    <el-button v-if="this.searchForm.isDelete == 0" type="text" style="color: #F56C6C" @click="handleDelete(row)">删除</el-button>
                    <el-button v-if="this.searchForm.isDelete == 1" type="text" style="color: #67C23A" @click="handleRecovery(row)">恢复</el-button>
                </template>
            </el-table-column>
        </el-table>
        <el-pagination
                background
                small
                :current-page="page.curPage"
                :page-size="page.pageSize"
                :layout="layout"
                :total="page.total"
                :page-sizes="[8, 10, 20, 30, 40, 50, 100]"
                @size-change="handleChangeSize"
                @current-change="handleChangePage"
        ></el-pagination>
    </div>
</template>

<script>
    import { pageUsers, updateUserIsDelete } from "@/api/admin/user"
    export default {
        name: "index",
        data() {
            return {
                listLoading: true,
                layout: "total, sizes, prev, pager, next, jumper",
                elementLoadingText: "正在加载...",
                searchForm: {
                    username: '',
                    phone: '',
                    email: '',
                    isDelete: 0
                },
                page: {
                    curPage: 1,
                    pageSize: 10,
                    total: 0,
                },
                users: []
            }
        },
        created() {
          this.getUsers()
        },
        methods: {
            getUsers() {
                this.listLoading = true;
                this.searchForm.curPage = this.page.curPage
                this.searchForm.pageSize = this.page.pageSize
                pageUsers(this.searchForm).then((res) => {
                    if (res.code == 200) {
                        this.users = res.data.users
                        this.page.total = res.data.count
                    } else {
                        this.users = []
                        this.page.total = 0
                    }
                    this.listLoading = false;
                })
            },
            search() {
                this.page.curPage = 1
                this.getUsers();
            },
            reloadTable() {
                this.searchForm = this.$options.data().searchForm;
                this.page.curPage = 1;
                this.getUsers();
            },
            handleChangePage(number) {
                this.page.curPage = number;
                this.getUsers();
            },
            handleChangeSize(number) {
                this.page.pageSize = number;
                this.getUsers();
            },
            handleDelete(row) {
                this.$confirm("你确定要删除用户【"+row.username+"】？", "提示", {
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    type: "warning",
                    "close-on-click-modal": false,
                }).then(() => {
                    updateUserIsDelete(row.id, 1).then((res) => {
                        if (res.code == 200) {
                            this.reloadTable()
                            this.$message({ type: "success", message: res.message });
                        } else {
                            this.$message({ type: "error", message: res.message });
                        }
                    })
                })
            },
            handleRecovery(row) {
                this.$confirm("你确定要恢复用户【"+row.username+"】？", "提示", {
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    type: "warning",
                    "close-on-click-modal": false,
                }).then(() => {
                    updateUserIsDelete(row.id, 0).then((res) => {
                        if (res.code == 200) {
                            this.reloadTable()
                            this.$message({ type: "success", message: res.message });
                        } else {
                            this.$message({ type: "error", message: res.message });
                        }
                    })
                })
            }
        }
    }
</script>

<style scoped>

</style>
