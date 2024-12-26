<template>
  <div class="siderbar">
    <el-menu default-active="1" class="el-menu-vertical" :collapse="isCollapse" @open="handleOpen" @close="handleClose">
      <el-menu-item index="1" @click="jumpTo">
        <i class="fa-solid fa-desktop"></i>
      </el-menu-item>

      <el-tooltip effect="dark" :content="item.desc" v-for="item in menuItems" :key="item.id" placement="right">
          <el-menu-item :index="item.id" @click="openServiceList(item.link)">
            <i :class="item.icon"></i>
          </el-menu-item>
      </el-tooltip>

    </el-menu>

  </div>
</template>

<script setup>

const dialogVisible = ref(false)
const router = useRouter();

// 菜单列表
const menuItems = ref([
  {id:'2' , icon:'fa-solid fa-feather' , link:'/base/search/project/index' , desc:'项目管理'},
  {id:'8' , icon:'fa-solid fa-file-pdf' , link:'/base/search/vectorData/index' , desc:'知识库管理'},
  {id:'10' , icon:'fa-solid fa-user-ninja' , link:'/base/search/memoryData/index' , desc:'记忆库管理'},
  {id:'4' , icon:'fa-brands fa-skype' , link:'/base/search/configuration/index' , desc:'服务配置管理'},
  {id:'3' , icon:'fa-solid fa-masks-theater' , link:'/base/search/document/index' , desc:'全文检索查询'},
  {id:'5' , icon:'fa-brands fa-wordpress' , link:'/base/search/indexes/index' , desc:'数据索引管理'},
  {id:'7' , icon:'fa-solid fa-computer' , link:'/base/search/analyse/index' , desc:'监控管理'},
  {id:'9' , icon:'fa-solid fa-code-pull-request' , link:'/base/search/apiRecord/index' , desc:'请求记录'},
]);

// 打开服务市场
function openServiceList(_path) {
  router.push({ path: _path });
}

// 打开客户配置
function jumpToConstomTheme() {
  router.push({ path: "/dashboard/dashboardTheme" });
}

// 打开首页
function jumpTo() {
  router.push({ path: "/index" });
}

// 打开智能客服
function openSmartService() {
  router.push({ path: "/dashboard/smartService" });
}

</script>

<style lang="scss" scoped>
.el-menu-vertical:not(.el-menu--collapse) {
  width: 65px;
}

.acp-suggest {
  bottom: 20px;
  position: absolute;
}

.siderbar {
  float: left;
  height: 100%;
  width: 64px;
  border-right: 1px solid #e6e6e6;
  padding-top: 40px;
  overflow: hidden;
  background-color: #fff;
  position: fixed;
}
</style>