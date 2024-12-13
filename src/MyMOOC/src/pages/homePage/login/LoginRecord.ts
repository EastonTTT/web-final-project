// 定义类型
interface LoginRecordType {
  user_id: number | null;
  username: string | null;
  role: number | null;
}

// 创建响应式对象
import { reactive } from 'vue';

export const LoginRecord = reactive<LoginRecordType>({
  user_id: null,
  username: null,
  role: null,
});
