// 定义类型
interface LogStatusType {
  isLogged: boolean;
}

// 创建响应式对象
import { reactive } from 'vue';

export const LogStatus = reactive<LogStatusType>({
  isLogged: false  // 初始化为 false，符合定义的类型
});
