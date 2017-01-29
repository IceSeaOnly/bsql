package binghai.Opt;

import binghai.Core.*;
import binghai.Std.IO;
import binghai.Std.Result;
import com.alibaba.fastjson.JSONObject;
import binghai.Core.InnerResult;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/12/15.
 */
public class Insert {

    private static String SyntaxError = "语法错误";
    private static String TrySTD = "试试 insert into tbl_name (col1,col2) VALUES(...,...); ?";
    private static String NoRightForTable = "无权操作该数据表";
    private static String TableReadOnly = "当前用户无法写入该表";
    private static String TableNotExist = "数据表不存在或者已经损坏";
    private static String NoInsertDataDefined = "没有指定插入值";
    private static String InsertDataNotMatch = "要插入的值与数据表结构不匹配";
    private static String PK_DUPLICATION = "主键值重复";
    private static String ILLIGAL_DATA = "非法值";
    private static String NotSupportSuchMethod = "暂不支持非完全插入";
    private static String InsertSuccess = "插入成功";

    public static Result Insert(String std) {
        String[] cmds = std.split(" ");
        String tableName = cmds[2];
        if(!tableName.startsWith("`") || !tableName.endsWith("`"))
            return new Result(false,SyntaxError,TrySTD);
        tableName = tableName.substring(1,tableName.length()-1);
        if(!SystemCore.getCurUser().getDbs().get(SystemCore.getCurDB().getDbName()).getTbs().contains(tableName))
            return new Result(false,NoRightForTable,NoRightForTable);
        if(!SystemCore.getCurUser().getDbs().get(SystemCore.getCurDB().getDbName()).isInsert())
            return new Result(false,NoRightForTable,TableReadOnly);
        int Lbrackets = countBrackets(std,'(');
        int Rbrackets = countBrackets(std,')');

        if(Lbrackets != Rbrackets || (Lbrackets != 2 && Lbrackets != 1))
            return new Result(false,SyntaxError,TrySTD);
        
        String tdata = IO.ReadCH(SystemCore.curDB.getDbName()+"/"+tableName);
        if(tdata == null) return new Result(false,TableNotExist,TableNotExist);
        DataTable dt = JSONObject.parseObject(tdata).getObject("table",DataTable.class);

        if(Lbrackets == 1){ // 没有指定插入列
            int l = std.indexOf("(");
            int r = std.indexOf(")");
            if(l+1 == r) return new Result(false,SyntaxError, NoInsertDataDefined);
            String s_value = std.substring(l+1,r);
            String[] values = s_value.split(",");
            if(dt.getItems().size() != values.length) return new Result(false,InsertDataNotMatch,InsertDataNotMatch);
            /** 检查数据合法性*/
            ArrayList<String> record = new ArrayList<String>();
            for (int i = 0; i < values.length; i++) {
                String cur_v = stdValue(values[i]);
                if(cur_v == null) return new Result(false,ILLIGAL_DATA,values[i]);
                if(dt.getItems().get(i).getPK()) { //该列为主键
                    if (dt.hasPkValue(dt.getItems().get(i).getName(), cur_v))
                        return new Result(false, PK_DUPLICATION, PK_DUPLICATION);
                    else dt.addPkValue(dt.getItems().get(i).getName(), cur_v);
                }
                InnerResult res = SupportType.illigalData(dt.getItems().get(i),cur_v);
                if(!res.isSuccess())
                    return new Result(false,ILLIGAL_DATA,cur_v);
                //record.add(res.getValue()); 改为全部存储字符串
                record.add(cur_v);
            }
            dt.getRecords().add(new DataRecord(record));
            JSONObject json = new JSONObject();
            json.put("table",dt);
            IO.WriteCH(SystemCore.getCurDB().getDbName()+"/"+tableName,json.toJSONString());
            return new Result(true,InsertSuccess,null);
        }else{ // 指定插入某些值
            return new Result(false,NotSupportSuchMethod,NotSupportSuchMethod);
        }
    }
    private static String stdValue(String value) {
        value = value.trim();
        if(value.startsWith("'") && value.length()>2){
            if(value.endsWith("'"))
                return value.substring(1,value.length()-1);
            return null;
        }else if(!value.contains("'")){
            return value;
        }
        return null;
    }

    /**
     * 计算std中的半括号数目
     * */
    private static int countBrackets(String std,char T) {
        int res = 0;
        for (int i = 0; i <std.length(); i++) {
            if(std.charAt(i) == T)
                res++;
        }
        return res;
    }
}
