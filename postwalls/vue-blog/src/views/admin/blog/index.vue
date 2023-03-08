<template>
    <div class="blog-container">
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
                        @change="getSearch"
                />
            </el-col>
            <el-col :xs="24" :sm="24" :md="24" :lg="12" :xl="12" class="right-panel">
                <el-form :inline="true" :model="searchForm" @submit.prevent>
                    <el-form-item style="width: 200px">
                        <el-input
                                v-model.trim="searchForm.title"
                                placeholder="标题"
                                clearable
                                @keyup.enter="getSearch"
                        />
                    </el-form-item>
                    <el-form-item style="width: 150px">
                        <el-date-picker
                                v-model="searchForm.startTime"
                                type="date"
                                placeholder="开始时间"
                        />
                    </el-form-item>
                    <el-form-item style="width: 150px">
                        <el-date-picker
                                v-model="searchForm.endTime"
                                type="date"
                                placeholder="结束时间"
                        />
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" @click="getSearch">
                            <i class="fa fa-search"></i> 查询
                        </el-button>
                    </el-form-item>
                </el-form>
            </el-col>
        </el-row>
        <el-table
                v-loading="listLoading"
                :data="articles"
                :element-loading-text="elementLoadingText"
        >
            <el-table-column label="序号" width="60" fixed="left">
                <template #default="scope">
                    {{ (page.curPage - 1) * page.pageSize + scope.$index + 1 }}
                </template>
            </el-table-column>
            <el-table-column label="封面" align="center" header-align="center">
                <template #default="{ row }">
                    <el-image :src="row.coverUrl"></el-image>
                </template>
            </el-table-column>
            <el-table-column
                    show-overflow-tooltip
                    prop="title"
                    label="标题"
                    width="300"
            ></el-table-column>
            <el-table-column prop="categoryName" label="分类"></el-table-column>
            <el-table-column width="70" label="浏览数">
                <template #default="{ row }">
                    <el-tag effect="dark" color="#E5913D" class="column-tag">
                        <span v-if="row.readNum <= 999999">{{ row.readNum }}</span>
                        <span v-else>999999+</span>
                    </el-tag>
                </template>
            </el-table-column>
            <el-table-column width="70" label="评论数">
                <template #default="{ row }">
                    <el-tag effect="dark" color="#1B8A5E" class="column-tag">
                        <span v-if="row.commentNum <= 999999">{{ row.commentNum }}</span>
                        <span v-else>999999+</span>
                    </el-tag>
                </template>
            </el-table-column>
            <el-table-column width="70" label="点赞数">
                <template #default="{ row }">
                    <el-tag effect="dark" color="#5C77D4" class="column-tag">
                        <span v-if="row.praiseNum <= 999999">{{ row.praiseNum }}</span>
                        <span v-else>999999+</span>
                    </el-tag>
                </template>
            </el-table-column>
            <el-table-column prop="publishDate" label="发布时间"></el-table-column>
            <el-table-column prop="createUserName" label="创建人"></el-table-column>
            <el-table-column
                    prop="createTime"
                    label="创建时间"
                    width="200"
            ></el-table-column>
            <el-table-column
                    prop="isComment"
                    label="是否发表"
                    width="80"
                    fixed="right"
            >
                <template #default="{ row }">
                    <el-popconfirm
                            title="你确定修改发表状态？"
                            @confirm="handlePublishState(row)"
                    >
                        <template #reference>
                            <el-switch
                                    :value="row.isPublish"
                                    :active-value="1"
                                    :inactive-value="0"
                            ></el-switch>
                        </template>
                    </el-popconfirm>
                </template>
            </el-table-column>
            <el-table-column
                    prop="isComment"
                    label="是否评论"
                    width="80"
                    fixed="right"
            >
                <template #default="{ row }">
                    <el-popconfirm
                            title="你确定修改评论状态？"
                            @confirm="handleCommentState(row)"
                    >
                        <template #reference>
                            <el-switch
                                    :value="row.isComment"
                                    :active-value="1"
                                    :inactive-value="0"
                            ></el-switch>
                        </template>
                    </el-popconfirm>
                </template>
            </el-table-column>
            <el-table-column prop="isTop" label="是否置顶" width="80" fixed="right">
                <template #default="{ row }">
                    <el-popconfirm
                            title="你确定修改置顶状态？"
                            @confirm="handleTopState(row)"
                    >
                        <template #reference>
                            <el-switch
                                    :value="row.isTop"
                                    :active-value="1"
                                    :inactive-value="0"
                            ></el-switch>
                        </template>
                    </el-popconfirm>
                </template>
            </el-table-column>
            <el-table-column label="操作" width="100" fixed="right">
                <template #default="{ row }">
                    <el-button v-if="this.searchForm.isDelete == 0" type="text" style="color: #F56C6C" @click="handleDelete(row)">删除</el-button>
                    <el-button v-if="this.searchForm.isDelete == 0" type="text" @click="$router.push('/articleView/' + row.link)">查看</el-button>
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
    import { pageAdminArticles, updateArticleIsDelete, updatePublishState, updateCommentState, updateTopState } from "@/api/admin/article"
    export default {
        name: "index",
        data() {
            return {
                listLoading: true,
                layout: "total, sizes, prev, pager, next, jumper",
                elementLoadingText: "正在加载...",
                page: {
                    curPage: 1,
                    pageSize: 8,
                    total: 0,
                },
                articles: [],
                searchForm: {
                    title: "",
                    startTime: "",
                    endTime: "",
                    isDelete: 0
                },
            };
        },
        created() {
          this.getArticles();
        },
        methods: {
            getArticles() {
                this.listLoading = true;
                this.searchForm.curPage = this.page.curPage
                this.searchForm.pageSize = this.page.pageSize
                pageAdminArticles(this.searchForm).then((res) => {
                    if (res.code == 200) {
                        this.articles = res.data.articles
                        this.page.total = res.data.count
                    } else {
                        this.articles = []
                        this.page.total = 0
                    }
                    this.listLoading = false;
                })
            },
            getSearch() {
                this.page.curPage = 1;
                this.getArticles()
            },
            reloadTable() {
                this.searchForm = this.$options.data().searchForm;
                this.page.curPage = 1;
                this.getArticles();
            },
            handleDelete(row) {
                this.$confirm("你确定要删除文章【"+row.title+"】？", "提示", {
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    type: "warning",
                    "close-on-click-modal": false,
                }).then(() => {
                    updateArticleIsDelete(row.id, 1).then((res) => {
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
                this.$confirm("你确定要恢复文章【"+row.title+"】？", "提示", {
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    type: "warning",
                    "close-on-click-modal": false,
                }).then(() => {
                    updateArticleIsDelete(row.id, 0).then((res) => {
                        if (res.code == 200) {
                            this.reloadTable()
                            this.$message({ type: "success", message: res.message });
                        } else {
                            this.$message({ type: "error", message: res.message });
                        }
                    })
                })
            },
            handlePublishState(row) {
                const publicState = row.isPublish == 0 ? 1 : 0;
                updatePublishState(publicState, row.id)
                    .then((res) => {
                        if (res.code == 200) {
                            row.isPublish = publicState;
                            this.$message({ type: "success", message: res.message });
                        } else {
                            this.$message({ type: "error", message: res.message });
                        }
                    })
            },
            handleCommentState(row) {
                const commentState = row.isComment == 0 ? 1 : 0;
                updateCommentState(commentState, row.id)
                    .then((res) => {
                        if (res.code == 200) {
                            row.isComment = commentState;
                            this.$message({ type: "success", message: res.message });
                        } else {
                            this.$message({ type: "error", message: res.message });
                        }
                    })
            },
            handleTopState(row) {
                const topState = row.isTop == 0 ? 1 : 0;
                updateTopState(topState, row.id)
                    .then((res) => {
                        if (res.code == 200) {
                            row.isTop = topState;
                            this.$message({ type: "success", message: res.message });
                        } else {
                            this.$message({ type: "error", message: res.message });
                        }
                    })
            },
            handleChangePage(number) {
                this.page.curPage = number;
                this.getArticles();
            },
            handleChangeSize(number) {
                this.page.pageSize = number;
                this.getArticles();
            },
        }
    }
</script>

<style scoped>

</style>
