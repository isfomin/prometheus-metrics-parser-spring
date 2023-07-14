package ru.vtb.geo.systeminfo.component.parser;

import com.sun.istack.Nullable;

import java.util.Map;

public record AttributeStruct(@Nullable Map<String, String> attributes, String value) implements SummarizedDouble {

    @Override
    public Double getDoubleValue() {
        Double doubleValue = null;
        try {
            doubleValue = Double.parseDouble(value);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return doubleValue;
    }
}
