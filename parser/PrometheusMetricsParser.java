package ru.vtb.geo.systeminfo.component.parser;

import lombok.NonNull;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class PrometheusMetricsParser {
    private static final String FORMAT_LINE_PATTERN = "(^\\w+)(\\{.*})* ([\\dE.-]+)";
    private static final String COMMENT_SIGN = "#";
    private static final String ATTRIBUTE_SPLITTER = ",";
    private static final String KEY_VALUE_SPLITTER = "=";

    public MetricData parse(BufferedReader bufferedReader) throws IOException {
        Pattern pattern = Pattern.compile(FORMAT_LINE_PATTERN);
        MetricData metricData = new MetricData();

        for (String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine()) {
            if (line.startsWith(COMMENT_SIGN)) continue;
            try {
                Matcher matcher = pattern.matcher(line);
                boolean matches = matcher.matches();
                int groupCount = matcher.groupCount();
                if (matches && groupCount == 3) {
                    String propertyName = matcher.group(1);
                    String attributesLine = matcher.group(2);
                    String value = matcher.group(3);
                    if (attributesLine != null) {
                        Map<String, String> attributes = parseAttributes(attributesLine);
                        metricData.append(propertyName, attributes, value);
                    } else {
                        metricData.append(propertyName, null, value);
                    }
                }
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }

        bufferedReader.close();

        return metricData;
    }

    public MetricData parse(InputStream inputStream) throws IOException {
        return parse(new BufferedReader(new InputStreamReader(inputStream)));
    }

    private Map<String, String> parseAttributes(@NonNull String attributesLine) {
        String unwrappedAttributesLine = unwrapLine(attributesLine);
        String[] attributesParts = unwrappedAttributesLine.split(ATTRIBUTE_SPLITTER);
        Map<String, String> result = new HashMap<>();

        for (String attributeLine : attributesParts) {
            String[] pair = attributeLine.split(KEY_VALUE_SPLITTER);
            result.put(pair[0], unwrapLine(pair[1]));
        }

        return result;
    }

    private String unwrapLine(String line) {
        return line.substring(1, line.length() - 1);
    }
}
