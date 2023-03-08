<template>
    <div class="articles">
        <a href="javascript: void(0)" v-for="(item, index) in articles" :key="index">
            <el-card class="box-card" shadow="hover" @click="$router.push('/articleView/' + item.link)">
                <div class="category">
                    {{item.categoryName}}
                </div>
                <div class="cover">
                    <el-image style="width: 380px; height: 254px" :src="item.coverUrl" fit="cover" />
                </div>
                <div class="article-info">
                    <h2 class="title">{{item.title}}</h2>
                    <div>
                        <el-avatar :size="40" :src="item.createUserAvatar" />
                        <span class="username">{{item.createUserName}}</span>
                    </div>
                    <div class="meta">
                        <span><i class="fa fa-fire"></i> {{item.readNum}} </span>
                        <span><i class="fa fa-thumbs-up"></i> {{item.praiseNum}} </span>
                        <span><i class="fa fa-commenting"></i> {{item.commentNum}} </span>
                        <span><i class="fa fa-clock-o"></i> {{item.publishDate}}</span>
                        <span><h4 style="display: inline-block;margin-right: 5px" v-for="(tag, tagIndex) in item.tags"
                            :key="tagIndex"><i class="fa fa-tag"></i>{{tag}} </h4></span>
                    </div>
                </div>
            </el-card>
        </a>
        <el-empty v-show="articles.length == 0" description="暂时没有文章哦~"/>
    </div>
    <el-pagination
            background
            :current-page="page.curPage"
            :page-size="page.pageSize"
            layout="total, prev, pager, next"
            :total="page.total"
            @size-change="handleChangeSize"
            @current-change="handleChangePage"
    ></el-pagination>
</template>

<script>
    import { pageArticles } from "@/api/user/article"
    export default {
        name: "main",
        data() {
            return {
                page: {
                    curPage: 1,
                    pageSize: 10,
                    total: 0,
                },
                articles: []
            }
        },
        created() {
          this.getArticles()
        },
        methods: {
            getArticles() {
                pageArticles(this.page).then((res) => {
                    if (res.code == 200) {
                        this.articles = res.data.articles
                        this.page.total = res.data.count
                    } else {
                        this.articles = []
                        this.page.total = 0
                    }
                })
            },
            search() {
                this.page.curPage = 1
                this.getArticles()
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
    .articles {
        margin-top: 20px;
    }
    .cover {
        display: inline-block;
    }
    .article-info {
        display: inline-block;
        vertical-align: top;
        margin-left: 15px;
        max-width: 252px;
    }
    .title {
        overflow: hidden;
        text-overflow: ellipsis;
        display: -webkit-box;
        -webkit-line-clamp: 3;
        -webkit-box-orient: vertical;
        height: 81px;
    }
    .username {
        vertical-align: top;
        font-size: 14px;
        font-weight: bold;
        line-height: 40px;
        margin: 0 10px;
    }
    .category {
        position: absolute;
        margin-left: -16px;
        margin-top: 14px;
        background-color: #fdbc40;
        box-shadow: 0 1px 5px #fdbc40;
        letter-spacing: 4px;
        z-index: 10;
        display: inline;
        padding: 0.8rem;
        font-size: 75%;
        font-weight: 700;
        line-height: 1;
        color: #fff;
        text-align: center;
        white-space: nowrap;
        vertical-align: baseline;
        border-radius: 0.25em;
    }
    .category:before {
        content: "";
        position: absolute;
        top: 100%;
        left: 0;
        width: 0;
        height: 0;
        background-color: transparent;
        border-style: solid;
        border-width: 0 1rem 1rem 0;
        border-color: transparent;
        border-right-color: #fdbc40;
        filter: brightness(.9);
    }
    .meta {
        margin: 15px 0;
    }
    .meta span {
        display: block;
        margin: 5px 0;
    }
    .articles .el-card {
        margin-bottom: 20px;
    }
</style>
