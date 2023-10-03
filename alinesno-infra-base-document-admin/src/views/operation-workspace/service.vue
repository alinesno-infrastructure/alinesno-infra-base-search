<template>
  <div>
    
    <el-row class="acp-dashboard-panel" :gutter="20">

      <el-col class="panel-col" :span="19">
        <div class="grid-content">
          <div class="panel-header">
            <div class="header-title"><i class="fa-solid fa-file-waveform"></i> 自动运维套件</div>
          </div>
          <div class="panel-body acp-height-auto" style="padding: 5px">
            <div class="acp-app-list">
              <ul>
                <li class="app-items" v-for="item in apps" :key="item" 
                  style="width:calc(25% - 10px);
                         height:80px;
                         border:1px solid #ebebeb;
                         margin:5px;
                         padding: 10px;
                         border-radius: 3px;">
                  <div class="app-icon">
                    <img :src="item.icon" :alt="item.name" />
                  </div>
                  <div class="app-info">
                    <div class="app-item-title">{{ item.name }}</div>
                    <div class="app-item desc">{{ item.desc }}</div>
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
            <div class="header-title"><i class="fa-solid fa-user-nurse"></i> 运维资产套件</div>
          </div>
          <div class="panel-body acp-height-auto">
            <ul class="panel-item-text">
              <li style="width:50%;padding:4px;border-bottom: 0px;" v-for="item in opertionAssets" :key="item.id">
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

const opertionAssets = ref([
  {id:'1' , title:'运维脚本' , count:45} ,
  {id:'2' , title:'服务资源' , count:145} ,
  {id:'3' , title:'持续集成' , count:65} ,
  {id:'4' , title:'自动配置' , count:85} ,
  {id:'5' , title:'配置管理' , count:45} ,
  {id:'6' , title:'安全监控' , count:45} ,
])

const apps = ref([
  {icon:'https://d1by4p17n947rt.cloudfront.net/icon/d88319dfa5d204f019b4284149886c59-7d586ea82f792b61a8c87de60565133d.svg' , name:'分布式链路跟踪' , desc:'基于OpenTelemetry标准构建的Trace服务'},
  {icon:'https://d1by4p17n947rt.cloudfront.net/icon/9da5a168cf8194c8ee5ed192a443d563-674375b53bc8ae94f48cfdb5c81e8363.svg' , name:'智能异常分析' , desc:'智能时序异常发现、文本异常模式挖掘、自定义根因分析诊断平台'},
  {icon:'https://d1by4p17n947rt.cloudfront.net/icon/d88319dfa5d204f019b4284149886c59-7d586ea82f792b61a8c87de60565133d.svg' , name:'容器云管理服务' , desc:'网络请求日志分析，提供详细的请求情况和流量监控等'},
  {icon:'https://d1by4p17n947rt.cloudfront.net/icon/d88319dfa5d204f019b4284149886c59-7d586ea82f792b61a8c87de60565133d.svg' , name:'自动化操作服务' , desc:'基于OpenTelemetry标准构建的Trace服务'},
  {icon:'https://d1by4p17n947rt.cloudfront.net/icon/a5ffe5487f62ef75d8e5cf78c18525a5-d4867f9d4adcd749f0c5aff987232847.svg' , name:'全栈监控' , desc:'覆盖Web应用、h5页面、小程序、监控页面访问速度、JS错误、资源监控、端到端链路'},
  {icon:'https://d1by4p17n947rt.cloudfront.net/icon/6e9e16955bd76c1d3a9996e0d6d3ad15-04cfc8c62f597690680d948b366980cf.svg' , name:'智能异常分析' , desc:'提供IT系统监控、K8s监控、中间件监控、融合分析、智能告警、丰富可视化等能力'},
  {icon:'https://d1by4p17n947rt.cloudfront.net/icon/9da5a168cf8194c8ee5ed192a443d563-674375b53bc8ae94f48cfdb5c81e8363.svg' , name:'分布式配置中心' , desc:'智能时序异常发现、文本异常模式挖掘、自定义根因分析诊断平台'},
  {icon:'https://d1by4p17n947rt.cloudfront.net/icon/a5ffe5487f62ef75d8e5cf78c18525a5-d4867f9d4adcd749f0c5aff987232847.svg' , name:'审计日志服务' , desc:'覆盖Web应用、h5页面、小程序、监控页面访问速度、JS错误、资源监控、端到端链路'},
  {icon:'https://d1by4p17n947rt.cloudfront.net/icon/6e9e16955bd76c1d3a9996e0d6d3ad15-04cfc8c62f597690680d948b366980cf.svg' , name:'持续集成服务' , desc:'提供IT系统监控、K8s监控、中间件监控、融合分析、智能告警、丰富可视化等能力'},
  {icon:'https://d1by4p17n947rt.cloudfront.net/icon/d88319dfa5d204f019b4284149886c59-7d586ea82f792b61a8c87de60565133d.svg' , name:'容器云管理服务' , desc:'网络请求日志分析，提供详细的请求情况和流量监控等'},
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