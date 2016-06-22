package fr.xebia.extras.selma.beans;

import java.util.List;
import java.util.Map;

public class RawCollectionBeanSource {
    private List<String> stringList;
    private Map<Integer, String> intMap;

    public List<String> getStringList() {
        return stringList;
    }

    public void setStringList(List<String> stringList) {
        this.stringList = stringList;
    }

    public Map<Integer, String> getIntMap() {
        return intMap;
    }

    public void setIntMap(Map<Integer, String> intMap) {
        this.intMap = intMap;
    }
}
