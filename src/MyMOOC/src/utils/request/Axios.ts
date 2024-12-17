import axios from "axios";
import type{AxiosInstance} from "axios";
import type{Result} from "../resultModel.ts";
import type { StringMappingType } from "typescript";

interface MyAxiosInstance extends AxiosInstance{
  myGetting :<T = any> (url: string, params?: Record<string, any>) => Promise<Result<T>>;
  myPosting :<T = any> (url:string, data?:Record<string, any>) => Promise<Result<T>>;
  myDelete :<T = any> (url:string, data?:Record<string, any>) => Promise<Result<T>>;
}

const axiosInstance:MyAxiosInstance = axios.create({
  baseURL:'http://localhost:8080',
})as MyAxiosInstance;

axiosInstance.myGetting = async function <T = any>(url: string, params?:Record<string,any>){
  try{
    const response = await this.get(url,params);
    if(response.data.status !== 200){
      return {
        status:400,
        message:response.data.message,
        data: null
      }
    }else{
      return{
        status: 200,
        message:response.data.message,
        data: response.data.data
      }
    }
  } catch(error: any){
    return{
      status:error.response?.status || 500,
      message:error.message || 'Internal server error',
      data: null
    }
  }

}

axiosInstance.myPosting = async function (url: string, data?: Record<string, any>) {
  try {
    const response = await this.post(url, data);
    return {
      status: response.status,
      message: '',
      data: response.data
    };
  } catch (error: any) {
    return {
      status: error.response?.status || 500,
      message: error.message || "Internal server error",
      data: null
    };
  }
}

axiosInstance.myDelete = async function (url: string, data?: Record<string,any>) {
  try {
    const response = await this.delete(url, data);
    return {
      status: response.status,
      message: '',
      data: response.data
    }
  } catch (error: any) {
    return {
      status: error.response?.status || 500,
      message: error.message || "Internal server error",
      data: null
    };
  }
}


export default axiosInstance;
