<template>
  <router-view />
</template>

<style>
/* 全局重置，让根元素铺满全屏 */
html, body, #app {
  width: 100%;
  height: 100%;
  margin: 0;
  padding: 0;
  /* 可选：防止滚动条/间距溢出 */
  box-sizing: border-box;
  overflow-x: hidden;
}
</style>