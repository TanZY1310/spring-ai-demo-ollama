package spring.ai.example.spring_ai_demo.WeatherFunctions;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

import java.util.function.Function;

@Configuration
public class FunctionConfig {

    private final WeatherConfigProperties props;

    public FunctionConfig(WeatherConfigProperties props) {
        this.props = props;
    }

    @Bean
    @Description("Get the current weather conditions for the given city.")
    public Function<WeatherService.WeatherRequest, WeatherService.WeatherResponse> currentWeatherFunction() {
        return new WeatherService(props);
    }
}
