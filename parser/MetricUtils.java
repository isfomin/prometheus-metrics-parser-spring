package ru.vtb.geo.systeminfo.component.parser;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.function.Predicate;

public class MetricUtils {

    public static <T extends SummarizedDouble> double summarizeList(List<T> list, Predicate<T> filterPredicate) {
        return list.stream()
                .filter(filterPredicate)
                .mapToDouble(T::getDoubleValue)
                .sum();
    }

    public static boolean totalMemoryUsedFilter(AttributeStruct attributeStruct) {
        return true;
    }

    public static boolean onlyHeapMemoryUsedFilter(AttributeStruct attributeStruct) {
        assert attributeStruct.attributes() != null;
        return attributeStruct.attributes().containsValue("heap");
    }

    public static boolean onlyNonHeapMemoryUsedFilter(AttributeStruct attributeStruct) {
        assert attributeStruct.attributes() != null;
        return attributeStruct.attributes().containsValue("nonheap");
    }

    public static String getFormatValueCpuUsage(AttributeStruct processCpuUsageAttribute) {
        return String.format("%.02f%%", processCpuUsageAttribute.getDoubleValue() * 100)
                .replaceAll(",", ".");
    }

    public static long getTotalMemoryUsedBytes(List<AttributeStruct> jvmMemoryUsedAttributeList) {
        double summedTotalMemoryUsed = summarizeList(jvmMemoryUsedAttributeList, MetricUtils::totalMemoryUsedFilter);
        return Math.round(summedTotalMemoryUsed);
    }

    public static long getHeapMemoryUsedBytes(List<AttributeStruct> jvmMemoryUsedAttributeList) {
        double summedHeapMemoryUsed = summarizeList(jvmMemoryUsedAttributeList, MetricUtils::onlyHeapMemoryUsedFilter);
        return Math.round(summedHeapMemoryUsed);
    }

    public static long getNonHeapMemoryUsedBytes(List<AttributeStruct> jvmMemoryUsedAttributeList) {
        double summedNonHeapMemoryUsed = summarizeList(jvmMemoryUsedAttributeList, MetricUtils::onlyNonHeapMemoryUsedFilter);
        return Math.round(summedNonHeapMemoryUsed);
    }

    public static BufferedReader getBufferedReader(String input) {
        return new BufferedReader(new InputStreamReader(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8))));
    }

    public static String convertBytes(long bytes, MemoryUnit memoryUnit) {
        long convertedBytes =  switch (memoryUnit) {
            case KB -> bytes >> 10;
            case MB -> bytes >> 20;
            case GB -> bytes >> 30;
            case TB -> bytes >> 40;
        };

        return convertedBytes + memoryUnit.toString();
    }

    public enum MemoryUnit {
        KB, MB, GB, TB
    }
}
