package mjz.ssc.brewery.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // when we want to manage all methods across the app we can use this way, this is global (using wildcars for all urls)
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**").allowedMethods("GET", "POST", "PUT");
//
//        //WebMvcConfigurer.super.addCorsMappings(registry);
//    }
}
