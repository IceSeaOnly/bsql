package binghai.Core;

import java.util.List;

/**
 * Created by Administrator on 2016/12/15.
 * 主键
 */
public class PrimaryKey {
    private String pkName;
    private List<String> pkValues;

    public PrimaryKey(String pkName, List<String> pkValues) {
        this.pkName = pkName;
        this.pkValues = pkValues;
    }

    public PrimaryKey() {
    }

    public String getPkName() {
        return pkName;
    }

    public void setPkName(String pkName) {
        this.pkName = pkName;
    }

    public List<String> getPkValues() {
        return pkValues;
    }

    public void setPkValues(List<String> pkValues) {
        this.pkValues = pkValues;
    }
}
