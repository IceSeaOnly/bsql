package binghai.Core;

import java.util.List;

/**
 * Created by Administrator on 2016/12/10.
 */
public class DataTable {
    private List<PrimaryKey> pks;
    private List<DataItem> items;
    private List<DataRecord> records;

    public DataTable(List<PrimaryKey> pks, List<DataItem> items, List<DataRecord> records) {
        this.pks = pks;
        this.items = items;
        this.records = records;
    }

    public DataTable() {
    }

    public List<PrimaryKey> getPks() {
        return pks;
    }

    public void setPks(List<PrimaryKey> pks) {
        this.pks = pks;
    }

    public List<DataItem> getItems() {
        return items;
    }

    public void setItems(List<DataItem> items) {
        this.items = items;
    }

    public List<DataRecord> getRecords() {
        return records;
    }

    public void setRecords(List<DataRecord> records) {
        this.records = records;
    }

    /** 名为name的主键是否含有v值*/
    public boolean hasPkValue(String name, String v) {
        for (int i = 0; i < pks.size(); i++) {
            if(pks.get(i).getPkName().equals(name)){
                return pks.get(i).getPkValues().contains(v);
            }
        }
        return false;
    }

    public void addPkValue(String name, String cur_v) {
        for (int i = 0; i < pks.size(); i++) {
            if(pks.get(i).getPkName().equals(name)){
                pks.get(i).getPkValues().add(cur_v);
            }
        }
    }
}
