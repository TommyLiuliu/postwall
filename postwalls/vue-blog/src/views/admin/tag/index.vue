<template>
    <div class="tag-container">
        <el-row class="query-form">
            <el-col :xs="24" :sm="24" :md="24" :lg="12" :xl="12" class="left-panel">
                <el-button type="primary" @click="handleAdd">
                    <i class="fa fa-edit"></i> 添加
                </el-button>
                <el-button @click="reloadTable">
                    <i class="fa fa-refresh"></i> 刷新表格
                </el-button>
            </el-col>
            <el-col :xs="24" :sm="24" :md="24" :lg="12" :xl="12" class="right-panel">
                <el-form :inline="true" :model="searchForm" @submit.prevent>
                    <el-form-item style="width: 200px">
                        <el-input
                                v-model.trim="searchForm.name"
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
                :data="tags"
                :element-loading-text="elementLoadingText"
        >
            <el-table-column show-overflow-tooltip width="60">
                <template #default="scope">
                    {{ (page.curPage - 1) * page.pageSize + scope.$index + 1 }}
                </template>
            </el-table-column>
            <el-table-column label="名称" align="center">
                <template #default="{ row }">
                    <el-tag effect="dark">{{row.name}}</el-tag>
                </template>
            </el-table-column>
            <el-table-column prop="createUserName" label="创建人"></el-table-column>
            <el-table-column prop="createTime" label="创建时间"></el-table-column>
            <el-table-column label="操作" width="100" fixed="right">
                <template #default="{ row }">
                    <el-button type="text" style="color: #F56C6C" @click="handleDelete(row)">删除</el-button>
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
        <tag-edit ref="edit" @reloadTable="reloadTable" />
    </div>
</template>

<script>
    import { pageTags, deleteTag } from "@/api/admin/tag"
    import tagEdit from "./components/tagEdit";
    export default {
        name: "index",
        components: {
            tagEdit
        },
        data() {
            return {
                listLoading: true,
                layout: "total, sizes, prev, pager, next, jumper",
                elementLoadingText: "正在加载...",
                searchForm: {
                    name: "",
                },
                tags: [],
                page: {
                    curPage: 1,
                    pageSize: 10,
                    total: 0,
                },
            };
        },
        created() {
          this.getTags()
        },
        methods: {
            async getTags() {
                this.listLoading = true;
                this.searchForm.curPage = this.page.curPage;
                this.searchForm.pageSize = this.page.pageSize;
                await pageTags(this.searchForm).then((res) => {
                    if (res.code == 200) {
                        this.tags = res.data.tags;
                        this.page.total = res.data.count;
                    } else {
                        this.tags = [];
                        this.page.total = 0;
                    }
                    this.listLoading = false;
                })
            },
            reloadTable() {
                this.searchForm = this.$options.data().searchForm;
                this.page.curPage = 1;
                this.getTags();
            },
            search() {
                this.page.curPage = 1;
                this.getTags();
            },
            handleChangePage(number) {
                this.page.curPage = number;
                this.getTags();
            },
            handleChangeSize(number) {
                this.page.pageSize = number;
                this.getTags();
            },
            handleAdd() {
                this.$refs['edit'].showEdit()
            },
            handleDelete(row) {
                this.$confirm("你确定要删除标签【"+row.name+"】？", "提示", {
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    type: "warning",
                    "close-on-click-modal": false,
                }).then(() => {
                    deleteTag(row.id).then((res) => {
                        if (res.code == 200) {
                            this.reloadTable()
                            this.$message({ type: "success", message: res.message });
                        } else {
                            this.$message({ type: "error", message: res.message });
                        }
                    })
                })
            },
        }
    }
</script>

<style scoped>

</style>
