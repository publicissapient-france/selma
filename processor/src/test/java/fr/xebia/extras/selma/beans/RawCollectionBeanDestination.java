package fr.xebia.extras.selma.beans;

import java.util.List;
import java.util.Map;

public class RawCollectionBeanDestination {
    private List stringList;
    private Map intMap;

    public Map getIntMap() {
        return intMap;
    }

    public void setIntMap(Map intMap) {
        this.intMap = intMap;
    }

    public List getStringList() {
        return stringList;
    }

    public void setStringList(List stringList) {
        this.stringList = stringList;
    }
}
