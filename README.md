# prometheus-metrics-parser-spring

### Using Prometheus Parser

```java

@Configuration
public class AppConfig {

    @Value("${spring.data.rest.connection.timeout}")
    private Long restConnectionTimeout;

    @Value("${spring.data.rest.connection.read-timeout}")
    private Long restReadTimeout;

    @Named("metrics")
    @Bean
    public RestTemplate restTemplateMetrics() {
        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
        MetricFormatHttpMessageConverter converter = new MetricFormatHttpMessageConverter();

        return restTemplateBuilder
                .messageConverters(converter)
                .setConnectTimeout(Duration.ofMillis(restConnectionTimeout))
                .setReadTimeout(Duration.ofMillis(restReadTimeout))
                .build();
    }
}

```