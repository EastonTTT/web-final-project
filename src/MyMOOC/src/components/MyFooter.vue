<template>
  <div class="footer">
    <div class="logo"><img src="../assets/uooc.png" alt="uooc-logo" width="150px" style="margin: 30px;"></div>
    <div class="text-content">
      <div>
        <t-space break-line class="break-line">
          <span>关于我们</span>
          <t-divider layout="vertical" />
          <span>服务条款</span>
          <t-divider layout="vertical" />
          <span>机构管理</span>
          <t-divider layout="vertical" />
          <span>免责声明</span>
        </t-space>
        <br>
        <t-space break-line class="break-line">
          <span>友情链接</span>
          <t-divider layout="vertical" />
          <span>深大优课</span>
          <t-divider layout="vertical" />
          <span>广东学历提升网</span>
          <t-divider layout="vertical" />
          <span>免责声明</span>
        </t-space>
      </div>

      <p>
      </p>
    </div>

  </div>
</template>

<script lang="ts" setup>

</script>

<style lang="less" scoped>
  .footer{
    height: 150px;
    text-align: center;
    display: flex;
    background-color: #313131;
    .text-content{
      span{
        font-size: 14px;
        color: #7d7d7d;
      }
      .break-line{
        margin-top: 10px;
      }

    }
  }
</style>
