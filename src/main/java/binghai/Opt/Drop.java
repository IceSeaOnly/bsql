package binghai.Opt;

import binghai.Std.IO;
import binghai.Std.Result;

/**
 * Created by Administrator on 2016/12/9.
 * 删除数据表
 */
public class Drop {
    public static Result dropTable(String tableName){
        return IO.delete(tableName);
    }
}
