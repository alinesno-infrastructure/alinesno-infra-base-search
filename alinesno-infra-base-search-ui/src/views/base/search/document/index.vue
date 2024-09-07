<template>
  <div class="app-container">
     <el-row :gutter="20">
          <!--类型数据-->
         <el-col :span="3" :xs="24">
            <DocumentField /> 
         </el-col>

        <!--应用数据-->
        <el-col :span="21" :xs="24">
           <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="100px">
              <el-form-item label="索引名称" prop="name">
                 <!-- <el-input v-model="queryParams.name" placeholder="请输入索引名称" clearable style="width: 540px" @keyup.enter="handleQuery" /> -->
                  <el-select
                     v-model="queryParams.name"
                     filterable
                     remote
                     reserve-keyword
                     placeholder="请输入索引关键字"
                     remote-show-suffix
                     :remote-method="remoteMethod"
                     :loading="loadingIndexQuery"
                     style="width: 540px"
                     >
                     <el-option
                        v-for="item in indexOptions"
                        :key="item.value"
                        :label="item.label"
                        :value="item.value"
                     />
                  </el-select>
              </el-form-item>
              <el-form-item>
                 <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
                 <el-button icon="Refresh" @click="resetQuery">重置</el-button>
              </el-form-item>
           </el-form>

               <SearchHits />

           <el-table v-loading="loading" :data="DocumentList" @selection-change="handleSelectionChange">
              <el-table-column type="index" label="序号" width="50" align="center" />

              <el-table-column type="expand" width="30px">
               <template #default="props">
                  <div m="4" style="margin-left:100px">
                     <json-viewer
                        :value="props.row"
                        :expand-depth=5
                        copyable
                        boxed
                        sort>
                     </json-viewer>
                  </div>
                  </template>
               </el-table-column>

              <!-- 业务字段-->
              <el-table-column label="时间" align="left" width="150" key="timestamp" prop="timestamp" v-if="columns[0].visible">
                 <template #default="scope">
                  <div>
                     {{ scope.row.timestamp }}
                  </div>
                 </template>
              </el-table-column>
              <el-table-column label="文档" align="left" key="domain" prop="domain" v-if="columns[3].visible">
                 <template #default="scope">
                  {{ scope.row }}
                 </template>
              </el-table-column>

              <!-- 操作字段  -->
              <el-table-column label="操作" align="center" width="150" class-name="small-padding fixed-width">
                 <template #default="scope">
                    <el-tooltip content="修改" placement="top" v-if="scope.row.DocumentId !== 1">
                       <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                          v-hasPermi="['system:Document:edit']"></el-button>
                    </el-tooltip>
                    <el-tooltip content="删除" placement="top" v-if="scope.row.DocumentId !== 1">
                       <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)"
                          v-hasPermi="['system:Document:remove']"></el-button>
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
                 <el-form-item label="应用全名" prop="name">
                    <el-input v-model="form.name" placeholder="请输入应用名称" maxlength="50" />
                 </el-form-item>
              </el-col>
           </el-row>
           <el-row>
              <el-col :span="24">
                 <el-form-item label="简称" prop="showName">
                    <el-input v-model="form.showName" placeholder="请输入类型" maxlength="50" />
                 </el-form-item>
              </el-col>
           </el-row>
           <el-row>
              <el-col :span="24">
                 <el-form-item label="所属领域" prop="domain">
                    <el-input v-model="form.domain" placeholder="请输入所属领域" maxlength="128" />
                 </el-form-item>
              </el-col>
           </el-row>
           <el-row>
              <el-col :span="24">
                 <el-form-item label="应用描述" prop="remark">
                    <el-input v-model="form.remark" placeholder="请输入连接用户名" maxlength="30" />
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

<script setup name="Document">

import JsonViewer from 'vue-json-viewer'

import {
  listDocument,
  delDocument,
  getDocument,
  updateDocument,
  addDocument
} from "@/api/base/search/documents";

import DocumentField from './field.vue'
import SearchHits from './hits.vue'

const router = useRouter();
const { proxy } = getCurrentInstance();

// 定义变量
const DocumentList = ref([]);
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
  { key: 6, label: `更新时间`, visible: true }
]);

// 搜索索引条件
const indexOptions = ref([]);
const loadingIndexQuery = ref(false);

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

const states = [
  'Alabama',
  'Alaska',
  'Arizona',
  'Arkansas',
  'California',
  'Colorado',
  'Connecticut',
  'Delaware',
  'Florida',
  'Georgia',
  'Hawaii',
  'Idaho',
  'Illinois',
  'Indiana',
  'Iowa',
  'Kansas',
  'Kentucky',
  'Louisiana',
  'Maine',
  'Maryland',
  'Massachusetts',
  'Michigan',
  'Minnesota',
  'Mississippi',
  'Missouri',
  'Montana',
  'Nebraska',
  'Nevada',
  'New Hampshire',
  'New Jersey',
  'New Mexico',
  'New York',
  'North Carolina',
  'North Dakota',
  'Ohio',
  'Oklahoma',
  'Oregon',
  'Pennsylvania',
  'Rhode Island',
  'South Carolina',
  'South Dakota',
  'Tennessee',
  'Texas',
  'Utah',
  'Vermont',
  'Virginia',
  'Washington',
  'West Virginia',
  'Wisconsin',
  'Wyoming',
]
const remoteMethod = () => {
   loadingIndexQuery.value = true
   setTimeout(() => {

      indexOptions.value = states.map((item) => {
         return { value: `value:${item}`, label: `label:${item}` }
      }) 
      loadingIndexQuery.value = false
   })
}

/** 查询应用列表 */
function getList() {
  loading.value = true;
  listDocument(proxy.addDateRange(queryParams.value, dateRange.value)).then(res => {
     loading.value = false;
     DocumentList.value = res.rows;
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
  const DocumentIds = row.id || ids.value;
  proxy.$modal.confirm('是否确认删除应用编号为"' + DocumentIds + '"的数据项？').then(function () {
     return delDocument(DocumentIds);
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
     DocumentName: undefined,
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
  const DocumentId = row.id || ids.value;
  getDocument(DocumentId).then(response => {
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
           updateDocument(form.value).then(response => {
              proxy.$modal.msgSuccess("修改成功");
              open.value = false;
              getList();
           });
        } else {
           addDocument(form.value).then(response => {
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
