<template>
  <div class="app-container">
     <el-row :gutter="20">
        <!--应用数据-->
        <el-col :span="24" :xs="24">
           <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="100px">
              <el-form-item label="索引基础名称" prop="indexBase">
                 <el-input v-model="queryParams.indexBase" placeholder="请输入索引基础名称" clearable style="width: 240px" @keyup.enter="handleQuery" />
              </el-form-item>
              <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
              <el-button icon="Refresh" @click="resetQuery">重置</el-button>
           </el-form>

           <el-row :gutter="10" class="mb8">
              <el-col :span="1.5">
                 <el-button type="primary" plain icon="Plus" @click="handleAdd">新增</el-button>
              </el-col>
              <el-col :span="1.5">
                 <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate">修改</el-button>
              </el-col>
              <el-col :span="1.5">
                 <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete">删除</el-button>
              </el-col>
              <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
           </el-row>

           <el-table v-loading="loading" :data="DocumentIndexList" @selection-change="handleSelectionChange">
              <el-table-column type="selection" width="50" align="center" />
              <el-table-column label="索引名称" align="left" key="indexBase" prop="indexBase" v-if="columns[0].visible">
                 <template #default="scope">
                    <div style="font-size: 15px;font-weight: 500;color: #3b5998;">
                       {{ scope.row.showName }}
                    </div>
                    <div style="font-size: 13px;color: #a5a5a5;">
                       {{ scope.row.indexBase }}
                    </div>
                 </template>
              </el-table-column>
              <el-table-column label="索引类型" align="center" key="indexType" prop="indexType" v-if="columns[2].visible" :show-overflow-tooltip="true" />
              <el-table-column label="分片数" align="center" key="shardNum" prop="shardNum" v-if="columns[4].visible" :show-overflow-tooltip="true" />
              <el-table-column label="副本数" align="center" key="replicaNum" prop="replicaNum" v-if="columns[5].visible" :show-overflow-tooltip="true" />
              <el-table-column label="文档数量" align="center" key="docCount" prop="docCount" v-if="columns[6].visible" :show-overflow-tooltip="true" />
              <el-table-column label="存储数据量" align="center" key="storageSize" prop="storageSize" v-if="columns[8].visible" :show-overflow-tooltip="true" />
              <el-table-column label="操作" align="center" width="150" class-name="small-padding fixed-width">
                 <template #default="scope">
                    <el-tooltip content="修改" placement="top" v-if="scope.row.DocumentIndexId !== 1">
                       <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['system:DocumentIndex:edit']"></el-button>
                    </el-tooltip>
                    <el-tooltip content="删除" placement="top" v-if="scope.row.DocumentIndexId !== 1">
                       <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['system:DocumentIndex:remove']"></el-button>
                    </el-tooltip>
                 </template>
              </el-table-column>
           </el-table>
           <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />
        </el-col>
     </el-row>

     <!-- 添加或修改应用配置对话框 -->
     <el-dialog :title="title" v-model="open" width="900px" append-to-body>
        <el-form :model="form" :rules="rules" ref="databaseRef" label-width="100px">
           <el-row>
              <el-col :span="24">
                 <el-form-item label="索引基础名称" prop="indexBase">
                    <el-input v-model="form.indexBase" placeholder="请输入索引基础名称" maxlength="255" />
                 </el-form-item>
              </el-col>
           </el-row>
           <el-row>
              <el-col :span="24">
                 <el-form-item label="字段名称" prop="fieldName">
                    <el-input v-model="form.fieldName" placeholder="请输入字段名称" maxlength="255" />
                 </el-form-item>
              </el-col>
           </el-row>
           <el-row>
              <el-col :span="24">
                 <el-form-item label="索引类型" prop="indexType">
                    <el-input v-model="form.indexType" placeholder="请输入索引类型" maxlength="255" />
                 </el-form-item>
              </el-col>
           </el-row>
           <el-row>
              <el-col :span="24">
                 <el-form-item label="文档ID" prop="docId">
                    <el-input v-model="form.docId" placeholder="请输入文档ID" maxlength="255" />
                 </el-form-item>
              </el-col>
           </el-row>
           <el-row>
              <el-col :span="24">
                 <el-form-item label="分片数" prop="shardNum">
                    <el-input-number v-model="form.shardNum" controls-position="right" :min="1" :max="100" />
                 </el-form-item>
              </el-col>
           </el-row>
           <el-row>
              <el-col :span="24">
                 <el-form-item label="副本数" prop="replicaNum">
                    <el-input-number v-model="form.replicaNum" controls-position="right" :min="0" :max="100" />
                 </el-form-item>
              </el-col>
           </el-row>
           <el-row>
              <el-col :span="24">
                 <el-form-item label="文档数量" prop="docCount">
                    <el-input-number v-model="form.docCount" controls-position="right" :min="0" :max="9007199254740991" />
                 </el-form-item>
              </el-col>
           </el-row>
           <el-row>
              <el-col :span="24">
                 <el-form-item label="别名" prop="alias">
                    <el-input v-model="form.alias" placeholder="请输入别名" maxlength="255" />
                 </el-form-item>
              </el-col>
           </el-row>
           <el-row>
              <el-col :span="24">
                 <el-form-item label="存储数据量" prop="storageSize">
                    <el-input-number v-model="form.storageSize" controls-position="right" :min="0" :max="9007199254740991" />
                 </el-form-item>
              </el-col>
           </el-row>
        </el-form>
        <template #footer>
           <div class="dialog-footer">
              <el-button type="primary" @click="submitForm">确 定</el-button>
              <el-button @click="cancel">取 消</el-button>
           </div>
        </template>
     </el-dialog>
  </div>
</template>

<script setup name="DocumentIndex">

import {
  listDocumentIndex,
  delDocumentIndex,
  getDocumentIndex,
  updateDocumentIndex,
  addDocumentIndex
} from "@/api/base/search/documentIndex";

const router = useRouter();
const { proxy } = getCurrentInstance();

// 定义变量
const DocumentIndexList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");
const dateRange = ref([]);
const postOptions = ref([]);
const roleOptions = ref([]);

// 列显隐信息
const columns = ref([
  { key: 0, label: `应用名称`, visible: true },
  { key: 1, label: `应用描述`, visible: true },
  { key: 2, label: `表数据量`, visible: true },
  { key: 3, label: `类型`, visible: true },
  { key: 4, label: `应用地址`, visible: true },
  { key: 5, label: `状态`, visible: true },
  { key: 6, label: `更新时间`, visible: true },
  { key: 7, label: `更新时间`, visible: true },
  { key: 8, label: `更新时间`, visible: true },
  { key: 9, label: `更新时间`, visible: true }
]);

const data = reactive({
  form: {},
  queryParams: {
     pageNum: 1,
     pageSize: 10,
     name: undefined,
     appKey: undefined
  },
  rules: {
     name: [{ required: true, message: "名称不能为空", trigger: "blur" }] , 
     domain: [{ required: true, message: "连接不能为空", trigger: "blur" }],
     showName: [{ required: true, message: "类型不能为空", trigger: "blur" }] , 
     remark: [{ required: true , message: "用户名不能为空", trigger: "blur"}],
     appKey: [{ required: true, message: "备注不能为空", trigger: "blur" }] 
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询应用列表 */
function getList() {
  loading.value = true;
  listDocumentIndex(proxy.addDateRange(queryParams.value, dateRange.value)).then(res => {
     loading.value = false;
     DocumentIndexList.value = res.rows;
     total.value = res.total;
  });
};

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
};

/** 重置按钮操作 */
function resetQuery() {
  dateRange.value = [];
  proxy.resetForm("queryRef");
  queryParams.value.deptId = undefined;
  proxy.$refs.deptTreeRef.setCurrentKey(null);
  handleQuery();
};
/** 删除按钮操作 */
function handleDelete(row) {
  const DocumentIndexIds = row.id || ids.value;
  proxy.$modal.confirm('是否确认删除应用编号为"' + DocumentIndexIds + '"的数据项？').then(function () {
     return delDocumentIndex(DocumentIndexIds);
  }).then(() => {
     getList();
     proxy.$modal.msgSuccess("删除成功");
  }).catch(() => { });
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
     id: undefined,
     deptId: undefined,
     DocumentIndexName: undefined,
     nickName: undefined,
     password: undefined,
     phonenumber: undefined,
     status: "0",
     remark: undefined,
  };
  proxy.resetForm("databaseRef");
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
  const DocumentIndexId = row.id || ids.value;
  getDocumentIndex(DocumentIndexId).then(response => {
     form.value = response.data;
     open.value = true;
     title.value = "修改应用";
  });
};

/** 提交按钮 */
function submitForm() {
  proxy.$refs["databaseRef"].validate(valid => {
     if (valid) {
        if (form.value.id != undefined) {
           updateDocumentIndex(form.value).then(response => {
              proxy.$modal.msgSuccess("修改成功");
              open.value = false;
              getList();
           });
        } else {
           addDocumentIndex(form.value).then(response => {
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
