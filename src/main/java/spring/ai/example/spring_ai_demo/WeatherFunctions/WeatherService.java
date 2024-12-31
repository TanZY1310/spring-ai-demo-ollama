package spring.ai.example.spring_ai_demo.WeatherFunctions;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.web.client.RestClient;

import java.util.function.Function;

/*
   Weather API
   https://www.weatherapi.com/api-explorer.aspx
 */

public class WeatherService
        implements Function<WeatherService.WeatherRequest, WeatherService.WeatherResponse> {

    private static final Logger log = LoggerFactory.getLogger(WeatherService.class);
    private final RestClient restClient;
    private final WeatherConfigProperties weatherProps;

    public WeatherService(WeatherConfigProperties props) {
        this.weatherProps = props;
        log.info("Weather API URL: {}", weatherProps.apiUrl());
        log.info("Weather API Key: {}", weatherProps.apiKey());
        this.restClient = RestClient.create(weatherProps.apiUrl());
    }


    @Override
    public WeatherResponse apply(WeatherRequest weatherRequest) {
        log.info("Weather Request: {}",weatherRequest);
        WeatherResponse response = restClient.get()
                .uri("/current.json?key={key}&q={q}", weatherProps.apiKey(), weatherRequest.city())
                .retrieve()
                .body(WeatherResponse.class);
        log.info("Weather API Response: {}", response);
        return response;
    }

    // mapping the response of the Weather API to records. I only mapped the information I was interested in.
    public record WeatherRequest(String city) {}
    public record WeatherResponse(Location location,Current current) {}
    public record Location(String name, String region, String country, Long lat, Long lon){}
    public record Current(String temp_f, Condition condition, String wind_mph, String humidity) {}
    public record Condition(String text){}

}
