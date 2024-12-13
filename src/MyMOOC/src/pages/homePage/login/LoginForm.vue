<template>
  <div style="width: 350px">
    <t-form ref="form" :data="formData" :colon="true" :rules="rules" :label-width="0" @reset="onReset" @submit="onSubmit" >
      <t-form-item name="account">
        <t-input v-model="formData.account" clearable placeholder="请输入账户名">
          <template #prefix-icon>
            <desktop-icon />
          </template>
        </t-input>
      </t-form-item>

      <t-form-item name="password" style="margin: 0;">
        <t-input v-model="formData.password" type="password" clearable placeholder="请输入密码" >
          <template #prefix-icon>
            <lock-on-icon />
          </template>
        </t-input>
      </t-form-item>
      <t-form-item style="margin: 5px 0 2px 275px;">
        <t-switch label="记住密码"/>
      </t-form-item>
      <t-form-item>
        <t-button theme="primary" type="submit" block>登录</t-button>
      </t-form-item>
    </t-form>
  </div>
</template>
<script lang="ts" setup>
import { reactive } from 'vue';
import { MessagePlugin, FormProps, type FormRule } from 'tdesign-vue-next';
import { DesktopIcon, LockOnIcon } from 'tdesign-icons-vue-next';
import { useRouter } from 'vue-router';
// import { defineEmits } from 'vue';
import { LogStatus } from '@/pages/homePage/login/LogStatus';
import { LoginRecord } from '@/pages/homePage/login/LoginRecord';
import MyAxios from '@/utils/request/Axios.ts';

const router = useRouter()
const emit = defineEmits(['close-dialog']);

//登录表单内置校验方法:
const rules: FormProps['rules'] = {
  account:[
    {
      required: true,
      message: '姓名必填',
      type: 'error',
      trigger: 'blur',
    },
    {
      required: true,
      message: '姓名必填',
      type: 'error',
      trigger: 'change',
    },
    {
      whitespace: true,
      message: '姓名不能为空',
    },
    {
      max: 15,
      message: '最大长度不能超过15个字符',
      type: 'error',
      trigger: 'blur',
    }
  ],
  password:[
  {
      required: true,
      message: '密码必填',
      type: 'error',
      trigger: 'blur'
    },
    {
      required: true,
      message: '密码必填',
      type: 'error',
      trigger: 'change'
    },
  ]
}

//登陆数据表单数据模型
const formData: FormProps['data'] = reactive({
  account: '',
  password: '',
});

const onReset: FormProps['onReset'] = () => {
  MessagePlugin.success('重置成功');
};

const onSubmit: FormProps['onSubmit'] = async ({ validateResult, firstError }) => {


  if (validateResult === true) {
    try{
      const response = await MyAxios.myPosting('/login',
        {
          username: formData.account,
          password: formData.password
        });
        if(response.data.status === 200){
          MessagePlugin.success('登录成功')
          emit('close-dialog');
          LogStatus.isLogged = true;
          LoginRecord.user_id = response.data.data.user_id;
          LoginRecord.username = response.data.data.username;
          LoginRecord.role = response.data.data.role;
        }else{
          MessagePlugin.error('登陆失败：' + response.data.message)
        }
    }catch(error){
      console.log('登录请求失败',error)
      MessagePlugin.error('登录请求失败')
    }
  } else {
    console.log('Validate Errors: ', firstError, validateResult);
    MessagePlugin.warning(firstError);
  }
};
</script>
