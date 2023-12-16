<template>
  <div style="padding-bottom: 30px;">
        <el-row :gutter="20">
            <el-col :span="24">
                    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="100px">

<el-form-item label="数据集名称" prop="name">
  <el-input v-model="queryParams['condition[name|like]']" placeholder="请输入应用名称" clearable style="width: 540px"
    @keyup.enter="handleQuery" />
</el-form-item>

<el-form-item>
  <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
  <el-button icon="Refresh" @click="resetQuery">重置</el-button>
</el-form-item>
</el-form>

                    <el-table v-loading="loading" :data="ApplicationList" @selection-change="handleSelectionChange">
                        <el-table-column type="index" width="30" align="center" />

                        <el-table-column label="类型" align="center" width="50px" prop="icon" v-if="columns[0].visible">
                            <template #default="scope">
                                <div class="role-icon" style="font-size: 20px;">
                                    <i class="fa-solid fa-file-pdf" />
                                </div>
                            </template>
                        </el-table-column>

                        <el-table-column label="查询结果" align="left" key="name" prop="name" v-if="columns[1].visible">
                            <template #default="scope">
                                <!-- <div style="font-size: 15px;font-weight: 500;color: #3b5998;">
                                    {{ scope.row.name }}
                                </div> -->
                                <div style="font-size: 13px;color: rgb(59, 89, 152);height: 43px;overflow: hidden;text-overflow: ellipsis;">
                                    数据安全防护系统保障数据的保密性、完整性和可用性，按照信息系统安全保 护等级，具有对数据安全从三方面进行防护——对敏感数据进行加密、保障数据传输安全和建立安全分 级身份认证。\n10.2  信息安全防护要求\n\n10.2.1 结构安全保障\nGB/T XXXXX—XXXX\n21\n\n信息网络分域分级， 按用户业务划分安全域， 并根据安全域支撑的业务，通过有效的路由控制、带 宽控制，保障关键业务对网络资源的需求。\n10.2.2 网络行为审计\n\n提供可视化管理，对信息网络关键节点上的业务访问进行深度识别与全面审计， 提供基于用户、访 问行为、系统资源等实施监控措施， 提升信息网络的透明度。\n10.2.3 边界完整性保护\n\n系统具备与第三方终端系统整合功能，对非法接入的终端进行识别与阻断。\n\n10.2.4 攻击和入侵防范要求\n\n提供基于应用的入侵防范，在实现对攻击行为的深度检测同时， 通过应用识别来锁定真实的应用， 并以此为基础进行深度的攻击分析， 准确、快捷地定位攻击的类型。
                                </div>
                            </template>
                        </el-table-column>
                        <el-table-column label="评分" width="100px" align="center" key="name" prop="name" v-if="columns[1].visible"
                            :show-overflow-tooltip="true">
                            <template #default="scope">
                                <div style="font-size: 15px;font-weight: 500;color: #3b5998;">
                                    9.88342
                                </div>
                            </template>
                        </el-table-column>

                        <el-table-column label="操作" align="center" width="100" class-name="small-padding fixed-width"
                            v-if="columns[8].visible">
                            <template #default="scope">
                                <el-tooltip content="修改" placement="top" v-if="scope.row.applicationId !== 1">
                                    <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                                        v-hasPermi="['system:Application:edit']"></el-button>
                                </el-tooltip>
                                <el-tooltip content="删除" placement="top" v-if="scope.row.applicationId !== 1">
                                    <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)"
                                        v-hasPermi="['system:Application:remove']"></el-button>
                                </el-tooltip>

                            </template>
                        </el-table-column>
                    </el-table>
                    <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />
            </el-col>

        </el-row>
    </div>
</template>

<script setup>
import { ref } from 'vue'

const textarea = ref('')

import { getToken } from "@/utils/auth";
import {
    listApplication,
    delApplication,
    getApplication,
    updateApplication,
    addApplication,
} from "@/api/base/search/vectorDataset";
import { reactive } from "vue";

const router = useRouter();
const { proxy } = getCurrentInstance();
// const { sys_normal_disable, sys_Application_sex } = proxy.useDict("sys_normal_disable", "sys_Application_sex");

const ApplicationList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");
const dateRange = ref([]);
const deptName = ref("");
const deptOptions = ref(undefined);
const initPassword = ref(undefined);
const postOptions = ref([]);
const roleOptions = ref([]);
/*** 应用导入参数 */
const upload = reactive({
    // 是否显示弹出层（应用导入）
    open: false,
    // 弹出层标题（应用导入）
    title: "",
    // 是否禁用上传
    isUploading: false,
    // 是否更新已经存在的应用数据
    updateSupport: 0,
    // 设置上传的请求头部
    headers: { Authorization: "Bearer " + getToken() },
    // 上传的地址
    url: import.meta.env.VITE_APP_BASE_API + "/system/Application/importData"
});
// 列显隐信息
const columns = ref([
    { key: 0, label: `图标`, visible: true },
    { key: 1, label: `数据集名称`, visible: true },
    { key: 2, label: `所有者`, visible: true },
    { key: 3, label: `描述信息`, visible: true },
    { key: 4, label: `状态`, visible: true },
    { key: 5, label: `访问权限`, visible: true },
    { key: 6, label: `数据总量`, visible: true },
    { key: 7, label: `创建时间`, visible: true },
    { key: 8, label: `编辑`, visible: true },

]);

const data = reactive({
    form: {},
    queryParams: {
        pageNum: 1,
        pageSize: 10,
        ApplicationName: undefined,
        name: undefined,
        ownerId: undefined,
        status: undefined,
        deptId: undefined
    },
    rules: {
        applicationId: [{ required: true, message: "应用编号不能为空", trigger: "blur" }],
        name: [{ required: true, message: "应用名称不能为空", trigger: "blur" }, {
            min: 2,
            max: 20,
            message: "应用名称长度必须介于 2 和 20 之间",
            trigger: "blur"
        }],
        ownerId: [{ required: true, message: "显示名称不能为空", trigger: "blur" }],
        description: [{ required: true, message: "描述信息不能为空", trigger: "blur" }],
        datasetStatus: [{ required: true, message: "域名不能为空", trigger: "blur" }],
        accessPermission: [{ required: true, message: "安全存储路径不能为空", trigger: "blur" }],
        datasetSize: [{ required: true, message: "应用目标不能为空", trigger: "blur" }],
    }
});

const { queryParams, form, rules } = toRefs(data);


/** 查询应用列表 */
function getList() {
    loading.value = true;
    listApplication(proxy.addDateRange(queryParams.value, dateRange.value)).then(res => {
        loading.value = false;
        ApplicationList.value = res.rows;
        total.value = res.total;
    });
};


/** 搜索按钮操作 */
function handleQuery() {
    console.log(queryParams);
    queryParams.value.pageNum = 1;
    getList();
};

/** 重置按钮操作 */
function resetQuery() {
    dateRange.value = [];
    proxy.resetForm("queryRef");
    queryParams.value.name = undefined;
    queryParams.value.ownerId = undefined;
    proxy.$refs.deptTreeRef.setCurrentKey(null);
    handleQuery();
};

/** 删除按钮操作 */
function handleDelete(row) {
    const applicationIds = row.id || ids.value;

    proxy.$modal.confirm('是否确认删除应用编号为"' + applicationIds + '"的数据项？').then(function () {
        return delApplication(applicationIds);
    }).then(() => {
        getList();
        proxy.$modal.msgSuccess("删除成功");
    }).catch(() => {
    });
};

/** 选择条数  */
function handleSelectionChange(selection) {
    ids.value = selection.map(item => item.id);
    single.value = selection.length != 1;
    multiple.value = !selection.length;
};

/** 重置操作表单 */
function reset() {
    form.value = {
        applicationId: undefined,
        name: undefined,
        ownerId: undefined,
        description: undefined,
        datasetStatus: undefined,
        accessPermission: undefined,
        datasetSize: undefined,
    };
    proxy.resetForm("ApplicationRef");
};

/** 取消按钮 */
function cancel() {
    open.value = false;
    reset();
};

/** 新增按钮操作 */
function handleAdd() {
    reset();
    open.value = true;
    title.value = "添加应用";
};

/** 修改按钮操作 */
function handleUpdate(row) {
    reset();
    const applicationId = row.id || ids.value;
    getApplication(applicationId).then(response => {
        form.value = response.data;
        form.value.applicationId = applicationId;
        open.value = true;
        title.value = "修改应用";

    });
};

/** 导入按钮操作 */
function handleImport() {
    upload.title = "数据集导入";
    upload.open = true;
};

/** 下载模板操作 */
function importTemplate() {
    proxy.download("system/user/importTemplate", {
    }, `user_template_${new Date().getTime()}.xlsx`);
};

/**文件上传中处理 */
const handleFileUploadProgress = (event, file, fileList) => {
    upload.isUploading = true;
};
/** 文件上传成功处理 */
const handleFileSuccess = (response, file, fileList) => {
    upload.open = false;
    upload.isUploading = false;
    proxy.$refs["uploadRef"].handleRemove(file);
    proxy.$alert("<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" + response.msg + "</div>", "导入结果", { dangerouslyUseHTMLString: true });
    getList();
};
/** 提交上传文件 */
function submitFileForm() {
    proxy.$refs["uploadRef"].submit();
};

/** 提交按钮 */
function submitForm() {
    proxy.$refs["ApplicationRef"].validate(valid => {
        if (valid) {
            if (form.value.applicationId != undefined) {
                updateApplication(form.value).then(response => {
                    proxy.$modal.msgSuccess("修改成功");
                    open.value = false;
                    getList();
                });
            } else {
                addApplication(form.value).then(response => {
                    proxy.$modal.msgSuccess("新增成功");
                    open.value = false;
                    getList();
                });
            }
        }
    });
};

getList();
</script>

<style lang="scss" scoped>
.role-icon {
    img {
        width: 45px;
        height: 45px;
        border-radius: 50%;
    }
}
</style>

<style lang="scss" scoped>
.input-search-text {
    width: 100%;
    resize: none;
    font-size: 14px;
    border: 0px solid #ccc;
    border-radius: 5px;
    background-color: #fafafa;
    outline: none;

    textarea {
        background-color: #fafafa;
    }
}
</style>