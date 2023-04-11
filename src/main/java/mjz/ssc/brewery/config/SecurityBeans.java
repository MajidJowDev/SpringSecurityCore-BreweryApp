package mjz.ssc.brewery.config;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorConfig;
import com.warrenstrange.googleauth.ICredentialRepository;
import mjz.ssc.brewery.security.SfgPasswordEncoderFactories;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;
import java.util.concurrent.TimeUnit;

@Configuration
public class SecurityBeans {

    @Bean
    PasswordEncoder passwordEncoder() { // by adding this, we can remove the {noop} from passwords in configure method
        //return NoOpPasswordEncoder.getInstance();
        //return new LdapShaPasswordEncoder();
        //return new StandardPasswordEncoder(); // Sha256
        //return new BCryptPasswordEncoder();

        // we can use Delegating Password encoder to use different password encoding methods
        // This way we can set a key for passowrds, so we can tell spring which encoding is going to work for each specific user
        //we can also define our algorithm for encoding
        //return PasswordEncoderFactories.createDelegatingPasswordEncoder(); // commented so we can use our implementaion below
        return SfgPasswordEncoderFactories.createDelegatingPasswordEncoder(); // our new custom encoder factories (could be useful for scenarios that we need to migrate some of passwords)

    }

    @Bean
    public GoogleAuthenticator googleAuthenticator(ICredentialRepository credentialRepository){
        GoogleAuthenticatorConfig.GoogleAuthenticatorConfigBuilder configBuilder
                = new GoogleAuthenticatorConfig.GoogleAuthenticatorConfigBuilder();

        configBuilder
                .setTimeStepSizeInMillis(TimeUnit.SECONDS.toMillis(60)) // based on UNIX time
                .setWindowSize(10)
                .setNumberOfScratchCodes(0); // since we are not generating scratch codes we set it to 0

        GoogleAuthenticator googleAuthenticator = new GoogleAuthenticator(configBuilder.build());
        googleAuthenticator.setCredentialRepository(credentialRepository); // works with the credential repositories that implemented ICredentialRepository
        return googleAuthenticator;

    }

    // added to manage and publish events related to authentication (log in times, expired logins, sign in locks etc)
    // this requires a listener as well
    @Bean
    public AuthenticationEventPublisher authenticationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        return new DefaultAuthenticationEventPublisher(applicationEventPublisher); // the default can be customized
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository(DataSource dataSource) {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource); // spring boot is going to create the datasource and that will be configured in spring context configured for H2 in-memory database
        return tokenRepository;
    }
}
