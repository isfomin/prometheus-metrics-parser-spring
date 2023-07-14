package ru.vtb.geo.systeminfo.component.parser;

import com.sun.istack.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MetricData {
    private final Map<String, List<AttributeStruct>> metrics = new HashMap<>();

    public void append(String propertyName, @Nullable Map<String, String> attributes, String value) {
        AttributeStruct attributeStruct = new AttributeStruct(attributes, value);
        List<AttributeStruct> list = metrics.get(propertyName);
        if (list == null) {
            list = new ArrayList<>();
        }
        list.add(attributeStruct);
        metrics.put(propertyName, list);
    }

    public List<AttributeStruct> getAttributes(String key) {
        return metrics.get(key);
    }

    @Override
    public String toString() {
        return "MetricData(size:" + metrics.size() + "){" +
                "metrics=" + metrics +
                '}';
    }
}
