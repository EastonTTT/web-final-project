// 定义类型
interface LoginRecordType {
  user_id: number | null;
  username: string | null;
  role: number | null;
  isLogged: boolean
}

const defaultLoginRecord: LoginRecordType = {
  user_id: null,
  username: null,
  role: null,
  isLogged: false
};

function getLoginRecord() : LoginRecordType{
  const storedRecord = localStorage.getItem('LoginRecord');
  return storedRecord? JSON.parse(storedRecord) : {isLogged: false,username: null,user_id: null,role: null};
}

function setLoginRecord(userData: {user_id: number,username: string,role: number}) : void{
  const loginRecord: LoginRecordType = {
    isLogged : true,
    user_id: userData.user_id,
    username: userData.username,
    role: userData.role
  };

  localStorage.setItem('LoginRecord',JSON.stringify(loginRecord));

}

function clearLoginRecord() : void {
  localStorage.removeItem('LoginRecord');
}

export {setLoginRecord,getLoginRecord,clearLoginRecord,defaultLoginRecord};
