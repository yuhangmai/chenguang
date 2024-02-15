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
import XYZ from 'ol/source/XYZ';
import { LineString, Point } from 'ol/geom';
import Feature from 'ol/Feature';
import { Style, Stroke, Fill, Circle, Circle as CircleStyle } from 'ol/style';  // 添加这个导入语句
import proj4 from 'proj4';
import Polygon from 'ol/geom/Polygon';
import Text from 'ol/style/Text';
import emitter from "@/utils/bus"
import { register } from "ol/proj/proj4";
import { useStore } from "../../stores/index";
import { Icon } from 'ol/style';
import * as easing from 'ol/easing';
import * as olProj from 'ol/proj';
import { fromLonLat } from 'ol/proj';
import { ScaleLine, defaults } from 'ol/control';
import { getPointResolution } from 'ol/proj';



const store = useStore()


const map = ref(null);
// 地图初始中心点
const initialCenter = [113.349, 23.164];
// 初始比例
const initialZoom = 18;


var wgs84 = '+proj=longlat +ellps=WGS84 +datum=WGS84 +no_defs  ';
var cgcs2000 = '+proj=tmerc +lat_0=0 +lon_0=114 +k=1 +x_0=500000 +y_0=0 +ellps=GRS80 +units=m +no_defs';

onMounted(() => {
  proj4.defs("EPSG:4490", "+proj=longlat +ellps=GRS80 +no_defs");
  register(proj4);

  // 创建地图
  map.value = new Map({
    target: 'map',
    layers: [
      // new ImageLayer({
      //   source: new ImageStatic({
      //     //url: 'http://47.236.64.84:8086/map2_x1_y1.png', 
      //     url: 'src/assets/六一运动场_x0_y0.jpg',
      //     imageExtent: [113.343, 23.159, 113.354, 23.1691],
      //   }),
      // }),
      new ImageLayer({
        source: new ImageStatic({
          //url: 'http://47.236.64.84:8086/map2_x1_y1.png', 
          url: 'src/assets/操场.png',
          imageExtent: [113.3498572, 23.1651863, 113.3505, 23.1661004],
        }),
      }),
    ],
    view: new View({
      projection: 'EPSG:4326',
      // projection: 'EPSG:4490',
      center: initialCenter,
      zoom: initialZoom,
      minZoom: 17, // 设置最小缩放级别
      // maxZoom: 20, // 设置最大缩放级别
      // extent: [113.209, 23.1052, 113.231, 23.1254], // 设置地图视图的边界范围
    })
  });

  // 第一个RTK
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
      movePoint.setCoordinates(proj4(cgcs2000, wgs84, [parseFloat(store.zuobiao.y), parseFloat(store.zuobiao.x)]));
      // console.log(proj4(cgcs2000, wgs84, [parseFloat(store.zuobiao.y), parseFloat(store.zuobiao.x)]));
      // console.log("实时移动2000坐标" + store.zuobiao.y, store.zuobiao.x);

    }, 1000); // 每1秒移动一次
  };

  // 第二个RTK
  const movePointTwo = new Point(proj4(cgcs2000, wgs84, [433410.633706, 2562980.255210475]));
  const movePointFeatureTwo = new Feature(movePointTwo);
  movePointFeatureTwo.setStyle(new Style({
    image: new Circle({
      radius: 8,
      fill: new Fill({ color: 'blue', width: 1, height: 1 }),
      stroke: new Stroke({ color: 'white', width: 2 }),
    }),
  }));

  // 创建移动点图层
  const movePointLayerTwo = new VectorLayer({
    source: new Vector({
      features: [movePointFeatureTwo],
    }),
  });

  // 将移动点图层添加到地图
  map.value.addLayer(movePointLayerTwo);

  // 定义移动点的动画函数
  const animateMovePointTwo = () => {
    // let index = 0;
    const interval = setInterval(() => {
      movePointTwo.setCoordinates(proj4(cgcs2000, wgs84, [parseFloat(store.zuobiaoTwo.y), parseFloat(store.zuobiaoTwo.x)]));
      // console.log(proj4(cgcs2000, wgs84, [parseFloat(store.zuobiaoTwo.y), parseFloat(store.zuobiaoTwo.x)]));
      // console.log("实时移动2000坐标" + store.zuobiaoTwo.y, store.zuobiaoTwo.x);

    }, 1000); // 每1秒移动一次
  };

  // 调用动画函数，使点移动
  animateMovePoint();
  animateMovePointTwo();

  // 创建一个中点来显示图标
  const midPoint = new Point([(movePoint.getCoordinates()[0] + movePointTwo.getCoordinates()[0]) / 2, (movePoint.getCoordinates()[1] + movePointTwo.getCoordinates()[1]) / 2]);

  const midPointFeature = new Feature(midPoint);

  const midPointStyle = new Style({
    image: new Icon({
      src: 'src/assets/操场.png', // 替换为你的图片路径
      anchor: [0.5, 0.5], // 图标的中心点作为锚点
      scale: 0.1, // 初始图标缩放比例
      rotation: 0, // 初始旋转角度
    }),
  });

  // // 监听地图的缩放级别变化事件
  // map.value.getView().on('change:resolution', function (event) {
  //   // 获取当前缩放级别
  //   var zoomLevel = map.value.getView().getZoom();
  //   console.log(zoomLevel);

  //   // 根据缩放级别调整图标的大小
  //   var newScale = 0.05 * (zoomLevel)
  //   console.log(newScale)

  //   // 更新图标样式的缩放比例
  //   midPointStyle.getImage().setScale(newScale);
  // });


  midPointFeature.setStyle(midPointStyle);

  const midPointLayer = new VectorLayer({
    source: new Vector({
      features: [midPointFeature],
    }),
  });

  // map.value.addLayer(midPointLayer);


  // 模拟移动
  setInterval(() => {

    const midX = (movePoint.getCoordinates()[0] + movePointTwo.getCoordinates()[0]) / 2;
    const midY = (movePoint.getCoordinates()[1] + movePointTwo.getCoordinates()[1]) / 2;

    midPoint.setCoordinates([midX, midY]);

    // 计算旋转角度
    // atan（to.x-from.x,to.y-from.y）,这里即movePoint1(rtk2)到movePint2(rtk1)
    const rotationAngle = Math.atan2(movePointTwo.getCoordinates()[1] - movePoint.getCoordinates()[1], movePointTwo.getCoordinates()[0] - movePoint.getCoordinates()[0]);
    // console.log(rotationAngle * (180 / Math.PI))
    // 直接更新 midPointFeature 的样式中的 rotation 属性
    midPointFeature.getStyle().getImage().setRotation(rotationAngle);

    // vectorSource.refresh();
  }, 500);




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
