<template>
  <div>
    
    <el-row class="acp-dashboard-panel" :gutter="20">

      <el-col class="panel-col" :span="19">
        <div class="grid-content">
          <div class="panel-header">
            <div class="header-title"><i class="fa-solid fa-file-waveform"></i> 最新知识库列表</div>
          </div>
          <div class="panel-body acp-height-auto" style="padding: 5px">
            <div class="acp-app-list">
              <ul>
                <li class="app-items" v-for="item in knowledgeBases" :key="item" 
                  style="width:calc(25% - 10px);
                         height:80px;
                         border:1px solid #ebebeb;
                         margin:5px;
                         padding: 10px;
                         border-radius: 3px;">
                  <div class="app-icon" style="font-size: 20px;color: #3b5998;">
                    <i :class="item.icon" :alt="item.name" />
                  </div>
                  <div class="app-info">
                    <div class="app-item-title">{{ item.name }}</div>
                    <div class="app-item desc">{{ truncateString(item.desc,30) }}</div>
                  </div>
                </li>
              </ul>
            </div>
          </div>
        </div>
      </el-col>

      <el-col :span="5">
        <div class="grid-content">
          <div class="panel-header">
            <div class="header-title"><i class="fa-solid fa-user-nurse"></i> 检索资产概览</div>
          </div>
          <div class="panel-body acp-height-auto">
            <ul class="panel-item-text">
              <li style="width:50%;padding:4px;border-bottom: 0px;" v-for="item in systemMetrics" :key="item.id">
                <div class="item-health-box">
                  <div class="item-health-title">{{ item.title }}</div>
                  <div class="item-health-count">{{ item.count }}</div>
                </div>
              </li>
            </ul>
          </div>
        </div>
      </el-col>

    </el-row>
  </div>
</template>

<script setup>

const systemMetrics = ref([
  { id: '1', title: '向量数据库数量', count: 5 },
  { id: '2', title: '文档索引数量', count: 15000 },
  { id: '3', title: '每日查询次数', count: 25000 },
  { id: '5', title: '平均检索时间', count: 50 }, // 单位：毫秒
  { id: '7', title: '存储容量', count: 2000 }, // 单位：GB
]);

const knowledgeBases = ref([
  { icon: 'fas fa-file-alt', name: '技术文档中心', desc: '集中管理各类技术文档，如API文档、SDK指南、架构设计等，便于工程师查找学习资料。'},
  { icon: 'fas fa-server', name: 'IT资产数据库', desc: '记录公司所有IT资产的信息，包括硬件设备、软件许可、网络配置等，方便资产管理。' },
  { icon: 'fas fa-clipboard-list', name: '项目管理知识库', desc: '收集项目管理的最佳实践、模板文件和进度报告，协助项目经理更好地规划和执行项目。' },
  { icon: 'fas fa-chart-bar', name: '市场研究报告', desc: '汇总市场调研报告、竞品分析和行业趋势，为企业制定战略计划提供数据支持。' },
  { icon: 'fas fa-users', name: '人力资源政策库', desc: '整理公司的人力资源政策、员工手册和培训材料，便于员工了解公司规章制度。'},
  { icon: 'fas fa-dollar-sign', name: '财务报表库', desc: '存储公司的财务报告、预算文件和税务记录，帮助财务部门进行合规管理和审计。'},
  { icon: 'fas fa-question-circle', name: '客户支持FAQ', desc: '整理客户常问问题及其解答，通过搜索引擎快速定位答案，提升客户服务体验。'}
]);

</script>

<style lang="scss" scoped>
.item-health-title{
  margin-bottom: 5px !important;
}
.item-health-count{
  margin-bottom: 5px;
}

</style>