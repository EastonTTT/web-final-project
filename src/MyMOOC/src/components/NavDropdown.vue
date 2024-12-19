<template>
  <el-dropdown>
    <span class="el-dropdown-link">
      <el-text class="mx-1" size="large">课程</el-text>
      <el-icon class="el-icon--right">
        <arrow-down />
      </el-icon>
    </span>
    <template #dropdown>
      <el-dropdown-menu>
        <el-dropdown-item divided><Calculation1Icon size="large" /> <p>理学</p></el-dropdown-item>
        <el-dropdown-item divided><ToolsCircleIcon size="large" /><p>工学</p></el-dropdown-item>
        <el-dropdown-item divided><LaptopIcon size="large" /><p>计算机</p></el-dropdown-item>
        <el-dropdown-item divided><PaletteIcon size="large" /><p>艺术</p></el-dropdown-item>
        <el-dropdown-item divided><Hospital1Icon size="large" /><p>医学</p></el-dropdown-item>
        <el-dropdown-item divided><ChatHeartIcon size="large" /><p>心理学</p></el-dropdown-item>
        <el-dropdown-item divided><BookOpenIcon size="large" /><p>哲史/文化</p></el-dropdown-item>
        <el-dropdown-item divided><el-icon><MoreFilled /> </el-icon></el-dropdown-item>
      </el-dropdown-menu>
    </template>
  </el-dropdown>
</template>

<script lang="ts" setup>
import { ArrowDown } from '@element-plus/icons-vue'
import {LaptopIcon,Calculation1Icon,ToolsCircleIcon,PaletteIcon,ChatHeartIcon,BookOpenIcon,Hospital1Icon} from 'tdesign-icons-vue-next'
</script>
<style scoped lang="less">
.example-showcase .el-dropdown-link {
  cursor: pointer;
  color: var(--el-color-primary);
  display: flex;
  align-items: center;
  font-size: 16px;
}
  p{
    margin: 0px 10px;
    font-size: 15px;
  }

  .el-dropdown-link:focus {
      outline: none;
}


</style>
