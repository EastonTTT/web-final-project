<template>
  <div style="width: 350px">
    <t-form ref="form" :data="formData" :colon="true" :label-width="0" @reset="onReset" @submit="onSubmit" >
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
import { MessagePlugin, FormProps } from 'tdesign-vue-next';
import { DesktopIcon, LockOnIcon } from 'tdesign-icons-vue-next';
import { useRouter } from 'vue-router';
import { defineEmits } from 'vue';
import { LogStatus } from '@/pages/homePage/login/LogStatus';


const router = useRouter()
const emit = defineEmits(['close-dialog']);
const formData: FormProps['data'] = reactive({
  account: '',
  password: '',
});
const onReset: FormProps['onReset'] = () => {
  MessagePlugin.success('重置成功');
};
const onSubmit: FormProps['onSubmit'] = ({ validateResult, firstError }) => {
  if (validateResult === true) {
    MessagePlugin.success('登录成功')
    emit('close-dialog');
    LogStatus.isLogged = true;
    // router.push('/user');
  } else {
    console.log('Validate Errors: ', firstError, validateResult);
    MessagePlugin.warning(firstError);
  }
};
</script>
