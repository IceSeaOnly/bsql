package binghai.Opt;

import binghai.Core.SystemCore;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/12/14.
 */
public class Help {

    public static void HelpDataBase() {
        String[] keys = SystemCore.getCurUser().getDbs().keySet().toArray(new String[SystemCore.getCurUser().getDbs().size()]);
        for (int i = 0; i < keys.length; i++) {
            System.out.println(keys[i]);
        }
        System.out.println("--------------------------- end");
    }

    public static void HelpTable() {
        ArrayList<String> tbs = (ArrayList<String>) SystemCore.getCurUser().getDbs().get(SystemCore.getCurDB().getDbName()).getTbs();
        for (String s :
                tbs) {
            System.out.println(s);
        }
        System.out.println("--------------------------- end");
    }
}
