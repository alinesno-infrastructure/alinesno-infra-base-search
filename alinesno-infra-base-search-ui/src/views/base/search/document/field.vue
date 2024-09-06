<template>
    <div>
        <div class="head-container">
            <el-input
                v-model="deptName"
                placeholder="请输入类型名称"
                clearable
                prefix-icon="Search"
                style="margin-bottom: 20px"
            />
        </div>
        <div class="head-container">
            <el-tree
                :data="deptOptions"
                :props="{ label: 'label', children: 'children' }"
                :expand-on-click-node="false"
                :filter-node-method="filterNode"
                ref="deptTreeRef"
                node-key="id"
                highlight-current
                @node-click="handleNodeClick"
            />
                <!-- default-expand-all -->
        </div>
    </div>
</template>

<script>
</script>

<style scoped lang="scss">
</style>


