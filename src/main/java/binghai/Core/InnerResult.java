package binghai.Core;

/**
 * Created by Administrator on 2016/12/15.
 */
public class InnerResult {
    boolean success;
    Object value;

    public InnerResult(boolean success, Object value) {
        this.success = success;
        this.value = value;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
