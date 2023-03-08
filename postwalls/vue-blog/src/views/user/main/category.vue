<template>
    <el-breadcrumb separator="/" style="margin-bottom: 20px">
        <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item>分类</el-breadcrumb-item>
    </el-breadcrumb>
    <el-row justify="center">
        <el-col :span="24" v-for="(item, index) in categories" :key="index">
            <a href="javascript: void(0)" @click="$router.push('/categoryList/' + item.id + '/' + item.name)">
                <el-card shadow="hover" :body-style="{padding: 0}" class="card">
                    <el-image style="height: 200px; width: 350px" :src="item.cover" fit="cover" />
                    <span class="category-name">{{item.name}}</span>
                </el-card>
            </a>
        </el-col>
        <el-empty v-show="categories.length == 0" description="暂时没有分类哦~"/>
    </el-row>
</template>

<script>
    import { getCategoryList } from "@/api/user/category"
    export default {
        name: "category",
        data() {
            return {
                categories: []
            }
        },
        created() {
          this.getCategoryList()
        },
        methods: {
            getCategoryList() {
                getCategoryList().then((res) => {
                    if (res.code == 200) {
                        this.categories = res.data
                    }
                })
            }
        }
    }
</script>

<style scoped>
    .card {
        width: 350px;
        margin: 0 auto 20px;
        position: relative;
    }
    .category-name {
        position: absolute;
        color: #ffffff;
        font-size: 26px;
        display: block;
        top: 50%;
        transform: translateY(-50%);
        width: 350px;
        text-align: center;
        background-color: rgba(0, 0, 0, .5);
        padding: 5px 0;
    }
</style>
