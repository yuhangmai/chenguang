<template>
    <div class="table-container">
        <div class="table-title">间距</div>
        <el-table :data="tableData1" border style="width: 100%" size="small" :show-header="false" class="custom-table">
            <el-table-column prop="date" width="60" />
            <el-table-column prop="name" width="100" />
            <el-table-column prop="address" width="50" />
        </el-table>
    </div>
    <div class="table-container">
        <div class="table-title">姿态</div>
        <el-table :data="tableData2" border style="width: 100%" size="small" :show-header="false" class="custom-table">
            <el-table-column prop="date" width="85" />
            <el-table-column prop="name" width="60" />
            <el-table-column prop="address" width="50" />
        </el-table>
    </div>
    <div class="table-container">
        <div class="table-title">干弦</div>
        <el-table :data="tableData3" border style="width: 100%" size="small" :show-header="false" class="custom-table">
            <el-table-column prop="date" width="85" />
            <el-table-column prop="name" width="60" />
            <el-table-column prop="address" width="50" />
        </el-table>
    </div>
    <div class="table-container">
        <div class="table-title">其他</div>
        <el-table :data="tableData4" border style="width: 100%" size="small" :show-header="false" class="custom-table">
            <el-table-column prop="date" width="85" />
            <el-table-column prop="name" width="60" />
            <el-table-column prop="address" width="50" />
        </el-table>
    </div>
</template>
  
<script lang="ts" setup>
import { ref, onMounted, inject, reactive, nextTick } from "vue"
import axios from 'axios'
const tableData1 = ref([
    // {
    //     date: 'A1距边坡',
    //     name: '21.997',
    //     address: 'No',
    // },
    // {
    //     date: 'A2距边坡',
    //     name: '21.997',
    //     address: 'No',
    // },
    // {
    //     date: 'B1距边坡',
    //     name: '21.997',
    //     address: 'No',
    // },
    // {
    //     date: 'B2距边坡',
    //     name: '21.997',
    //     address: 'No',
    // },
    {
        date: '2000x',
        name: '21.997',
        address: 'No',
    },
    {
        date: '2000y',
        name: '21.997',
        address: 'No',
    },
    {
        date: '2000h',
        name: '21.997',
        address: 'No',
    },
])
const tableData2 = [
    {
        date: '距航道底',
        name: '10.374',
        address: 'No',
    },
    {
        date: '横倾角度',
        name: '0.074',
        address: 'No',
    },
    {
        date: '纵倾角度',
        name: '10.374',
        address: 'No',
    },
    {
        date: '水位',
        name: '10.374',
        address: 'No',
    },
    {
        date: '潮位',
        name: '10.374',
        address: 'No',
    },
]
const tableData3 = [
    {
        date: '干弦A1',
        name: '10.374',
        address: 'No',
    },
    {
        date: '干弦A2',
        name: '10.374',
        address: 'No',
    },
    {
        date: '干弦B1',
        name: '10.374',
        address: 'No',
    },
    {
        date: '干弦B2',
        name: '10.374',
        address: 'No',
    },
]
const tableData4 = [
    {
        date: '浮运速度',
        name: '10.374',
        address: 'No',
    },
    {
        date: '方位角',
        name: '10.374',
        address: 'No',
    },
    {
        date: '已浮距离',
        name: '10.374',
        address: 'No',
    },
    {
        date: '端头距离',
        name: '10.374',
        address: 'No',
    },
]
// const $axios = inject("$axios");

onMounted(() => {
    let websocket = null;
    let host = document.location.host;
    //判断当前浏览器是否支持WebSocket
    if ('WebSocket' in window) {
        //连接WebSocket节点
        websocket = new WebSocket("ws://localhost:9090/webSocket");
    } else {
        alert('浏览器不支持webSocket');
    }

    // WebSocket连接建立事件
    websocket.onopen = function (event) {
        console.log("WebSocket连接已建立");

        // 使用Vue的响应式API更新数据，确保在DOM更新之后再进行
        // $nextTick 用于确保在 DOM 更新之后执行回调函数


        // WebSocket消息接收事件
        websocket.onmessage = function (event) {
            console.log(event.data);
            // tableData1.value[0].name = event.data
            const a = event.data.substring(0, 4);
            nextTick(() => {
                if (a == '0001') {
                    tableData1.value[0].name = event.data.substring(5,16);
                } else if ( a == '0002' ) {
                    tableData1.value[1].name = event.data.substring(5,15);
                } else if ( a == '0003' ) {
                    tableData1.value[2].name = event.data.substring(5,11);
                }

                // tableData1.value[0].name = event.data;
                // tableData1.value[1].name = event.data;
            });
        };

        // 在此处发送消息，示例：
        // websocket.send("Hello, WebSocket!");
    };
    // setTimeout(() => {
    //     tableData1.value[0].name = '22.000'; // 更新数据
    // }, 3000); // 3000毫秒，即3秒

    // axios.get("http://localhost:9090/map").then((res) => {
    //     console.log(res);
    // });

})


</script>

<style>
.table-container {
    /* width: 100%; */
    background-color: aliceblue;
    border: 1px solid #ddd;
    /* 添加边框 */
    padding: 10px;
    /* 增加内边距 */
}

.table-title {
    font-size: small;
    color: blue;
    /* 设置标题颜色为蓝色 */
    text-align: left;
    /* 靠左对齐 */
    margin-left: 20px;
}
</style>
  