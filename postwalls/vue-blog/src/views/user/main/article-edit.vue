<template>
    <el-dialog v-model="dialogFormVisible" :fullscreen="true" @close="close">
        <template #header>
            <div style="font-size: 16px">
                <i class="fa fa-edit"></i>
                <span v-text="title"></span>
            </div>
        </template>
        <el-form ref="form" :model="form" :rules="blogRules" label-width="50px">
            <el-row :gutter="20">
                <el-col :xs="24" :sm="24" :md="18" :lg="18" :xl="18">
                    <el-row :gutter="20">
                        <el-col>
                            <el-form-item label="标题" prop="title">
                                <el-input
                                        v-model.trim="form.title"
                                        autocomplete="off"
                                ></el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <md-editor
                            v-model="form.content"
                            class="form-editor"
                            @onUploadImg="onUploadImg"
                    ></md-editor>
                </el-col>
                <el-col :xs="24" :sm="24" :md="6" :lg="6" :xl="6">
                    <el-image :src="form.base64" fit="cover" v-if="form.base64"></el-image>
                    <el-image :src="form.coverUrl" fit="cover" v-else></el-image>
                    <div class="image-button">
                        <el-upload
                                ref="uploadRef"
                                action="#"
                                :show-file-list="false"
                                accept="image/*"
                                :on-change="selectImage"
                                :auto-upload="false"
                        >
                            <template #trigger>
                                <el-button type="primary">上传封面</el-button>
                            </template>
                        </el-upload>
                    </div>
                    <el-form-item label="封面路径" prop="coverUrl" label-width="80px">
                        <el-input
                                v-model.trim="form.coverUrl"
                                autocomplete="off"
                        ></el-input>
                    </el-form-item>
                    <el-form-item label="文章分类" prop="categoryId" label-width="80px">
                        <el-select
                                v-model="form.categoryId"
                                placeholder="分类"
                                no-data-text="没有找到分类"
                        >
                            <el-option
                                    v-for="(item, index) in categoryList"
                                    :key="index"
                                    :label="item.name"
                                    :value="item.id"
                            ></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="文章标签" prop="tags" label-width="80px">
                        <el-select
                                v-model="form.tags"
                                multiple
                                placeholder="标签"
                                no-data-text="没有找到标签"
                        >
                            <el-option
                                    v-for="(item, index) in tags"
                                    :key="index"
                                    :label="item.name"
                                    :value="item.name"
                            ></el-option>
                        </el-select>
                    </el-form-item>
                    <el-button
                            type="primary"
                            style="width: 100%; font-size: 16px"
                            @click="saveBlog"
                    >
                        发表
                    </el-button>
                </el-col>
            </el-row>
        </el-form>
    </el-dialog>
</template>

<script>
    import MdEditor from "md-editor-v3";
    import "md-editor-v3/lib/style.css";
    import { getCategoryList } from "@/api/user/category"
    import { getTagList } from "@/api/user/tag"
    import { insertArticle, updateArticle, upload } from "@/api/user/article"
    export default {
        name: "article-edit",
        components: {
            MdEditor
        },
        data() {
            return {
                title: "",
                dialogFormVisible: false,
                operate: false,
                refreshTable: false,
                form: {
                    id: '',
                    categoryId: '',
                    title: '',
                    content: '',
                    coverUrl: '',
                    base64: '',
                    tags: ''
                },
                blogRules: {
                    title: [{ required: true, trigger: "blur", message: "请输入标题" }],
                    categoryId: [
                        { required: true, trigger: "blur", message: "请选择分类" },
                    ],
                },
                categoryList: [],
                tags: []
            }
        },
        methods: {
            showEdit(row) {
                if (!row) {
                    this.title = " 编辑文章";
                    this.operate = false;
                } else {
                    this.title = " 修改文章";
                    this.operate = true;
                    this.form = Object.assign({}, row);
                }
                this.dialogFormVisible = true;
                this.refreshTable = false;
                this.getCategoryList()
                this.getTagList()
            },
            close() {
                this.$refs["form"].resetFields();
                this.form = this.$options.data().form;
                this.dialogFormVisible = false;
                this.$emit("reloadTable");
            },
            saveBlog() {
                this.$refs["form"].validate(async (valid) => {
                  if (valid) {
                      this.refreshTable = true;
                      if(this.operate) {
                          updateArticle(this.form).then((res) => {
                              if (res.code == 200) {
                                  this.$message({ type: "success", message: res.message });
                                  this.close();
                              } else {
                                  this.$message({ type: "error", message: res.message });
                              }
                          })
                      } else {
                          insertArticle(this.form).then((res) => {
                              if (res.code == 200) {
                                  this.$message({ type: "success", message: res.message });
                                  this.close();
                              } else {
                                  this.$message({ type: "error", message: res.message });
                              }
                          })
                      }
                  }
                })
            },
            getCategoryList() {
                getCategoryList().then((res) => {
                    if (res.code == 200) {
                        this.categoryList = res.data
                    }
                })
            },
            getTagList() {
                getTagList().then((res) => {
                    if (res.code == 200) {
                        this.tags = res.data
                    }
                })
            },
            selectImage(file) {
                this.getBase64(file.raw).then(res=>{
                    this.form.base64 = res
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
            async onUploadImg(files, callback) {
                const res = await Promise.all(
                    files.map((file) => {
                        return new Promise((rev, rej) => {
                            const form = new FormData();
                            form.append("file", file);
                            upload(form).then((res) => rev(res))
                                .catch((error) => rej(error));
                        });
                    })
                );
                callback(res.map((item) => item.data));
            },
        }
    }
</script>

<style scoped>
.form-editor {
    margin: 20px 0;
    height: 700px;
}
.el-image {
    width: 100%;
    height: 280px;
}
.image-button {
    padding: 5px 0 20px;
}
.el-form-item /deep/ .el-form-item__content {
    display: inline-block !important;
}
</style>
