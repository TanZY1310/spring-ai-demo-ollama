package spring.ai.example.spring_ai_demo.WeatherFunctions;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(value = "weather")
public record WeatherConfigProperties(String apiKey, String apiUrl) {

}
