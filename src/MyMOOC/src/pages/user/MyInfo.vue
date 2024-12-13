<template>
  <t-space direction="vertical" size="large" style="margin: 30px 100px;">
    <!-- 可以使用全局 ConfigProvider errorMessage 配置规则校验结果描述，而无需给每一个表单都配置校验信息 -->


    <!-- error-message 非必需 -->
    <t-form
      ref="form"
      :data="formData"
      :rules="rules"
      scroll-to-first-error="smooth"
      @reset="onReset"
      @submit="onSubmit"
      style="margin: 20px 10px;"
    >
      <!-- !!!注意：当 FormItem 的 label 属性为 Function 时，errorMessage 模板中的 ${name} 会被替换为 FormItem.name 属性值 -->
      <t-form-item :label="renderLabel" name="account" style="width: 400px;">
        <t-input v-model="formData.account"></t-input>
      </t-form-item>

      <t-form-item label="密码" name="password">
        <t-input v-model="formData.password" type="password"></t-input>
      </t-form-item>

      <t-form-item label="邮箱" name="email">
        <t-auto-complete v-model="formData.email" :options="emailOptions" filterable></t-auto-complete>
      </t-form-item>

      <t-form-item label="性别" name="gender">
        <t-radio-group v-model="formData.gender">
          <t-radio value="male">男</t-radio>
          <t-radio value="femal">女</t-radio>
        </t-radio-group>
      </t-form-item>

      <t-form-item
        label="入学时间"
        name="date"
        :rules="[{ date: { delimiters: ['/', '-', '.'] }, message: '日期格式有误' }]"
      >
        <t-input v-model="formData.date" disabled=""></t-input>
      </t-form-item>

      <t-form-item>
        <t-space size="small">
          <t-button theme="primary" type="submit">提交</t-button>
          <t-button theme="default" variant="base" type="reset">重置</t-button>
        </t-space>
      </t-form-item>
    </t-form>
  </t-space>
</template>
<script lang="ts" setup>
import { ref, reactive, computed } from 'vue';
import {
  MessagePlugin,
  FormProps,
  FormInstanceFunctions,
  AutoCompleteProps,
  CheckboxGroupProps,
  FormItemProps,
  ButtonProps,
} from 'tdesign-vue-next';
const formData: FormProps['data'] = reactive({
  account: '',
  password: '',
  email: '',
  gender: '',
  date: '',
});
const form = ref<FormInstanceFunctions>(null);
const emailSuffix = ['@qq.com', '@163.com', '@gmail.com'];
const emailOptions = computed<AutoCompleteProps['options']>(() => {
  const emailPrefix = formData.email.split('@')[0];
  if (!emailPrefix) return [];
  return emailSuffix.map((suffix) => emailPrefix + suffix);
});


const onReset: FormProps['onReset'] = () => {
  MessagePlugin.success('重置成功');
};
const onSubmit: FormProps['onSubmit'] = ({ validateResult, firstError }) => {
  if (validateResult === true) {
    MessagePlugin.success('提交成功');
  } else {
    console.log('Errors: ', validateResult);
    MessagePlugin.warning(firstError);
  }
};
const renderLabel: FormItemProps['label'] = () => '用户名';

const rules: FormProps['rules'] = {
  account: [
    {
      required: true,
    },
    // { enum: ['sheep', 'name'] },
    {
      min: 2,
    },
    {
      max: 10,
      type: 'warning',
    },
  ],
  description: [
    {
      validator: (val) => val.length >= 3,
      message: '太短啦'
    },
    {
      validator: (val) => val.length < 10,
      message: '不能超过 20 个字，中文长度等于英文长度',
    },
  ],
  password: [
    {
      required: true,
    },
    {
      len: 8,
      message: '请输入 8 位密码',
    },
    {
      pattern: /[A-Z]+/,
      message: '密码必须包含大写字母',
    },
  ],
  email: [
    {
      required: true,
    },
    {
      email: {
        ignore_max_length: true,
      },
    },
  ],
  gender: [
    {
      required: true,
    },
  ],
  course: [
    {
      required: true,
    },
    {
      validator: (val) => val.length <= 2,
      message: '最多选择 2 门课程',
      type: 'warning',
    },
  ],
  'content.url': [
    {
      required: true,
    },
    {
      url: {
        protocols: ['http', 'https', 'ftp'],
        require_protocol: true,
      },
    },
  ],
};

</script>

<style scoped>
.demo-select-base {
  width: 400px;
}
</style>
