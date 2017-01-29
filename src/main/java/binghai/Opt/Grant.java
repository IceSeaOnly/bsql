package binghai.Opt;

import binghai.Core.DBPrivilege;
import binghai.Core.DataBase;
import binghai.Core.SystemCore;
import binghai.Std.Result;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/12/14.
 */
public class Grant {
    //GRANT privileges ON databasename.tablename TO 'username'
    //GRANT SELECT, INSERT ON test.user TO 'pig'
    //GRANT ALL ON *.* TO 'pig'

    public static Result grantDB2User(String std) {
        String[] cmds = std.split(" ");
        if (cmds.length < 6)
            return new Result(false, "语法不正确", "试试grant privileges ON databasename.tablename TO 'username'?");
        String ts = cmds[3];
        int flag = ts.indexOf(".");
        if(flag == ts.length())
            return new Result(false, "语法不正确", "试试grant privileges ON databasename.tablename TO 'username'?");

        String dbName = ts.substring(0,flag);
        String dbTable = ts.substring(flag+1,ts.length());
        ArrayList<String> tbs = new ArrayList<String>();

        for (DataBase d :
                SystemCore.getDbs()) {
            if (dbName.equals(d.getDbName()) || ts.equals("*.*")){
                for (int i = 0; i < d.getTbs().size(); i++) {
                    if(dbTable.equals(d.getTbs().get(i)) || ts.equals("*.*")){
                        tbs.add(d.getTbs().get(i));
                    }
                }
            }
        }

        String ps = cmds[1];
        DBPrivilege dbp = new DBPrivilege(
                ps.contains("delete"),
                ps.contains("drop"),
                ps.contains("createtable"),
                ps.contains("alter"),
                ps.contains("index"),
                ps.contains("insert"),
                ps.contains("select"),
                ps.contains("update"),
                ps.contains("all"),
                tbs
        );

        String username = cmds[5].substring(1,cmds[5].length()-1);
        SystemCore.getUser(username).grant(dbName,dbp);
        return null;
    }
}
