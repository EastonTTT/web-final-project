<template>

  <div class="my-table">
    <el-table :data="tableData" style="width: 100%" height="600" :row-style="{height: '60px'}">
      <el-table-column fixed prop="date" label="Date" width="200" />
      <el-table-column prop="name" label="Name" width="200" />
      <el-table-column prop="email" label="Email" width="300" />
      <el-table-column prop="role" label="Role" width="200" />
      <el-table-column fixed="right" label="Operations" min-width="200">
        <template v-slot="scope">
          <el-button type="primary" size="small" @click="handleResetPassword(scope.row.name)">
            Reset password
          </el-button>
          <!-- <el-button type="danger">Danger</el-button> -->
          <el-button type="danger" size="small" @click="preDelete(scope.row)">Delete</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>

  <el-dialog v-model="dialogVisible" title="删除确认" width="500">
    <span style="font-size: 15px;">⚠你确定要执行删除操作嘛? 此操作不可撤销哦!</span>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="dialogVisible = false">Cancel</el-button>
        <el-button type="danger" @click="handleDelete(currentRow.name)">
          Confirm
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script lang="ts" setup>

import MyAxios from '@/utils/request/Axios';
import { MessagePlugin } from 'tdesign-vue-next';
import { ref } from 'vue';
import { ElMessageBox } from 'element-plus'
import { ca, tr } from 'vuetify/locale';

interface dataType{
  date: string | null,
  name: string | null,
  email: string | null,
  role: string | null
}

getAccount();

let tableData = ref<dataType[]>([]);  // 初始化为一个空数组
const dialogVisible = ref(false);
const currentRow = ref(null);

function preDelete(row){
  currentRow.value = row;
  dialogVisible.value = true;
}

async function getAccount(){
  try{
    const response = await MyAxios.myGetting('/user/accountAll');
    if(response.status === 200){
      MessagePlugin.success('账户信息查询成功');
      tableData.value = response.data.map(item => ({
      date: new Date(item.created_at).toLocaleDateString(),
      name: item.username,
      email: item.email,
    role: getIdentity(item.role)
  }));
    }else{
      MessagePlugin.error('查询账户信息：' + response.message)

    }
  }catch(error){
    console.log('查询账户信息失败...');
    MessagePlugin.error('查询账户信息失败')

  }
}

function getIdentity(role: number) {
  switch (role) {
    case 1:
      return '学生';
    case 2:
      return '教师';
    case 3:
      return '管理员'
    default:
      return 'unknown'
  }
}

//执行删除用户操作
async function handleDelete(name){
  console.log(name);
  dialogVisible.value = false;
  try{
    const response = await MyAxios.myDelete(`/user/delete/${name}`);
    if(response.status === 200){
      MessagePlugin.success('删除用户成功!');
      getAccount();
    }else{
      MessagePlugin.error('删除用户失败..');
    }
  }catch(error){
    MessagePlugin.error('删除请求失败..');
  }
}

//重置密码操作
async function handleResetPassword(name){
  try{
    console.log(name);
    const response = await MyAxios.myPosting('/user/update',{username: name});
    if(response.status === 200){
      MessagePlugin.success('重置密码成功!');
    }else{
      MessagePlugin.error('重置密码失败..');
    }
  }catch(error){
    MessagePlugin.error('重置请求失败..');
  }
}
</script>

<style lang="less" scoped>
  .my-table{
    margin: 20px;
  }
</style>
