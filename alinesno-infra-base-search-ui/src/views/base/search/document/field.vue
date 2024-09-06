<template>
  <div class="filter-panel">
    <el-input
      v-model="searchText"
      placeholder="请输入字段名称"
      clearable
      prefix-icon="Search"
      style="width: 100%"
    />
    <div class="section">
      <div class="section-title">已选字段</div>
      <el-scrollbar max-height="300px">
        <ul class="selected-fields">
          <li v-for="(field, index) in selectedFields" :key="index" class="selected-field-item">
            <span>{{ field }}</span>
            <el-button type="text" size="small" @click="removeField(index)">移除</el-button>
          </li>
        </ul>
      </el-scrollbar>
    </div>
    <div class="section">
      <div class="section-title">可用字段</div>
      <el-scrollbar max-height="350px">
        <el-checkbox-group @change="onSelectionChange" v-model="selectedFields">
          <el-checkbox
            v-for="field in filteredFields"
            style="width:100%"
            :key="field"
            :label="field"
            class="available-field-item"
          >{{ field }}</el-checkbox>
        </el-checkbox-group>
      </el-scrollbar>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue';

// 示例字段列表（Kubernetes 日志字段）
const fields = [
  'pod_name',
  'container_name',
  'namespace',
  'node_name',
  'start_time',
  'end_time',
  'log_level',
  'log_message',
  'source',
  'tags',
  'labels',
  'annotations',
  'event_type',
  'reason',
  'involved_object',
  'related_objects',
  'first_seen',
  'last_seen',
  'count',
  'message',
];

// 搜索框绑定的值
const searchText = ref('');

// 已选择的字段列表
const selectedFields = ref([
    '_source'
]);

// 根据搜索值过滤字段
const filteredFields = computed(() => {
  return fields.filter(field => field.includes(searchText.value));
});

// 移除已选择字段的方法
const removeField = (index) => {
  selectedFields.value.splice(index, 1);
};

// 选中事件处理函数
const onSelectionChange = (newVal) => {
  console.log('Selected fields changed:', newVal);
  // 在这里可以执行其他操作，例如更新数据模型或发送请求
};

</script>

<style scoped lang="scss">
.filter-panel {
  display: flex;
  flex-direction: column;
  gap: 10px;

  .section {
    display: flex;
    flex-direction: column;
    gap: 10px;

    .section-title {
      font-size: 14px;
      font-weight: bold;
    }

    .selected-fields,
    .available-field-item {
      list-style: none;
      padding: 0;
      margin: 0;
    }

    .selected-field-item {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 5px;
      border: 0px solid #ccc;
      border-radius: 4px;
      margin-bottom: 0px;
    
      span {
        color: rgb(96, 98, 102);
        font-weight: 500;
        font-size: 14px;
      }
    }
  }
}
</style>