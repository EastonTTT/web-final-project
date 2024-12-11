<template>
  <div>
    <h3>课程发布</h3>
    <el-form :model="course" label-width="120px">
      <el-form-item label="课程名称">
        <el-input v-model="course.name" placeholder="请输入课程名称"></el-input>
      </el-form-item>

      <el-form-item label="课程简介">
        <el-input
          type="textarea"
          v-model="course.description"
          placeholder="请输入课程简介"
        ></el-input>
      </el-form-item>

      <el-form-item label="轮播图">
        <el-upload
          class="upload-demo"
          action="#"
          :auto-upload="false"
          :file-list="carouselImages"
          multiple
          :on-change="handleCarouselChange"
        >
          <el-button type="primary">上传轮播图</el-button>
        </el-upload>
        <el-button type="success" @click="submitCarousel">提交轮播图</el-button>
        <el-draggable v-model="carouselImages" handle=".drag-handle">
          <div
            v-for="(item, index) in carouselImages"
            :key="item.uid"
            class="carousel-item"
          >
            <span class="drag-handle">::</span>
            <img :src="item.url" alt="轮播图" class="carousel-img" />
          </div>
        </el-draggable>
      </el-form-item>

      <el-form-item label="是否开启评论区">
        <el-switch v-model="course.commentsEnabled" active-text="开启" inactive-text="关闭"></el-switch>
      </el-form-item>

      <el-form-item label="是否开启笔记区">
        <el-switch v-model="course.notesEnabled" active-text="开启" inactive-text="关闭"></el-switch>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="previewCourse">预览课程</el-button>
        <el-button type="success" @click="publishCourse">发布课程</el-button>
      </el-form-item>
    </el-form>

    <el-dialog :visible.sync="previewVisible" title="课程预览">
      <p><strong>课程名称：</strong>{{ course.name }}</p>
      <p><strong>课程简介：</strong>{{ course.description }}</p>
      <p><strong>轮播图：</strong></p>
      <div class="preview-carousel">
        <img v-for="(img, index) in carouselImages" :key="index" :src="img.url" alt="轮播图" />
      </div>
      <p><strong>评论区：</strong>{{ course.commentsEnabled ? '开启' : '关闭' }}</p>
      <p><strong>笔记区：</strong>{{ course.notesEnabled ? '开启' : '关闭' }}</p>
      <span slot="footer" class="dialog-footer">
        <el-button @click="previewVisible = false">关闭</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref } from 'vue';
import { ElForm, ElFormItem, ElInput, ElUpload, ElButton, ElSwitch, ElDialog, ElMessage } from 'element-plus';
import draggable from 'vuedraggable';

export default defineComponent({
  name: 'CoursePublishing',
  components: {
    ElForm,
    ElFormItem,
    ElInput,
    ElUpload,
    ElButton,
    ElSwitch,
    ElDialog,
    draggable,
  },
  setup() {
    const course = ref({
      name: '',
      description: '',
      commentsEnabled: true,
      notesEnabled: true,
    });

    const carouselImages = ref<any[]>([]);
    const previewVisible = ref(false);

    const handleCarouselChange = (file: any, fileList: any[]) => {
      carouselImages.value = fileList;
    };

    const submitCarousel = () => {
      if (carouselImages.value.length === 0) {
        ElMessage.warning('请先选择要上传的轮播图');
        return;
      }
      // 模拟上传过程
      setTimeout(() => {
        ElMessage.success('轮播图上传成功');
      }, 1000);
    };

    const previewCourse = () => {
      if (!course.value.name) {
        ElMessage.warning('请填写课程名称');
        return;
      }
      previewVisible.value = true;
    };

    const publishCourse = () => {
      if (!course.value.name || !course.value.description) {
        ElMessage.warning('请填写完整的课程信息');
        return;
      }
      // 模拟发布过程
      setTimeout(() => {
        ElMessage.success('课程发布成功');
        // 清空表单
        course.value.name = '';
        course.value.description = '';
        carouselImages.value = [];
        course.value.commentsEnabled = true;
        course.value.notesEnabled = true;
      }, 1000);
    };

    return {
      course,
      carouselImages,
      previewVisible,
      handleCarouselChange,
      submitCarousel,
      previewCourse,
      publishCourse,
    };
  },
});
</script>

<style scoped>
.carousel-item {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
}

.drag-handle {
  cursor: move;
  margin-right: 10px;
}

.carousel-img {
  width: 100px;
  height: 60px;
  object-fit: cover;
}

.preview-carousel img {
  width: 150px;
  height: 90px;
  object-fit: cover;
  margin-right: 10px;
}
</style>
