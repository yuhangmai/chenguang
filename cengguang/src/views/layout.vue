<template>
    <div class="common-layout">
        <el-container>
         
            <el-header>
                <!-- 顶部导航 -->
                <div class="title-left">
                    <router-link to="/project">
                        <span class="">工程</span>
                    </router-link>
                    <router-link to="/floating">
                        <span class="">浮运</span>
                    </router-link>
                    <router-link to="/piple">
                        <span class="">沉管</span>
                    </router-link>
                    <router-link to="/help">
                        <span class="">帮助</span>
                    </router-link>
                </div>
            </el-header>
            <!-- 第二行导航 -->
            <div class="second-title">
                <el-button class="custom-button" size="default">
                    <div class="msg">
                        <el-icon>
                            <VideoPlay />
                        </el-icon>
                        <div>暂停</div>
                    </div>
                </el-button>
                <el-button class="custom-button" size="default">
                    <div class="msg">
                        <el-icon>
                            <VideoPause />
                        </el-icon>
                        <div>停止</div>
                    </div>
                </el-button>
                <el-button class="custom-button" size="default" @click="resetMap_1">
                    <div class="msg">
                        <el-icon>
                            <RefreshRight />
                        </el-icon>
                        <div>还原布局</div>
                    </div>
                </el-button>
                <el-button class="custom-button" size="default">
                    <div class="msg">
                        <el-icon>
                            <LocationInformation />
                        </el-icon>
                        <div>角点坐标</div>
                    </div>
                </el-button>
                <el-button class="custom-button" size="default">
                    <div class="msg">
                        <el-icon>
                            <Tools />
                        </el-icon>
                        <div>设备状态</div>
                    </div>
                </el-button>
                <el-button class="custom-button" size="default">
                    <div class="msg">
                        <el-icon>
                            <Lock />
                        </el-icon>
                        <div>旋转锁定</div>
                    </div>
                </el-button>
                <el-button class="custom-button" size="default">
                    <div class="msg">
                        <el-icon>
                            <Cpu />
                        </el-icon>
                        <div>连接</div>
                    </div>
                </el-button>
            </div>

            <!-- 侧边 -->
            <el-container>
                <el-aside width="200px">
                    <asideTable></asideTable>
                </el-aside>
                <el-main>
                    <mainMap ref="mainmap"></mainMap>
                </el-main>
            </el-container>
        </el-container>
    </div>
</template>
  
<script lang="ts" setup>
import asideTable from "./layout/aside.vue"
import mainMap from "./layout/mainMap.vue"
import {ref,inject, onMounted} from "vue"

// 父调用子的方法
const mainmap = ref()

const resetMap_1= () => {
    mainmap.value.resetMap();
}




</script>
<style lang="scss">
.common-layout {
    display: flex;
    height: 100vh;
    width: 100vw;
    

    .el-header,
    .el-footer {
        background-color: #81abe1;
        color: #333;
        text-align: center;
        line-height: 30px;
        height: 40px;
    }

    .el-aside {
        background-color: #D3DCE6;
        color: #333;
        text-align: center;
        // line-height: 200px;
        width: 230px;
    }

    .el-main {
        background-color: #E9EEF3;
        color: #333;
        text-align: center;
        // line-height: 160px;
    }
}

.title-left {
    width: 580px;
    height: 40px;
    gap: 20px; // 间隔调整为适当的值
    margin-left: 0;
    display: flex;
    cursor: pointer;
    /* 添加手型光标 */

    a {
        position: relative;
        color: #fff;
        text-decoration: none;
        font-size: smaller;

        &::after {
            content: '';
            position: absolute;
            width: 100%;
            height: 2px;
            background-color: #fff;
            /* 下划线颜色 */
            bottom: 0;
            left: 0;
            transform: scaleX(0);
            /* 初始时不显示下划线 */
            transform-origin: bottom right;
            transition: transform 0.3s ease;
            /* 添加过渡效果 */
        }

        &:hover::after {
            transform: scaleX(1);
            /* 鼠标悬浮时显示下划线 */
            transform-origin: bottom left;
        }

    }

}

.second-title {
    display: flex;
    margin: 5px;
    padding: 5px;
}

.el-main {
    padding: 0;
}
</style>
  