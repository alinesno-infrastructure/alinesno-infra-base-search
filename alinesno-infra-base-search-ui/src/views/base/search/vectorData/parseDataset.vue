<template>
    <div class="app-container" >
          <el-page-header @back="goBack">
            <template #content>
                <span class="text-large font-600 mr-3" style="font-size: 16px;"> 配置知识库数据集</span>
            </template>
        </el-page-header>

        <div style="margin-top:20px">
            <el-tabs :tab-position="tabPosition" style="height: calc(100vh - 150px)" class="demo-tabs">
                <el-tab-pane style="height: 100%;" label="数据源管理">
                    <DatasetList />
                </el-tab-pane>
                <el-tab-pane label="向量性测试">
                    <DatasetSearch />
                </el-tab-pane>
                <el-tab-pane label="知识库配置">
                    <DatasetConfig />
                </el-tab-pane>
            </el-tabs>
        </div>
    </div>
</template>

<script setup>
import { ref } from 'vue'

import DatasetList from './datasetList';
import DatasetConfig from './datasetConfig';
import DatasetSearch from './datasetSearch';

const tabPosition = ref('top')
const router = useRouter();

const goBack = () => {
    console.log('go back')
    let path = '/base/search/vectorData/index';
    router.push(path)
}
</script>

<style lang="scss" scoped >
.demo-tabs > .el-tabs__content {
  padding: 32px;
  color: #6b778c;
  font-size: 32px;
  font-weight: 600;
}

.el-tabs--right .el-tabs__content,
.el-tabs--left .el-tabs__content {
  height: 100%;
}
</style>