package mjz.ssc.brewery.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity  //Because spring security auto-configuration may not find everything on classpath, so it would use conditionals, we can add the security configs here
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(authorize -> {
                    authorize.antMatchers("/", "/webjars/**", "/login", "/resources/**" ).permitAll() // by this line we permit all requests, (intercepting authorize request). if we do not add "/webjars/**" and "/login" here the bootstrap scripts in webjars won't be loaded on page (loading will encounter error on browser)
                            .antMatchers("/beers/find", "/beers*").permitAll()
                            .antMatchers(HttpMethod.GET, "/api/v1/beer/**").permitAll();
                })
                .authorizeRequests()
                .anyRequest().authenticated() // if we want to do exceptions to everything being authenticated, we have to do it before this line (so in this case if we do the antMatchers part after this line we will get an error)
                .and()
                .formLogin().and()
                .httpBasic();
    }


}
