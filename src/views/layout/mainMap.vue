<template>
  <div style="height: 100%; width: 100%; position: relative;" id="map">
  </div>
</template>

<script setup >
import 'ol/ol.css';
import { onMounted, ref, nextTick, inject } from 'vue';
import { Map, View } from 'ol';
import TileLayer from 'ol/layer/Tile';
import ImageLayer from 'ol/layer/Image';
import VectorLayer from 'ol/layer/Vector';  // 添加这个导入语句
import { OSM, ImageStatic, Vector } from 'ol/source';
import { LineString, Point } from 'ol/geom';
import Feature from 'ol/Feature';
import { Style, Stroke, Fill, Circle } from 'ol/style';  // 添加这个导入语句
import proj4 from 'proj4';
import 'ol'; // Add this line to import OpenLayers
import Polygon from 'ol/geom/Polygon';
import Text from 'ol/style/Text';
import emitter from "@/utils/bus"
import { register } from "ol/proj/proj4";
import { useStore } from "../../stores/index";

const store = useStore()


const map = ref(null);
// const initialCenter = [113.2205701, 23.11502356];
const initialCenter = [113.349, 23.164];
const initialZoom = 18;

const x2000 = 0;
const y2000 = 0;
const h2000 = 0;

var wgs84 = '+proj=longlat +ellps=WGS84 +datum=WGS84 +no_defs  ';
var cgcs2000 = '+proj=tmerc +lat_0=0 +lon_0=114 +k=1 +x_0=500000 +y_0=0 +ellps=GRS80 +units=m +no_defs';



onMounted(() => {
  proj4.defs("EPSG:4490", "+proj=longlat +ellps=GRS80 +no_defs");
  register(proj4);


  // 创建地图
  map.value = new Map({
    target: 'map',
    layers: [
      new ImageLayer({
        source: new ImageStatic({
          //url: 'http://47.236.64.84:8086/map2_x1_y1.png', 
          url: 'src/assets/六一运动场_x0_y0.jpg',
          imageExtent: [113.343, 23.159, 113.354, 23.1691],
        }),
      }),

    ],
    view: new View({
      projection: 'EPSG:4326',
      // projection: 'EPSG:4490',
      center: initialCenter,
      zoom: initialZoom,
      minZoom: 17, // 设置最小缩放级别
      maxZoom: 20, // 设置最大缩放级别
      // extent: [113.209, 23.1052, 113.231, 23.1254], // 设置地图视图的边界范围
    })
  });


  // // 创建轨迹线
  const trackCoordinates = [
    [113.215987, 23.1117125],
    [113.215997, 23.1118125],
    [113.216107, 23.1119125],
    [113.216217, 23.1120125],
    [113.216327, 23.1121125],
    [113.216437, 23.1122125],
    // 添加更多坐标点...
  ];



  // const trackLine = new LineString(trackCoordinates);
  // const trackFeature = new Feature(trackLine);
  // const trackLayer = new VectorLayer({  
  //   source: new Vector({
  //     features: [trackFeature]
  //   }),
  //   style: new Style({
  //     stroke: new Stroke({
  //       color: 'blue',
  //       width: 2
  //     })
  //   })
  // });

  // // 将轨迹线添加到地图
  // map.value.addLayer(trackLayer);

  // 创建矩形
  // const rectangleCoordinates = [
  //   [113.21720, 23.11440],
  //   [113.21780, 23.11400],
  //   [113.21828, 23.11453],
  //   [113.21775, 23.11490],
  //   // [113.21727, 23.11455],
  // ];
  // const rectangle = new Polygon([rectangleCoordinates]);
  // const rectangleFeature = new Feature(rectangle);
  // // 设置矩形样式
  // rectangleFeature.setStyle(new Style({
  //   fill: new Fill({
  //     color: 'gray',  // 设置填充颜色和透明度
  //   }),
  //   stroke: new Stroke({
  //     color: 'black',  // 设置边框颜色
  //     width: 2,        // 设置边框宽度
  //   }),
  //   text: new Text({
  //     text: '广州城建中心', // 文本内容
  //     font: '10px Calibri,sans-serif', // 字体和大小
  //     fill: new Fill({
  //       color: 'black', // 文本颜色
  //     }),
  //     offsetX: 0, // X轴偏移
  //     offsetY: -5, // Y轴偏移，负值表示文字在矩形上方
  //     rotation: -Math.PI /4,
  //   }),
  // }));
  // // 创建矩形图层
  // const rectangleLayer = new VectorLayer({
  //   source: new Vector({
  //     features: [rectangleFeature],
  //   }),
  // });
  // // 将矩形图层添加到地图
  // map.value.addLayer(rectangleLayer);
  // 点的创建------------------------------------------------
  // 创建移动点
  // const movePoint = new Point([113.349, 23.164]);
  const movePoint = new Point(proj4(cgcs2000, wgs84, [433404.633706, 2562975.255210475]));
  // console.log(proj4(cgcs2000,wgs84,[433404.633706, 2562975.255210475]))

  const movePointFeature = new Feature(movePoint);
  movePointFeature.setStyle(new Style({
    image: new Circle({
      radius: 8,
      fill: new Fill({ color: 'red', width: 1, height: 1 }),
      stroke: new Stroke({ color: 'white', width: 2 }),
    }),
  }));

  // 创建移动点图层
  const movePointLayer = new VectorLayer({
    source: new Vector({
      features: [movePointFeature],
    }),
  });

  // 将移动点图层添加到地图
  map.value.addLayer(movePointLayer);

  // 定义移动点的动画函数
  const animateMovePoint = () => {
    // let index = 0;
    const interval = setInterval(() => {
      // const currentCoordinate = proj4(cgcs2000, wgs84, [store.zuobiao.y, store.zuobiao.x]);
      // movePoint.setCoordinates(proj4(cgcs2000, wgs84, [store.zuobiao.y, store.zuobiao.x]));

      // console.log(store.zuobiao.x)
      // console.log(store.zuobiao.y)
      movePoint.setCoordinates(proj4(cgcs2000, wgs84, [parseFloat(store.zuobiao.y), parseFloat(store.zuobiao.x)]));
      console.log(proj4(cgcs2000, wgs84, [parseFloat(store.zuobiao.y), parseFloat(store.zuobiao.x)]));
      console.log("实时移动2000坐标" + store.zuobiao.y, store.zuobiao.x);

    }, 1000); // 每1秒移动一次
  };

  // 调用动画函数，使点移动
  // animateMovePoint();

  // // 模拟实时运动
  // const simulateRealTimeMovement = () => {
  //   let index = 0;
  //   setInterval(() => {
  //     index = (index + 1) % trackCoordinates.length;
  //     const currentCoordinate = trackCoordinates[index];
  //     movePoint.setCoordinates(currentCoordinate);
  //   }, 1000); // 更新频率，每秒一次
  // };

  // // 调用实时运动函数
  // simulateRealTimeMovement();

});


// 将cgcs2000坐标转换成wgs84坐标
// console.log(proj4(cgcs2000, wgs84, [419685.616, 2557124.796]));
// console.log(proj4(cgcs2000, wgs84, [419831.381, 2557422.058]));
// console.log(proj4(cgcs2000, wgs84, [420634.167, 2557626.591]));
// console.log(proj4(cgcs2000, wgs84, [420675.500, 2557499.310]));

// console.log(proj4(wgs84,cgcs2000, [113.2159876, 23.1117114]));
// console.log(proj4(wgs84,cgcs2000, [113.2173949, 23.1144029]));
// console.log(proj4(wgs84,cgcs2000, [113.2252205,  23.1162883]));
// console.log(proj4(wgs84,cgcs2000, [113.2256306, 23.1151411]));

// console.log(proj4(wgs84, cgcs2000, [113.349113192, 23.165922501]));
// console.log(proj4(cgcs2000, wgs84, [433347.980, 2563013.231])); 




// 添加还原地图的方法
const resetMap = () => {
  // 还原地图初始状态
  map.value.getView().setCenter(initialCenter);
  map.value.getView().setZoom(initialZoom);
  console.log("还原地图")
};

// 输出组件的方法，让外部组件可以调用
defineExpose({
  resetMap,
})



</script>

<style></style>
