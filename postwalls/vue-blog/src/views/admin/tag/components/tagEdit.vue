<template>
    <el-dialog
            :title="title"
            :close-on-click-modal="false"
            v-model="dialogFormVisible"
            width="500px"
            @close="close"
    >
        <el-form ref="form" :model="form" :rules="rules" label-width="80px" @submit.prevent>
            <el-form-item label="标签名称" prop="name">
                <el-input
                        v-model.trim="form.name"
                        autocomplete="off"
                        @keyup.enter="save"
                ></el-input>
            </el-form-item>
        </el-form>
        <template #footer>
            <el-button @click="close">取 消</el-button>
            <el-button type="primary" @click="save">确 定</el-button> </template
        >
    </el-dialog>
</template>

<script>
    import { insertTag } from "@/api/admin/tag"
    export default {
        name: "tagEdit",
        data() {
            return {
                form: {
                    name: "",
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
                    this.title = "添加标签";
                    this.operate = false;
                } else {
                    this.title = "修改标签";
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
            async save() {
                this.$refs["form"].validate(async (valid) => {
                    if (valid) {
                        this.refreshTable = true;
                        await insertTag(this.form.name).then((res) => {
                            if (res.code == 200) {
                                this.$message({ type: "success", message: res.message });
                                this.close();
                            } else {
                                this.$message({ type: "error", message: res.message });
                            }
                        })
                    }  else {
                        return false;
                    }
                })
            }
        }
    }
</script>

<style scoped>

</style>
