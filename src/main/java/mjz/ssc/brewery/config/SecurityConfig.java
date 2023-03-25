package mjz.ssc.brewery.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity  //Because spring security auto-configuration may not find everything on classpath, so it would use conditionals, we can add the security configs here
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(authorize -> {
                    authorize.antMatchers("/", "/webjars/**", "/login", "/resources/**" ).permitAll() // by this line we permit all requests, (intercepting authorize request). if we do not add "/webjars/**" and "/login" here the bootstrap scripts in webjars won't be loaded on page (loading will encounter error on browser)
                            .antMatchers("/beers/find", "/beers*").permitAll()
                            .antMatchers(HttpMethod.GET, "/api/v1/beer/**").permitAll()
                            .mvcMatchers(HttpMethod.GET, "/api/v1/beerUpc/{upc}").permitAll(); // we do not have the ant matchers wild card, instead we have path param
                })
                .authorizeRequests()
                .anyRequest().authenticated() // if we want to do exceptions to everything being authenticated, we have to do it before this line (so in this case if we do the antMatchers part after this line we will get an error)
                .and()
                .formLogin().and()
                .httpBasic();
    }

    @Bean
    PasswordEncoder passwordEncoder() { // by adding this, we can remove the {noop} from passwords in configure method
        //return NoOpPasswordEncoder.getInstance();
        //return new LdapShaPasswordEncoder();
        return new StandardPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("springuser")
                .password("springpass") // by adding the passewordEncoder method (Bean), we can remove {noop}
                //.password("{noop}springpass") // we have to define a password encoder for spring security, otherwise we get error (There is no PasswordEncoder mapped for id "null"), here we use the {noop} password encoder
                .roles("ADMIN")
                .and() // we can add users in separate auth.inMemoryAuthentication calls or just use .and()
                .withUser("user")
                .password("11419772f752d13497f2c5f7b3b98a2fc3def9e154c33099d8111da57c3ba32f6a22e182977c58c9") // Sha256
                //.password("{SSHA}ikS5HECYlHYK8K4QWFIBzIhOCCxWM8LXOn3rjA==") // for Ldap we use the hashed password
                //.password("pass") // by adding the passewordEncoder method (Bean), we can remove {noop}
                //.password("{noop}pass")
                .roles("USER");

        auth.inMemoryAuthentication().withUser("ali").password("test").roles("CUSTOMER");
        //auth.inMemoryAuthentication().withUser("springuser").password("springpass").roles("ADMIN");
        //auth.inMemoryAuthentication().withUser("user").password("pass").roles("USER");
    }

    /*
    //However this approach is deprecated
    //this is an in-memory user manager (as a user DAO in spring security context), with this method we can get define and load the user specific data/info into spring context
    @Override
    @Bean
    protected UserDetailsService userDetailsService() {
        UserDetails admin = User.withDefaultPasswordEncoder() // added this user here, instead of defining it in app.properties
                .username("springuser")
                .password("springpass")
                .roles("ADMIN") // we need to define a security role otherwise spring security will be unhappy
                .build();

        UserDetails user = User.withDefaultPasswordEncoder()
                .username("user")
                .password("pass")
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(admin, user);

    }
    */


}
