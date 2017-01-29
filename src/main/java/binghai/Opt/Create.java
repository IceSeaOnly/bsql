package binghai.Opt;

import binghai.Core.*;
import binghai.Std.Format;
import binghai.Std.IO;
import binghai.Std.Result;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by sdust on 2016/12/9.
 */
public class Create {
    private static String SameNameErr = "重名错误";
    /**
     * 格式化并转换成模型
     */
    private static DataTable formatCreateSQL(String ori) throws Exception {
        String std = Format.formatOriSQL(ori);
        int findex = std.indexOf('(');
        int lindex = std.lastIndexOf(')');
        String tableSource = std.substring(findex + 1, lindex).trim();
        String[] src = tableSource.split(",");
        ArrayList<DataItem> items = new ArrayList<DataItem>();
        List<String> pks = new ArrayList<String>();
        for (int i = 0; i < src.length; i++) {
            if (src[i].trim().startsWith("`")) {
                DataItem it = translateSQL2Model(src[i].trim());
                if(hasSameName(items,it))
                    throw new Exception(SameNameErr+" -> "+it.getName()+"重复");
                items.add(it);
                if(it.getPK() && !pks.contains(it.getName()))
                    pks.add(it.getName());
            } else if(src[i].trim().startsWith("primary key")){
                ArrayList<String> ps = getPK(src[i].trim());
                for (String s :
                        ps) {
                    if (!pks.contains(s)) pks.add(s);
                }
            }
        }

        assignmentPK(pks,items);
        ArrayList<PrimaryKey> pkss = new ArrayList<PrimaryKey>();
        for (int i = 0; i < pks.size(); i++) {
            pkss.add(new PrimaryKey(pks.get(i),new ArrayList<String>()));
        }
        return new DataTable(pkss, items, new ArrayList<DataRecord>());
    }
    private static boolean hasSameName(ArrayList<DataItem> items, DataItem it) {
        for (int i = 0; i < items.size(); i++) {
            if(items.get(i).getName().equals(it.getName()))
                return true;
        }
        return false;
    }
    private static void assignmentPK(List<String> pks, ArrayList<DataItem> items) {
        for (int i = 0; i < pks.size(); i++) {
            for (int j = 0; j < items.size(); j++) {
                if(items.get(j).getName().equals(pks.get(i))){
                    items.get(j).setPK(true);
                }
            }
        }
    }
    private static ArrayList<String> getPK(String trim) {
        int findex = trim.indexOf('(');
        int lindex = trim.indexOf(")");
        String[] tpks = trim.substring(findex+1,lindex).split(",");
        ArrayList<String> pks = new ArrayList<String>();
        for (int i = 0; i < tpks.length; i++) {
            pks.add(tpks[i].trim().substring(1,tpks[i].length()-1));
        }
        return pks;
    }
    /**
     * 把创建表的定义语句转换成模型
     */
    private static DataItem translateSQL2Model(String s) throws Exception {
        int findex = s.indexOf('`');
        if (findex == s.length()) throw new Exception("sql语句不合法");
        int lindex = s.lastIndexOf('`');
        String mname = s.substring(findex + 1, lindex);
        int rfindex = s.indexOf('(');
        if (rfindex == s.length()) throw new Exception("sql语句不合法");
        String type = s.substring(lindex + 2, rfindex);
        int rlindex = s.indexOf(')');
        if (rlindex == s.length()) throw new Exception("sql语句不合法");
        int len = Integer.parseInt(s.substring(rfindex + 1, rlindex).trim());
        Boolean PK = s.contains("primary key");
        Boolean autoIncrement = s.contains("auto") && s.contains("increment");
        Boolean not_null = s.contains("not null");
        Boolean default_null = s.contains("default null");
        Boolean hasIndex = false;
        if (not_null && default_null) throw new Exception("不可同时含有not null和default null");
        return new DataItem(mname, type, len, PK, autoIncrement, not_null, default_null, hasIndex);
    }
    public static Result createTable(String ori) {
        String tableName = getTableName(ori);
        if (IO.isDataTableExsit(SystemCore.getCurDB().getDbName()+"/"+tableName))
            return new Result(false, null, "该数据表已经存在!创建失败!");

        JSONObject table = new JSONObject();
        try {
            table.put("table", formatCreateSQL(ori));
        } catch (Exception e) {
            return new Result(false, null, e.getMessage());
        }
        IO.WriteCH(SystemCore.getCurDB().getDbName()+"/"+tableName, table.toJSONString());
        SystemCore.getCurDB().getTbs().add(tableName);
        SystemCore.getCurUser().getDbs().get(SystemCore.getCurDB().getDbName()).addTable(tableName);
        return new Result(true, null, null);
    }
    private static String getTableName(String ori) {
        int sindex = ori.indexOf('`');
        int eindex = ori.indexOf('`', sindex + 1);
        return ori.substring(sindex + 1, eindex);
    }
    public static Result CreateDB(String std) {
        String dbName = getDBName(std);
        if(dbName == null) return new Result(false,"语法不正确","请尝试使用 create database `DATABASE` 创建数据库");
        if(IO.dictionaryExist(dbName)){
            return new Result(false,"数据库"+dbName+"已经存在","不可创建同名数据库");
        }
        IO.CreateDataBase(dbName);
        DataBase db = new DataBase(dbName,new ArrayList<String>());
        SystemCore.getDbs().add(db);
        SystemCore.getCurUser().grant(dbName,new DBPrivilege(true,true,true,true,true,true,true,true,true,new ArrayList<String>()));
        return new Result(true,dbName+"数据库创建成功",null);
    }
    private static String getDBName(String std) {
        String[] cmds = std.split(" ");
        if(cmds.length>2 && cmds[2].startsWith("`") && cmds[2].endsWith("`"))
            return cmds[2].substring(1,cmds[2].length()-1);
        return null;
    }
    public static Result useDB(String std) {
        String[] cmds = std.split(" ");
        if(cmds.length< 2)
            return new Result(false,"语法错误","试试 use database?");
        for (int i = 0; i < SystemCore.getDbs().size(); i++) {
            if(SystemCore.getDbs().get(i).getDbName().equals(cmds[1])){
                SystemCore.setCurDB(SystemCore.getDbs().get(i));
                if(SystemCore.getCurUser().getDbs().get(SystemCore.getCurDB().getDbName()) == null){
                    SystemCore.setCurDB(null);
                    return new Result(false,"当前用户无权操作"+cmds[1]+"数据库",null);
                }
                return new Result(true,"已选定"+ SystemCore.getCurDB().getDbName()+"数据库","success");
            }
        }
        return new Result(false,cmds[1]+"数据库不存在或已经损坏",null);
    }
    public static Result createUser(String std) {
        // create user 'xiaoming' identified by 'password';
        String[] cmds = std.split(" ");
        if(cmds.length<6)
            return new Result(false,"语法不正确","试试create user 'xxx' identified by 'password';来创建用户试试?");
        String name = cmds[2];
        if(!name.startsWith("'") || !name.endsWith("'"))
            return new Result(false,"语法不正确","试试create user 'xxx' identified by 'password';来创建用户试试?");
        name = name.substring(1,name.length()-1);
        for (int i = 0; i < SystemCore.getUsers().size(); i++) {
            if(SystemCore.getUsers().get(i).getName().equals(name))
                return new Result(false,"创建失败",name+"用户已存在!");
        }
        String password = cmds[5];
        if(!password.startsWith("'") || !password.endsWith("'"))
            return new Result(false,"语法不正确","试试create user 'xxx' identified by 'password';来创建用户试试?");
        SystemCore.getUsers().add(new User(name,password,new UserPrivileges(true,false,false,false),new HashMap<String, DBPrivilege>()));
        return new Result(true,name+"创建成功","success");
    }
}
