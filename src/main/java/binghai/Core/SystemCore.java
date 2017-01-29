package binghai.Core;

import binghai.Std.IO;
import binghai.Std.Result;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by Administrator on 2016/12/10.
 * 系统核心
 */
public class SystemCore {
    private static User curUser;
    private static List<User> users;
    private static List<DataBase> dbs;
    /**
     * 传入创建表的命令
     * 返回是否创建成功
     * json方式存储
     * type:{每一列的名称及类型}
     * data:{每一个的每一个参数名称及数据}
     */
    public static DataBase curDB = null;

    public static User getUser(String username){
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getName().equals(username))
                return users.get(i);
        }
        return null;
    }

    public static User getCurUser() {
        return curUser;
    }

    public static void setCurUser(User curUser) {
        SystemCore.curUser = curUser;
    }

    public static List<User> getUsers() {
        return users;
    }

    public static void setUsers(List<User> users) {
        SystemCore.users = users;
    }

    public static List<DataBase> getDbs() {
        return dbs;
    }

    public static void setDbs(List<DataBase> dbs) {
        SystemCore.dbs = dbs;
    }

    public static Result SystemStart() {
        String USER = IO.ReadCH("BSQL_SYS_CORE_USER");
        String DATABASE = IO.ReadCH("BSQL_SYS_CORE_DATABASE");
        if (USER == null || DATABASE == null) {
            System.out.println("系统第一次启动，正在初始化...");

            users = new ArrayList<User>();
            users.add(new User("root", "root", new UserPrivileges(), new HashMap<String, DBPrivilege>()));
            System.out.println("默认管理员root，密码root");
            JSONObject initu = new JSONObject();
            initu.put("users", users);
            IO.WriteCH("BSQL_SYS_CORE_USER", initu.toJSONString());

            dbs = new ArrayList<DataBase>();
            JSONObject arr = new JSONObject();
            arr.put("dbs", dbs);
            IO.WriteCH("BSQL_SYS_CORE_DATABASE", arr.toJSONString());

            USER = initu.toJSONString();
            DATABASE = arr.toJSONString();
        }
        JSONObject ju = JSONObject.parseObject(USER);
        JSONObject jd = JSONObject.parseObject(DATABASE);
        users = JSONObject.parseArray(ju.getString("users"),User.class);
        dbs = JSONObject.parseArray(jd.getString("dbs"),DataBase.class);
        return new Result(true, "系统启动完毕", "数据准备完毕," + users.size() + "个用户," + dbs.size() + "个数据库.");
    }

    public static Result doLogin(String std) {
        String[] cmds = std.split(" ");
        String uname = null;
        String upass = null;
        for (int i = 0; i < cmds.length; i++) {
            if (cmds[i].equals("-u") && i + 1 < cmds.length)
                uname = cmds[i + 1];
            if (cmds[i].equals("-p") && i + 1 < cmds.length)
                upass = cmds[i + 1];
        }
        if (uname != null && upass != null) {
            for (int i = 0; i < users.size(); i++) {
                if (users.get(i) != null && users.get(i).getName().equals(uname)) {
                    if(users.get(i).getPassword().equals(upass)){
                        curUser = users.get(i);
                        return new Result(true, uname + "登录成功", "login success");
                    }
                    else
                        return new Result(false, uname + "登录失败,密码不正确", "login failed");
                }
            }

            return new Result(false, uname + "用户不存在", "login failed");
        }
        return new Result(false, null, "不支持的命令格式,请使用 bsql -u YOUR_NAME -p YOUR_PASS 登录bsql.");
    }

    public static Result doLogout() {
        if (curUser != null)
            curUser = null;
        else
            return new Result(false, "非法操作", "当前没有用户登录!");
        return new Result(true, "登出成功", "logout success");
    }

    public static void flushCore() {
        JSONObject initu = new JSONObject();
        initu.put("users", users);
        IO.WriteCH("BSQL_SYS_CORE_USER", initu.toJSONString());

        JSONObject arr = new JSONObject();
        arr.put("dbs", dbs);
        IO.WriteCH("BSQL_SYS_CORE_DATABASE", arr.toJSONString());
    }

    public static DataBase getCurDB() {
        return curDB;
    }

    public static void setCurDB(DataBase curDB) {
        SystemCore.curDB = curDB;
    }
}
