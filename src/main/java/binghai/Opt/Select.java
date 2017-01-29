package binghai.Opt;

import binghai.Core.*;
import binghai.Std.IO;
import binghai.Std.Result;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/16.
 */
public class Select {
    private static String SyntaxError = "语法错误";
    private static String TrySTD = "试试 select * from Table; /select a,b,c from Table ?";
    private static String NoRightForTable = "无权操作该数据表";
    private static String SyntaxNoSupport = "语法不支持";
    private static String TableNotExist = "数据表不存在或者已经损坏";
    private static String QuerySuccess = "查询成功";
    private static String NotSupportSuchMethod = "暂不支持条件查询";

    public static Result select(String std) {
        // select * from av
        // select id,name,sex from av
        // select * from av where id>10 and sex = 1
        String[] cmds = std.split(" ");
        if (cmds.length < 4)
            return new Result(false, SyntaxError, TrySTD);

        if (!cmds[2].equals("from"))
            return new Result(false, SyntaxNoSupport, SyntaxNoSupport);
        String tableName = cmds[3];
        String tdata = IO.ReadCH(SystemCore.curDB.getDbName() + "/" + tableName);
        if (tdata == null) return new Result(false, TableNotExist, TableNotExist);
        DataTable dt = JSONObject.parseObject(tdata).getObject("table", DataTable.class);

        if (!SystemCore.getCurUser().getDbs().get(SystemCore.getCurDB().getDbName()).getTbs().contains(tableName)) {
            return new Result(false, NoRightForTable, NoRightForTable);
        }

        /**
         * 无筛选，无查询条件类型
         * */
        if (cmds[1].equals("*") && cmds.length == 4) {
            StringBuilder sb = new StringBuilder();
            sb.append("\n");
            for (int i = 0; i < dt.getItems().size(); i++) {
                sb.append(stdOutPut(dt.getItems().get(i).getName(), 10));
                sb.append(" ");
            }
            sb.append("\n");
            sb.append(drawLine(dt.getItems().size() * 13));
            for (int i = 0; i < dt.getRecords().size(); i++) {
                DataRecord r = dt.getRecords().get(i);
                for (int j = 0; j < r.getData().size(); j++) {
                    sb.append(stdOutPut(r.getData().get(j), 10)); /** 每个值最多显示10个字符*/
                    sb.append(" ");
                }
                sb.append("\n" + drawLine(dt.getItems().size() * 13));
            }
            return new Result(true, QuerySuccess, sb.toString());

        } else if (cmds.length == 4) { /** 有筛选无条件的查询*/
            StringBuilder sb = new StringBuilder();
            sb.append("\n");
            for (int i = 0; i < dt.getItems().size(); i++) {
                if(cmds[1].contains(dt.getItems().get(i).getName())){
                    sb.append(stdOutPut(dt.getItems().get(i).getName(), 10));
                    sb.append(" ");
                }
            }
            sb.append("\n");
            sb.append(drawLine(cmds[1].split(",").length * 13));
            for (int i = 0; i < dt.getRecords().size(); i++) {
                DataRecord r = dt.getRecords().get(i);
                for (int j = 0; j < r.getData().size(); j++) {
                    if(cmds[1].contains(dt.getItems().get(j).getName())){
                        sb.append(stdOutPut(r.getData().get(j), 10)); /** 每个值最多显示10个字符*/
                        sb.append(" ");
                    }
                }
                sb.append("\n" + drawLine(cmds[1].split(",").length * 13));
            }
            return new Result(true, QuerySuccess, sb.toString());
        } else return new Result(false, NotSupportSuchMethod, NotSupportSuchMethod);

    }


    public static String stdOutPut(String ori, int stdLen) {
        if (ori.length() == stdLen)
            return ori + " |";
        if (ori.length() > stdLen)
            return ori.substring(0, stdLen) + " |";
        StringBuilder sb = new StringBuilder();
        sb.append(ori);
        for (int i = 0; i < stdLen - ori.length(); i++) {
            sb.append(" ");
        }
        sb.append(" |");
        return sb.toString();
    }

    public static String drawLine(int l) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < l; i++) {
            sb.append("-");
        }
        sb.append("\n");
        return sb.toString();
    }
}
