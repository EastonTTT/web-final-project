// 定义类型
interface LoginRecordType {
  username:String | null,
  role:number | null
}

// 创建响应式对象
import { reactive } from 'vue';

export const LoginRecord = reactive<LoginRecordType>({
  username: null,  // 初始化为 false，符合定义的类型
  role: null
});
