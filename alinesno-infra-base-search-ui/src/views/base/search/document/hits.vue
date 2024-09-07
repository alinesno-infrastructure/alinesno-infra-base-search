<template>
    <div style="margin-bottom:20px">
       <el-row>
            <div id="echarts-bar2-chart" :style="{ width: '100%', height: '150px' }"></div>
       </el-row>
    </div>
</template>

<script setup>
import * as echarts from "echarts";

import { getSearchHits } from '@/api/base/search/documents';

/// 声明定义一下echart
const echart = echarts;

function drawBar(dataItems , timeSegments) {
  let barChart = echart.init(
    document.getElementById("echarts-bar2-chart")
  );

  var lineOption = {
    title: {
      text: "搜索量统计 76824 hits" , 
      show: true  , 
      x: 'right',
      textStyle: {
        color: '#222' , 
        fontSize: 14,
        fontStyle: 'normal',
        fontWeight: 'normal',
      }
    }, 
    tooltip: {
        trigger: 'axis',
        axisPointer: {
        type: 'shadow'
        }
    },
    grid: {
        left: '1%',
        right: '1%',
        top: '20%',
        bottom: '3%',
        containLabel: true
    },
    xAxis: [
        {
        type: 'category',
        data: timeSegments,
        axisTick: {
            alignWithLabel: true
        }
        }
    ],
    yAxis: [
        {
        type: 'value'
        }
    ],
    series: [
        {
          name: 'Direct',
          type: 'bar',
          barWidth: '60%',
          data: dataItems , 
        }
    ]
 };

  barChart.setOption(lineOption);
}

onMounted(() => {

  getSearchHits().then(response => {
    let dataItems = response.dataItems;
    let timeSegments = response.timeSegments;

    drawBar(dataItems, timeSegments);
  })
});

onUnmounted(() => {
  echart.dispose;
});
</script>

<style scoped lang="scss">
#echarts-bar2-chart {
  width: 100%;
  height: 400px;
}
</style>