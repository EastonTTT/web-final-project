export interface Result<T>{

  // 定义泛型接口 Result，匹配后端发送的结构
    status: number;    // 状态码
    message: string;   // 响应消息
    data: T | null;    // 响应数据，根据实际情况可能为 null

}

