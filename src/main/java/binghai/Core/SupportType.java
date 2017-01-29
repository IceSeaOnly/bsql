package binghai.Core;

import binghai.Std.Result;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by sdust on 2016/12/9.
 * 支持的存储类型定义
 */
public class SupportType {
    private static Map<String,Class> table = null;
    private static void initMap(){
        table = new HashMap<String, Class>();
        table.put("int",Integer.class);
        table.put("bigint",Long.class);
        table.put("double",Double.class);
        table.put("float",Float.class);
        table.put("boolean",Boolean.class);
        table.put("varchar",String.class);
        table.put("char",char.class);
        table.put("smallint",int.class);
    }
    public static InnerResult illigalData(DataItem dataItem, String cur_v) {
        String t = dataItem.getClassType();
        if(t.equals("int") || t.equals("smallint")){
            try {
                Integer v = Integer.parseInt(cur_v);
                return new InnerResult(true,v);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                new InnerResult(false,null);
            }
        }else if(t.equals("bigint")){
            try {
                Long v = Long.parseLong(cur_v);
                return new InnerResult(true,v);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                new InnerResult(false,null);
            }
        }else if(t.equals("double")){
            try {
                Double v = Double.parseDouble(cur_v);
                return new InnerResult(true,v);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                new InnerResult(false,null);
            }
        }else if(t.equals("float")){
            try {
                Float v = Float.parseFloat(cur_v);
                return new InnerResult(true,v);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                new InnerResult(false,null);
            }
        }else if(t.equals("boolean")){
            if(cur_v.equals("true") || cur_v.equals("0"))
                return new InnerResult(true,new Boolean(true));
            else if(cur_v.equals("false") || cur_v.equals("1"))
                return new InnerResult(true,new Boolean(false));
            return new InnerResult(false,null);
        }else if(t.equals("varchar")){
            return new InnerResult(true,cur_v);
        }else if(t.equals("char")){
            if(cur_v.length()>1)
                return new InnerResult(false,null);
            return new InnerResult(true,cur_v.charAt(0));
        }
        return new InnerResult(false,null);
    }
    /** 根据类型名查询是否为支持的类型*/
    public static Result isSupport(String typename){
        if(table == null) initMap();
        Set<String> names = table.keySet();
        Iterator it = names.iterator();
        while(it.hasNext()){
            if(it.next().equals(typename))
                    return new Result(true,null,"支持"+typename+"类型.");
        }
        return new Result(false,null,"不支持"+typename+"类型.");
    }

    /** 根据类型名返回对应的类型*/
    public static Class getClassByName(String className) throws Exception{
        if(table == null) initMap();
        Set<String> names = table.keySet();
        Iterator it = names.iterator();
        while(it.hasNext()){
            if(it.next().equals(className))
                return table.get(className);
        }
        throw new Exception("发现不支持的"+className+"类型");
    }

    /** 获取所有支持的类型*/
    public static String[] getAllSupportType(){
        if(table == null) initMap();
        Set<String> names = table.keySet();
        return names.toArray(new String[]{});
    }


}
