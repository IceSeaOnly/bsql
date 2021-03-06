package binghai;

import binghai.Core.SystemCore;
import binghai.Opt.*;
import binghai.Std.Format;
import binghai.Std.IO;
import binghai.Std.Result;

import java.util.Scanner;

import static binghai.Main.CMD.*;

/**
 * Created by sdust on 2016/12/9.
 */
public class Main {


    public static void main(String[] args) {
        InitSystem();
        Scanner reader = new Scanner(System.in);
        String line = null;
        while ((line = reader.nextLine()) != null) {
            if (!CommandDispatcher(Format.formatOriSQL(line)))
                break;
        }
        SystemCore.flushCore();
        System.out.println("BSQL 已经停机.");
    }


    /**
     * 命令识别&分发中心,返回值为fasle时系统关闭
     */
    private static boolean CommandDispatcher(String std) {
        if (SystemCore.getCurUser() == null && !std.contains("bsql -u")) {
            System.out.println("请先登录B-SQL!");
            return true;
        }

        switch (CommandRecognize(std)) {
            case NODEFINE:
                System.out.println("无法识别的命令");
                break;
            case LOGIN:
                System.out.println(SystemCore.doLogin(std));
                break;
            case LOGOUT:
                System.out.println(SystemCore.doLogout());
                break;
            case SHUTDOWN:
                if (SystemCore.getCurUser().getUp().isShutdown())
                    return false;
                else System.out.println("当前用户无权停止bsql系统");
                break;
            case CREATE_DB:
                if (SystemCore.getCurUser().getUp().isCreateDB())
                    System.out.println(Create.CreateDB(std));
                else System.out.println(new Result(false, null, "当前用户无权创建数据库"));
                break;
            case USE_DB:
                System.out.println(Create.useDB(std));
                break;
            case CREATE_USER:
                if (SystemCore.getCurUser().getUp().isCreateUser()) {
                    System.out.println(Create.createUser(std));
                } else
                    System.out.println(new Result(false, "当前用户无权创建其他用户", null));
                break;
            case GRANT:
                if (SystemCore.getCurUser().getUp().isGrant()) {
                    System.out.println(Grant.grantDB2User(std));
                } else System.out.println(new Result(false, "该用户无权分配权限", null));
                break;
            case CREATE_TABLE:
                if (SystemCore.getCurDB() == null)
                    System.out.println(new Result(false, "尚未选定数据库！", "使用use DATABASENAME 选择一个数据库先?"));
                else if (SystemCore.getCurUser().getDbs().get(SystemCore.getCurDB().getDbName()).isCreateTable())
                    System.out.println(Create.createTable(std));
                else System.out.println(new Result(false, "无权创建数据库!", "无权创建数据库!"));
                break;
            case HELP_DATABASE:
                Help.HelpDataBase();
                break;
            case HELP_TABLE:
                if (SystemCore.getCurDB() == null)
                    System.out.println(new Result(false, "尚未选定数据库！", "使用use DATABASENAME 选择一个数据库先?"));
                else if (SystemCore.getCurUser().getDbs().get(SystemCore.getCurDB().getDbName()) != null)
                    Help.HelpTable();
                else
                    System.out.println(new Result(false, "无权查看", null));
                break;
            case INSERT:
                if (SystemCore.getCurDB() != null)
                    System.out.println(Insert.Insert(std));
                else System.out.println(new Result(false, "尚未选定数据库！", "使用use DATABASENAME 选择一个数据库先?"));
                break;
            case SELECT:
                if (SystemCore.getDbs() != null)
                    System.out.println(Select.select(std));
                else System.out.println(new Result(false, "尚未选定数据库！", "使用use DATABASENAME 选择一个数据库先?"));
                break;
            default:
                System.out.println("无法识别的命令");
        }
        return true;
    }

    /**
     * 定义命令种类
     */
    public enum CMD {
        NODEFINE, LOGIN, LOGOUT, SHUTDOWN, CREATE_USER, CREATE_DB, CREATE_TABLE, USE_DB,
        GRANT, HELP_DATABASE, HELP_TABLE, INSERT, SELECT
    }

    private static CMD CommandRecognize(String std) {
        if (std.startsWith("bsql -u"))
            return LOGIN;
        if (std.startsWith("exit") || std.startsWith("logout"))
            return LOGOUT;
        if (std.startsWith("shutdown"))
            return SHUTDOWN;
        if (std.startsWith("create database"))
            return CREATE_DB;
        if (std.startsWith("create user"))
            return CREATE_USER;
        if (std.startsWith("create table"))
            return CREATE_TABLE;
        if (std.startsWith("use"))
            return USE_DB;
        if (std.startsWith("grant"))
            return GRANT;
        if (std.startsWith("help database") || std.startsWith("show databases"))
            return HELP_DATABASE;
        if (std.startsWith("help table") || std.startsWith("show tables"))
            return HELP_TABLE;
        if (std.startsWith("insert into"))
            return INSERT;
        if (std.startsWith("select"))
            return SELECT;
        return NODEFINE;
    }


    private static void InitSystem() {
        IO.initSystem();
        System.out.println(SystemCore.SystemStart());
    }
}
