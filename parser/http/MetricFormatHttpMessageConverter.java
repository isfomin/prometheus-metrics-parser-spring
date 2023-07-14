package ru.vtb.geo.systeminfo.component.parser.http;

import io.micrometer.core.lang.NonNullApi;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import ru.vtb.geo.systeminfo.component.parser.MetricData;
import ru.vtb.geo.systeminfo.component.parser.PrometheusMetricsParser;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

@NonNullApi
@Component
public class MetricFormatHttpMessageConverter extends AbstractHttpMessageConverter<MetricData> {
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private final PrometheusMetricsParser parser = new PrometheusMetricsParser();

    public MetricFormatHttpMessageConverter() {
        this(DEFAULT_CHARSET);
    }

    public MetricFormatHttpMessageConverter(Charset defaultCharset) {
        super(defaultCharset, MediaType.TEXT_PLAIN);
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return MetricData.class == clazz;
    }

    @Override
    protected MetricData readInternal(Class<? extends MetricData> clazz, HttpInputMessage inputMessage) throws IOException {
        return parser.parse(inputMessage.getBody());
    }

    @Override
    protected void writeInternal(MetricData object, HttpOutputMessage outputMessage) throws IOException {
        HttpHeaders headers = outputMessage.getHeaders();
        if (headers.get("Accept-Charset") == null) {
            headers.setAcceptCharset(List.of(DEFAULT_CHARSET));
        }
        if (headers.get("Content-Type") == null) {
            headers.setContentType(MediaType.TEXT_PLAIN);
        }

        StreamUtils.copy(object.toString(), DEFAULT_CHARSET, outputMessage.getBody());
    }
}
