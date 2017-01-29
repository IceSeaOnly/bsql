package binghai.Core;

/**
 * Created by Administrator on 2016/12/9.
 * 数据项
 */
public class DataItem {
    private String name;
    private String classType;
    private int maxlength;
    private Boolean PK; /** 是否是主键*/
    private Boolean autoIncrement; /** 是否自动增长*/
    private Boolean not_null; /** 是否不为空*/
    private Boolean default_null; /** 是否默认为null*/
    private Boolean hasIndex; /** 是否有索引*/

    public DataItem(String name, String classType, int maxlength, Boolean PK, Boolean autoIncrement, Boolean not_null, Boolean default_null, Boolean hasIndex) {
        this.name = name;
        this.classType = classType;
        this.maxlength = maxlength;
        this.PK = PK;
        this.autoIncrement = autoIncrement;
        this.not_null = not_null;
        this.default_null = default_null;
        this.hasIndex = hasIndex;
    }

    public DataItem() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassType() {
        return classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }

    public int getMaxlength() {
        return maxlength;
    }

    public void setMaxlength(int maxlength) {
        this.maxlength = maxlength;
    }

    public Boolean getPK() {
        return PK;
    }

    public void setPK(Boolean PK) {
        this.PK = PK;
    }

    public Boolean getAutoIncrement() {
        return autoIncrement;
    }

    public void setAutoIncrement(Boolean autoIncrement) {
        this.autoIncrement = autoIncrement;
    }

    public Boolean getNot_null() {
        return not_null;
    }

    public void setNot_null(Boolean not_null) {
        this.not_null = not_null;
    }

    public Boolean getDefault_null() {
        return default_null;
    }

    public void setDefault_null(Boolean default_null) {
        this.default_null = default_null;
    }

    public Boolean getHasIndex() {
        return hasIndex;
    }

    public void setHasIndex(Boolean hasIndex) {
        this.hasIndex = hasIndex;
    }
}
