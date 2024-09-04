<template>
  <div>
    <el-row class="acp-dashboard-panel" :gutter="20">
      <el-col class="panel-col" :span="19">
        <div class="grid-content">
          <div class="panel-header">
            <div class="header-title"><i class="fa-solid fa-link"></i> 业务最新巡检</div>
          </div>
          <div class="panel-body" style="height: auto;position: relative;">
              <div class="direct-box">
                <ul>
                  <li class="box-item" v-for="item in directContent" :key="item">
                    <div class="dire-panel">
                      <div class="panel-title">
                        <i :class="item.icon" :alt="item.name" />
                        {{ item.name }} 
                      </div>
                      <div class="panel-describe">
                        {{ item.desc }}
                      </div>
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
            <div class="header-title"><i class="fa-solid fa-link"></i> 构建解决方案</div>
          </div>
          <div class="panel-body" style="height: auto;position: relative;">
            <div class="acp-app-list">
              <ul>
                <li class="app-items" style="width:100%" v-for="item in useScenarios" :key="item">
                  <div class="app-icon">
                    <i :class="item.icon" :alt="item.name" />
                  </div>
                  <div class="app-info">
                    <div class="app-item-title">{{ item.name }} 
                    </div>
                    <div class="app-item desc">{{ item.desc }}</div>
                  </div>
                </li>
              </ul>
            </div>
          </div>

        </div>
      </el-col>
    </el-row>

  </div>
</template>

<script setup>

const directContent = ref([
  { icon: 'fas fa-search', name: '高效检索体验', desc: '海量数据支持毫秒级响应，快速返回搜索结果 (高性能)'},
  { icon: 'fas fa-brain', name: '语义理解和向量搜索', desc: '利用语义分析技术，提供基于内容理解的精确搜索结果 (高相关性)'},
  { icon: 'fas fa-expand-arrows-alt', name: '高度可扩展性', desc: '轻松应对数据量增长，支持水平扩展以满足不断变化的需求 (灵活性)'},
  { icon: 'fas fa-database', name: '灵活的数据模型', desc: '兼容多种数据格式，支持结构化与非结构化数据的统一管理 (兼容性)'},
  { icon: 'fas fa-code-branch', name: '易于集成', desc: '提供丰富的API接口，便于与其他系统和服务无缝对接 (易用性)'}
]);

const useScenarios = ref([
  {
    icon: 'fas fa-search',
    name: '企业知识库搜索',
    desc: '员工可以在企业内部知识库中快速查找文档，提高工作效率。'
  },
  {
    icon: 'fas fa-chart-line',
    name: '市场数据分析',
    desc: '通过平台对大量市场报告进行快速分析，提取关键信息用于决策支持。'
  },
  {
    icon: 'fas fa-comments',
    name: '社交媒体内容分析',
    desc: '使用平台分析社交媒体上的用户评论和帖子，了解公众情绪和社会趋势。'
  },
]);

</script>