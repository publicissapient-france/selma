package fr.xebia.extras.selma.beans;

import java.util.List;
import java.util.Map;

/**
 * @author Christopher Ng
 */
public class RawBean {
    private Object object;
    private List rawList;
    private Map rawMap;

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public List getRawList() {
        return rawList;
    }

    public void setRawList(List rawList) {
        this.rawList = rawList;
    }

    public Map getRawMap() {
        return rawMap;
    }

    public void setRawMap(Map rawMap) {
        this.rawMap = rawMap;
    }
}
