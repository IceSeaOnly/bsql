package binghai.Std;

/**
 * Created by sdust on 2016/12/9.
 * 统一执行结果类
 */
public class Result {
    private boolean success; // 执行是否成功
    private String reasult; // 执行结果，json
    private String info; // 执行后的信息

    public Result(boolean success, String reasult, String info) {
        this.success = success;
        this.reasult = reasult;
        this.info = info;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getReasult() {
        return reasult;
    }

    public String getInfo() {
        return info;
    }

    @Override
    public String toString() {
        return "执行"+(success?"成功":"失败")+"\n"+reasult+"\n信息:"+info;
    }
}
