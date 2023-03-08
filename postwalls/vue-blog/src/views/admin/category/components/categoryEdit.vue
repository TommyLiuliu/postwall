<template>
    <el-dialog
            :title="title"
            :close-on-click-modal="false"
            v-model="dialogFormVisible"
            width="500px"
            @close="close"
    >
        <el-form ref="form" :model="form" :rules="rules" label-width="80px">
            <el-form-item label="分类名称" prop="name">
                <el-input v-model.trim="form.name" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="状态" prop="state">
                <el-select v-model="form.state" placeholder="状态">
                    <el-option label="开启" :value="1"></el-option>
                    <el-option label="关闭" :value="2"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="排序" prop="sort">
                <el-input v-model.trim="form.sort" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="描述" prop="remark">
                <el-input v-model.trim="form.remark" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="封面" prop="coverUrl">
                <el-upload
                        v-if="form.base64"
                        ref="uploadRef"
                        action="#"
                        :show-file-list="false"
                        accept="image/*"
                        :on-change="selectImage"
                        :auto-upload="false"
                >
                    <template #trigger>
                        <el-image style="width: 100px; height: 100px"
                                  :src="form.base64"
                                  fit="cover"
                        />
                    </template>
                </el-upload>

                <el-input v-else v-model.trim="form.coverUrl" autocomplete="off">
                    <template #append>
                        <el-upload
                                ref="uploadRef"
                                action="#"
                                :show-file-list="false"
                                accept="image/*"
                                :on-change="selectImage"
                                :auto-upload="false"
                        >
                            <template #trigger>
                                <el-button type="primary">上传</el-button>
                            </template>
                        </el-upload>
                    </template>
                </el-input>
            </el-form-item>
        </el-form>
        <template #footer>
            <el-button @click="close">取 消</el-button>
            <el-button type="primary" @click="save">确 定</el-button>
        </template>
    </el-dialog>
</template>

<script>
    import { insertCategory, updateCategory } from "@/api/admin/category"
    export default {
        name: "categoryEdit",
        data() {
            return {
                form: {
                    name: "",
                    state: 1,
                    sort: "",
                    remark: "",
                    coverUrl: "",
                },
                rules: {
                    name: [{ required: true, trigger: "blur", message: "请输入名称" }],
                },
                title: "",
                operate: false,
                dialogFormVisible: false,
                refreshTable: false,
            };
        },
        methods: {
            showEdit(row) {
                if (!row) {
                    this.title = "添加分类";
                    this.operate = false;
                } else {
                    this.title = "修改分类";
                    this.operate = true;
                    this.form = Object.assign({}, row);
                }
                this.dialogFormVisible = true;
                this.refreshTable = false;
            },
            close() {
                this.$refs["form"].resetFields();
                this.form = this.$options.data().form;
                this.dialogFormVisible = false;
                if (this.refreshTable) {
                    this.$emit("reloadTable");
                }
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

            async save() {
                this.$refs["form"].validate(async (valid) => {
                    if (valid) {
                        this.refreshTable = true;
                        if(this.operate) {
                            updateCategory(this.form).then((res) => {
                                if (res.code == 200) {
                                    this.$message({ type: "success", message: res.message });
                                    this.close();
                                } else {
                                    this.$message({ type: "error", message: res.message });
                                }
                            })
                        } else {
                            await insertCategory(this.form).then((res) => {
                                if (res.code == 200) {
                                    this.$message({ type: "success", message: res.message });
                                    this.close();
                                } else {
                                    this.$message({ type: "error", message: res.message });
                                }
                            })
                        }
                    } else {
                        return false;
                    }
                })
            },
        }
    }
</script>

<style scoped>

</style>
