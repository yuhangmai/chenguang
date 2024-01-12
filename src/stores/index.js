// 定义一个store
import { defineStore } from 'pinia';

export const useStore = defineStore('main', {
    // 定义一个state
    state: () => ({
        zuobiao: {
            x: 0,
            y: 0,
            h: 0,
        }
    }),
    actions: {
        setXData(value) {
            this.zuobiao.x = value
        },
        setYData(value) {
            this.zuobiao.y = value
        },
        setHData(value) {
            this.zuobiao.h = value
        },
    },

})