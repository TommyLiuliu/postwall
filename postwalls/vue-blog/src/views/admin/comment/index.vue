<template>
    <div class="comment-container">
        <el-row class="query-form">
            <el-col :xs="24" :sm="24" :md="24" :lg="12" :xl="12" class="left-panel">
                <el-button @click="reloadTable">
                    <i class="fa fa-refresh"></i> 刷新表格
                </el-button>
            </el-col>
            <el-col :xs="24" :sm="24" :md="24" :lg="12" :xl="12" class="right-panel">
                <el-form :inline="true" :model="searchForm" @submit.prevent>
                    <el-form-item style="width: 100px">
                        <el-select
                                v-model="searchForm.isDelete"
                                placeholder="状态"
                                @change="getSearch"
                        >
                            <el-option label="全部" value=""></el-option>
                            <el-option label="发布中" :value="0"></el-option>
                            <el-option label="已删除" :value="1"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" @click="getSearch">
                            <i class="fa fa-search"></i>查询
                        </el-button>
                    </el-form-item>
                </el-form>
            </el-col>
        </el-row>
        <el-table
                v-loading="listLoading"
                :data="comments"
                :element-loading-text="elementLoadingText"
        >
            <el-table-column show-overflow-tooltip label="序号" width="60">
                <template #default="scope">
                    {{ (page.curPage - 1) * page.pageSize + scope.$index + 1 }}
                </template>
            </el-table-column>
            <el-table-column
                    label="头像"
                    width="60"
                    align="center"
                    header-align="center"
            >
                <template #default="{ row }">
                    <el-image :src="row.userInfo.avatar"></el-image>
                </template>
            </el-table-column>
            <el-table-column
                    prop="userInfo.nickname"
                    label="用户"
                    align="center"
                    header-align="center"
            ></el-table-column>
            <el-table-column prop="articleDTO.title" label="文章标题"></el-table-column>
            <el-table-column label="内容" width="500" prop="content"></el-table-column>
            <el-table-column
                    prop="commentTime"
                    label="评论时间"
                    width="160"
            ></el-table-column>
            <el-table-column prop="state" label="是否删除" fixed="right" width="80">
                <template #default="{ row }">
                    <el-tag effect="dark" type="danger" v-if="row.isDelete == 1"
                    >已删除</el-tag
                    >
                    <el-tag effect="dark" type="success" v-else>发布中</el-tag>
                </template>
            </el-table-column>
            <el-table-column label="操作" fixed="right" width="100">
                <template #default="{ row }">
                    <el-button v-if="row.isDelete == 0" type="text" @click="$router.push('/articleView/' + row.articleDTO.link)">
                        查看
                    </el-button>
                    <el-button v-if="row.isDelete == 0" type="text" @click="handleDelete(row)">
                        删除
                    </el-button>
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
        <commentEdit ref="edit" @reloadTable="reloadTable" />
    </div>
</template>

<script>
    import { pageArticleComment, updateCommentIsDelete } from "@/api/admin/comment"
    export default {
        name: "index",
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
                comments: [],
                searchForm: {
                    title: "",
                    nickname: "",
                    ipAddress: "",
                    isDelete: 0,
                },
            };
        },
        created() {
            this.pageArticleComment();
        },
        methods: {
            pageArticleComment() {
                this.listLoading = true;
                this.searchForm.curPage = this.page.curPage;
                this.searchForm.pageSize = this.page.pageSize;
                pageArticleComment(this.searchForm)
                    .then((res) => {
                        if (res.code == 200) {
                            this.comments = res.data.comments;
                            this.page.total = res.data.count;
                        } else {
                            this.comments = [];
                            this.page.total = 0;
                        }
                        this.listLoading = false;
                    })
            },
            getSearch() {
                this.page.curPage = 1;
                this.pageArticleComment();
            },
            handleChangePage(number) {
                this.page.curPage = number;
                this.pageArticleComment();
            },
            handleChangeSize(number) {
                this.page.pageSize = number;
                this.pageArticleComment();
            },
            reloadTable() {
                this.searchForm = this.$options.data().searchForm;
                this.page.curPage = 1;
                this.pageArticleComment();
            },
            handleDelete(row) {
                this.$confirm("你确定要删除评论【"+row.content+"】？", "提示", {
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    type: "warning",
                    "close-on-click-modal": false,
                }).then(() => {
                    updateCommentIsDelete(row.id, 1).then((res) => {
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
                this.$confirm("你确定要恢复评论【"+row.content+"】？", "提示", {
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    type: "warning",
                    "close-on-click-modal": false,
                }).then(() => {
                    updateCommentIsDelete(row.id, 0).then((res) => {
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
