<template>
    <div class="table-container">
        <div class="table-title">RTK1</div>
        <el-table :data="tableData1" border style="width: 100%" size="small" :show-header="false" class="custom-table">
            <el-table-column prop="date" width="60" />
            <el-table-column prop="name" width="95" />
            <!-- <el-table-column prop="address" width="50" /> -->
            <el-table-column prop="address" width="50">
                <!-- 用小圆圈表示是否正常运行 -->
                <template v-slot="{ row }">
                    <el-button type="danger" size="small" circle v-show="row.address === 'no'"></el-button>
                    <el-button type="success" size="small" circle v-show="row.address === 'yes'"
                        style="margin: 0;"></el-button>
                </template>
            </el-table-column>
        </el-table>
    </div>
    <div class="table-container">
        <div class="table-title">RTK2</div>
        <el-table :data="tableData2" border style="width: 100%" size="small" :show-header="false" class="custom-table">
            <el-table-column prop="date" width="60" />
            <el-table-column prop="name" width="95" />
            <el-table-column prop="address" width="50">
                <!-- 用小圆圈表示是否正常运行 -->
                <template v-slot="{ row }">
                    <el-button type="danger" size="small" circle v-show="row.address === 'no'"></el-button>
                    <el-button type="success" size="small" circle v-show="row.address === 'yes'"
                        style="margin: 0;"></el-button>
                </template>
            </el-table-column>
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
import { ref, onMounted, inject, reactive, nextTick, provide } from "vue"
import axios from 'axios'
import emitter from "@/utils/bus"
// 使用store
import { useStore } from "../../stores/index";
const store = useStore()
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
        name: '0',
        address: 'no',
    },
    {
        date: '2000y',
        name: '0',
        address: 'no',
    },
    {
        date: '2000h',
        name: '0',
        address: 'no',
    },
])
const tableData2 = ref([
    {
        date: '2000x',
        name: '0',
        address: 'no',
    },
    {
        date: '2000y',
        name: '0',
        address: 'no',
    },
    {
        date: '2000h',
        name: '0',
        address: 'no',
    },
    // {
    //     date: '水位',
    //     name: '10.374',
    //     address: 'no',
    // },
    // {
    //     date: '潮位',
    //     name: '10.374',
    //     address: 'no',
    // },
])
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
    // 用于存储每个DTU的最后一次接收数据的时间,判断是否5s内没有收到数据
    let lastDataReceivedTimeDTU1 = Date.now();
    let lastDataReceivedTimeDTU2 = Date.now();


    //判断当前浏览器是否支持WebSocket
    if ('WebSocket' in window) {
        //连接WebSocket节点
        websocket = new WebSocket("ws://localhost:9090/webSocket");
    } else {
        alert('浏览器不支持webSocket');
    }

    // 定时检查是否有数据到达
    // setInterval(() => {
    //     const currentTime = Date.now();
    //     const timeout = 5000; // 5秒

    //     // 检查DTU1
    //     if (currentTime - lastDataReceivedTimeDTU1 > timeout) {
    //         console.log("No data received from DTU1 in the last 5 seconds");
    //         // 执行DTU1相应的逻辑...
    //         tableData1.value[0].address = "no";
    //         tableData1.value[1].address = "no";
    //         tableData1.value[2].address = "no";
    //         // websocket.close();
    //         // websocket = new WebSocket("ws://localhost:9090/webSocket");
    //     }

    //     // 检查DTU2
    //     if (currentTime - lastDataReceivedTimeDTU2 > timeout) {
    //         console.log("No data received from DTU2 in the last 5 seconds");
    //         // 执行DTU2相应的逻辑...
    //         tableData2.value[0].address = "no";
    //         tableData2.value[1].address = "no";
    //         tableData2.value[2].address = "no";
    //         // websocket.close();
    //         // websocket = new WebSocket("ws://localhost:9090/webSocket");
    //     }
    // }, 5000); // 每5秒检查一次

    // 重连websocket
    //setInterval(() => {
    //    websocket.close();
    //     websocket = new WebSocket("ws://localhost:9090/webSocket");
    //     console.log("websocket重连")
    // }, 5000);  // 每5秒检查一次


    // WebSocket连接建立事件
    websocket.onopen = function (event) {
        console.log("WebSocket连接已建立");



        // WebSocket消息接收事件
        websocket.onmessage = function (event) {

            const a = event.data.substring(0, 4);
            nextTick(() => {
                if (a == '0001') {
                    tableData1.value[0].name = event.data.substring(5, 16);
                    store.setXData(tableData1.value[0].name)
                    // 用一个来保证数据传到
                    lastDataReceivedTimeDTU1 = Date.now();
                } else if (a == '0002') {
                    tableData1.value[1].name = event.data.substring(5, 15);
                    store.setYData(tableData1.value[1].name)
                    tableData1.value[0].address = "yes";
                    tableData1.value[1].address = "yes";
                    tableData1.value[2].address = "yes";
                } else if (a == '0003') {
                    tableData1.value[2].name = event.data.substring(5, 11);
                    store.setHData(tableData1.value[2].name)
                    // 第二个RTK
                } else if (a == '0004') {
                    tableData2.value[0].name = event.data.substring(5, 16);
                    store.setXTwoData(tableData2.value[0].name)
                    lastDataReceivedTimeDTU2 = Date.now();
                } else if (a == '0005') {
                    tableData2.value[0].address = "yes";
                    tableData2.value[1].address = "yes";
                    tableData2.value[2].address = "yes";
                    tableData2.value[1].name = event.data.substring(5, 15);
                    store.setYTwoData(tableData2.value[1].name)
                } else if (a == '0006') {
                    tableData2.value[2].name = event.data.substring(5, 11);
                    store.setHTwoData(tableData2.value[2].name)
                }
                // else if (a == '0008') {
                //     tableData2.value[0].address = "yes";
                //     tableData2.value[1].address = "yes";
                //     tableData2.value[2].address = "yes";
                // } else if (a == '0007') {
                //     tableData1.value[0].address = "yes";
                //     tableData1.value[1].address = "yes";
                //     tableData1.value[2].address = "yes";
                // }
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

    // websocket报错
    websocket.onerror = function (event) {
        console.error("WebSocket连接发生错误:", event);
    };
    // websocket关闭
    websocket.onclose = function (event) {
        console.log("WebSocket连接关闭:", event);
    };


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

.el-button--small {
    --el-button-size: 15px
}
</style>
  