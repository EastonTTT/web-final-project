<template>
  <div>
    <h3>课件管理</h3>
    <el-upload
      class="upload-demo"
      action="#"
      :auto-upload="false"
      :file-list="fileList"
      multiple
      :on-change="handleChange"
    >
      <el-button type="primary">点击上传课件</el-button>
    </el-upload>
    <el-button type="success" @click="submitUpload">提交上传</el-button>

    <el-tree
      :data="tagTree"
      :props="defaultProps"
      accordion
      node-key="id"
      :expand-on-click-node="false"
      class="filter-tree"
    ></el-tree>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref } from 'vue';
import { ElUpload, ElButton, ElTree, ElMessage } from 'element-plus';

export default defineComponent({
  name: 'CourseManagement',
  components: {
    ElUpload,
    ElButton,
    ElTree,
  },
  setup() {
    const fileList = ref<any[]>([]);
    const tagTree = ref([
      {
        id: 1,
        label: '数学',
        children: [
          { id: 2, label: '代数' },
          { id: 3, label: '几何' },
        ],
      },
      {
        id: 4,
        label: '物理',
        children: [
          { id: 5, label: '力学' },
          { id: 6, label: '电磁学' },
        ],
      },
    ]);

    const defaultProps = {
      children: 'children',
      label: 'label',
    };

    // 重命名参数以避免与外部的 fileList 冲突
    const handleChange = (file: any, newFileList: any[]) => {
      fileList.value = newFileList;
    };

    const submitUpload = () => {
      if (fileList.value.length === 0) {
        ElMessage.warning('请先选择要上传的课件');
        return;
      }
      // 模拟上传过程
      setTimeout(() => {
        ElMessage.success('课件上传成功');
        fileList.value = [];
      }, 1000);
    };

    return {
      fileList,
      tagTree,
      defaultProps,
      handleChange,
      submitUpload,
    };
  },
});
</script>

<style scoped>
.upload-demo {
  margin-bottom: 20px;
}
.filter-tree {
  margin-top: 20px;
}
</style>
