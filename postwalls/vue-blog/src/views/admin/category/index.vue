<template>
    <div class="category-container">
        <el-row class="query-form">
            <el-col :xs="24" :sm="24" :md="24" :lg="12" :xl="12" class="left-panel">
                <el-button type="primary" @click="handleAdd">
                    <i class="fa fa-edit"></i> 添加
                </el-button>
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
                                v-model.trim="searchForm.categoryName"
                                placeholder="名称"
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
                :data="categories"
                :element-loading-text="elementLoadingText"
        >
            <el-table-column show-overflow-tooltip width="60">
                <template #default="scope">
                    {{ (page.curPage - 1) * page.pageSize + scope.$index + 1 }}
                </template>
            </el-table-column>
            <el-table-column label="封面" align="center" header-align="center" width="100">
                <template #default="{ row }">
                    <el-image :src="row.coverUrl" :preview-teleported="true"
                              :preview-src-list="[row.coverUrl]"></el-image>
                </template>
            </el-table-column>
            <el-table-column prop="name" label="名称" align="center">
                <template #default="{ row }">
                    <el-check-tag checked>{{row.name}}</el-check-tag>
                </template>
            </el-table-column>
            <el-table-column prop="remark" label="描述" align="center"></el-table-column>
            <el-table-column prop="sort" label="排序" align="center"></el-table-column>
            <el-table-column prop="createUserName" label="创建人" align="center"></el-table-column>
            <el-table-column prop="createTime" label="创建时间" align="center"></el-table-column>
            <el-table-column prop="updateUserName" label="最后修改人" align="center"></el-table-column>
            <el-table-column prop="updateTime" label="最后修改时间" align="center"></el-table-column>
            <el-table-column prop="state" label="状态" fixed="right" width="100" align="center">
                <template #default="{ row }">
                    <el-popconfirm title="你确定要修改状态？" @confirm="changeState(row)">
                        <template #reference>
                            <el-switch
                                    :value="row.state"
                                    :active-value="1"
                                    :inactive-value="2"
                            ></el-switch>
                        </template>
                    </el-popconfirm>
                </template>
            </el-table-column>
            <el-table-column fixed="right" label="操作" width="150" align="center">
                <template #default="{ row }">
                    <el-button type="text" @click="handleEdit(row)">修改</el-button>
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
        <category-edit ref="edit" @reloadTable="reloadTable"></category-edit>
    </div>
</template>

<script>
    import { pageCategory, updateIsDelete, updateState } from "@/api/admin/category"
    import categoryEdit from "./components/categoryEdit";
    export default {
        name: "index",
        components: {
            categoryEdit
        },
        data() {
            return {
                listLoading: true,
                layout: "total, sizes, prev, pager, next, jumper",
                elementLoadingText: "正在加载...",
                page: {
                    curPage: 1,
                    pageSize: 10,
                    total: 0,
                },
                categories: [],
                searchForm: {
                    categoryName: "",
                    isDelete: 0,
                },
            }
        },
        created() {
          this.getCategoryList()
        },
        methods: {
            getCategoryList() {
                this.listLoading = true;
                this.searchForm.curPage = this.page.curPage
                this.searchForm.pageSize = this.page.pageSize
                pageCategory(this.searchForm).then((res) => {
                    if (res.code == 200) {
                        this.categories = res.data.categories
                        this.page.total = res.data.count
                    } else {
                        this.categories = []
                        this.page.total = 0
                    }
                    this.listLoading = false;
                })
            },
            reloadTable() {
                this.searchForm = this.$options.data().searchForm;
                this.page.curPage = 1;
                this.getCategoryList();
            },
            search() {
                this.page.curPage = 1;
                this.getCategoryList();
            },
            handleChangePage(number) {
                this.page.curPage = number;
                this.getCategoryList();
            },
            handleChangeSize(number) {
                this.page.pageSize = number;
                this.getCategoryList();
            },
            handleAdd() {
                this.$refs['edit'].showEdit()
            },
            handleEdit(row) {
                this.$refs['edit'].showEdit(row)
            },
            handleDelete(row) {
                this.$confirm("你确定要删除分类【"+row.name+"】？", "提示", {
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    type: "warning",
                    "close-on-click-modal": false,
                }).then(() => {
                    updateIsDelete(row.id, 1).then((res) => {
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
                this.$confirm("你确定要恢复分类【"+row.name+"】？", "提示", {
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    type: "warning",
                    "close-on-click-modal": false,
                }).then(() => {
                    updateIsDelete(row.id, 0).then((res) => {
                        if (res.code == 200) {
                            this.reloadTable()
                            this.$message({ type: "success", message: res.message });
                        } else {
                            this.$message({ type: "error", message: res.message });
                        }
                    })
                })
            },
            changeState(row) {
                const state = row.state == 1 ? 2 : 1;
                updateState(row.id, state).then((res) => {
                    if (res.code == 200) {
                        row.state = state
                        this.$message({ type: "success", message: res.message });
                    } else {
                        this.$message({ type: "error", message: res.message });
                    }
                })
            }
        }
    }
</script>

<style scoped>

</style>
