<template>
    <div class="article-container">
        <el-image style="width: 100%; height: 400px" :src="article.coverUrl" fit="cover" />
        <div class="title">【{{article.title}}】</div>
        <el-card>
            <el-avatar :size="50" :src="article.createUserAvatar" />
            <span class="username">{{article.createUserName}}</span>
            <md-editor
                    v-model="article.content"
                    class="form-editor"
                    :previewOnly="true"
            ></md-editor>
            <hr />
            <div class="article-tags">
                <a
                        href="javascript: 0"
                        v-for="(tag, index) in article.tags"
                        :key="index"
                        @click="this.$router.push('/tagList/' + tag)"
                >
                    <i class="fa fa-tag">{{ tag }}</i>
                </a>
            </div>
            <div class="article-praise">
                <el-button id="priseBtn" @click="articlePraise">
                    <i class="fa fa-thumbs-o-up"></i>
                    <span style="display: inline-block">{{ article.praiseNum }}</span>
                </el-button>
            </div>
            <div class="article-comment">
                <h3 class="comment-title">
                    <i class="fa fa-comments"></i> 评论
                </h3>
                <div class="comment-submit" v-show="article.isComment == 1">
                    <el-input
                            type="textarea"
                            :autosize="{ minRows: 5 }"
                            v-model="commentForm.content"
                            placeholder="觉得文章不错，给个评价吧(。・∀・)ノ"
                    />
                    <div  class="submit-btn">
                        <el-button
                                type="warning"
                                @click="$router.push('/login')"
                                v-if="Object.keys(userInfo).length==0"
                        >
                            <i class="fa fa-paper-plane"></i>
                            请先登录
                        </el-button>
                        <el-button
                                type="primary"
                                @click="commentArticle(0)"
                                v-else
                        >
                            <i class="fa fa-paper-plane"></i>
                            发送
                        </el-button>
                    </div>
                </div>
                <div class="comments">
                    <h3 class="comment-title">{{ commentPage.total }} 条评论</h3>
                    <div class="comment" v-for="(comment, index) in comments" :key="index">
                        <el-row :gutter="15">
                            <el-col :span="3">
                                <el-avatar
                                        :size="50"
                                        shape="square"
                                        :src="comment.userInfo.avatar"
                                        @error="true"
                                ></el-avatar>
                            </el-col>
                            <el-col :span="21">
                                <div class="user">
                                    <a href="javascript:void(0)">
                                        <strong>{{ comment.userInfo.nickname }}</strong>
                                    </a>
                                    <span class="tag-role" v-if="comment.userId == article.createUser">博主</span>
                                    <time>{{ comment.commentTime }}</time>
                                </div>
                                <div class="content">
                                    <span><p>{{ comment.content }}</p></span>
                                </div>
                            </el-col>
                        </el-row>
                    </div>
                    <div class="comment-more" @click="getMoreComment" v-show="commentPage.curPage * commentPage.pageSize <commentPage.total">
                        查看更多
                    </div>
                    <div class="comment-loading" v-loading="commentLoading"></div>
                    <el-empty v-show="comments.length == 0" description="文章暂无评论哦~"/>
                </div>
            </div>
        </el-card>

    </div>
</template>

<script>
    import { getArticle, commentArticle, getCommentByArticleId, praiseArticle } from "@/api/user/article";
    import MdEditor from "md-editor-v3";
    import "md-editor-v3/lib/style.css";
    import {mapGetters} from "vuex";
    export default {
        name: "article-view",
        computed: {
            ...mapGetters({
                userInfo: 'userInfo',
            })
        },
        components: {
            MdEditor
        },
        data() {
            return {
                article: {},
                commentForm: {
                    parentId: '',
                    articleId: '',
                    content: '',
                },
                comments: [],
                commentPage: {
                    curPage: 1,
                    pageSize: 10,
                    total: 0,
                },
                commentLoading: false,
                praiseDisabled: true,
            }
        },
        created() {
          this.getArticle();
        },
        methods: {
            getArticle() {
                const year = this.$route.params.year;
                const month = this.$route.params.month;
                const day = this.$route.params.day;
                const title = this.$route.params.title;
                getArticle(year, month, day, title).then((res) => {
                    if (res.code == 200) {
                        this.article = res.data
                        this.praiseDisabled = false;
                        this.initComment();
                    } else {
                        this.$message({ type: "error", message: res.message });
                    }
                })
            },
            initComment() {
                this.commentPage.id = this.article.id;
                this.commentLoading = true;
                getCommentByArticleId(this.commentPage).then((res) => {
                    if (res.code == 200) {
                        this.comments = res.data.comments;
                        this.commentPage.total = res.data.count;
                    }
                    this.commentLoading = false;
                });
            },
            getMoreComment() {
                this.commentPage.curPage = this.commentPage.curPage + 1;
                this.commentLoading = true;
                getCommentByArticleId(this.commentPage).then((res) => {
                    if (res.code == 200) {
                        this.comments = this.comments.concat(res.data.comments);
                    }
                    this.commentLoading = false;
                });
            },
            commentArticle(parentId) {
                if (this.commentForm.content == '') {
                    this.$message({ type: "error", message: "请输入评论内容" });
                    return;
                }
                this.commentForm.articleId = this.article.id
                this.commentForm.parentId = parentId
                commentArticle(this.commentForm).then((res) => {
                    if (res.code == 200) {
                        this.initComment();
                        this.commentForm.content = ''
                        this.$message({ type: "success", message: res.message });
                    } else {
                        this.$message({ type: "error", message: res.message });
                    }
                })
            },
            articlePraise() {
                if (this.praiseDisabled) {
                    return;
                }
                this.praiseDisabled = true;
                const user = Object.assign({}, this.$store.state.user.userInfo);
                if (Object.keys(user).length == 0) {
                    this.$message({ type: "error", message: "请先登录" });
                    this.praiseDisabled = false;
                    return;
                }
                praiseArticle(this.article.id, user.id).then((res) => {
                    if (res.code == 200) {
                        if (res.data) {
                            this.article.praiseNum = this.article.praiseNum + 1;
                        } else {
                            this.article.praiseNum = this.article.praiseNum - 1;
                        }
                        this.$message({ type: "success", message: res.message });
                    } else {
                        this.$message({ type: "error", message: res.message });
                    }
                    this.praiseDisabled = false;
                });
            },
        }
    }
</script>

<style scoped>
    .title {
        font-size: 30px;
        display: block;
        width: 695px;
        position: absolute;
        top: 150px;
        text-align: center;
        color: #fff;
        background-color: rgba(0,0,0,0.5);
        padding: 10px 0;
    }
    .el-card {
        min-height: calc(100vh - 500px);
    }
    .username {
        vertical-align: top;
        margin: 0 15px;
        font-size: 18px;
        font-weight: bold;
        line-height: 50px;
    }
    .form-editor {
        margin-top: 20px;
        margin-bottom: 40px;
    }
    hr {
        position: relative;
        margin: 2rem auto;
        width: calc(100% - 4px);
        border: 2px dashed #a4d8fa;
        background: #fff;
        box-sizing: content-box;
        height: 0;
        overflow: visible;
    }

    .article-tags {
        margin: 1rem 0;
    }

    .article-tags a {
        display: inline-block;
        padding: 0.75rem;
        border-radius: 0.3125rem;
        background: #f9f9f9;
        color: #fdbc40;
        margin-right: 6px;
    }
    .article-tags a:hover {
        background: #fdbc40;
        color: #fff;
    }
    .article-praise {
        margin: 1rem auto;
        text-align: center;
    }
    .article-praise #priseBtn {
        width: 70px;
        height: 70px;
        border: 0;
        background: #f44336;
        color: #fff;
        padding: 1rem;
        border-radius: 50%;
        outline: none;
        box-shadow: 0 1px 30px -10px #afacac;
        transition: all 0.3s;
        font-size: 16px !important;
    }
    .article-praise #priseBtn:hover {
        opacity: 0.8;
        box-shadow: 0 1px 20px -4px #f44336;
    }
    .comment-submit {
        margin: 20px 0;
    }
    .submit-btn {
        text-align: right;
        margin: 10px 0;
    }
    .comment-title {
        color: #000;
        font-weight: 400;
        margin: 1rem 0;
    }
    .comment {
        padding: 20px;
        position: relative;
        z-index: 99;
        font-size: 16px;
    }
    .comment .user {
        display: inline-block;
    }
    .comment .user a {
        text-decoration: none;
        color: #ef794f;
    }
    .comment .user time {
        color: #34495e;
        margin-left: 10px;
        font-weight: inherit;
        font-size: 12px;
    }
    .comment .content {
        margin-top: 0.5rem;
        overflow: auto;
        max-height: 500px;
        background: #f2f3f5;
        color: #272829;
        padding: 16px;
        border-radius: 12px;
    }

    .comment .content span p {
        font-size: 15px;
    }
    .comment .user .tag-role {
        background-color: #67c23a20;
        border: 1px solid #fdbc40;
        border-radius: 2px;
        color: #fdbc40;
        display: inline-block;
        padding: 0 0.5em;
        font-size: 0.75em;
        margin-left: 10px;
        font-family: "Merriweather Sans", Helvetica, Tahoma, Arial, "PingFang SC",
        "Hiragino Sans GB", "Microsoft Yahei", "WenQuanYi Micro Hei", sans-serif;
    }
    .comment-more {
        width: 100%;
        cursor: pointer;
        padding: 0.75em;
        text-align: center;
        transition: all 0.5s;
        color: #272829;
    }
    .comment-more:hover {
        background-color: #00000020;
    }
</style>
