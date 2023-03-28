package mjz.ssc.brewery.security;

import lombok.extern.slf4j.Slf4j;
import mjz.ssc.brewery.domain.security.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.UUID;


// in spring security we can define our beans and utilize those beans to perform authentication checks as we are
// working with spring security configuration, so we extend our spring expression language so we can access to a custom bean
@Slf4j
@Component
public class BeerOrderAuthenticationManager {

    public boolean customerIdMatches(Authentication authentication, UUID customerId) { // take in spring security context by bringing in Authentication object
        User authenticatedUser = (User) authentication.getPrincipal();

        log.debug("Authenticated User Customer Id: " + authenticatedUser.getCustomer().getId() + " Customer Id: " + customerId);

        return authenticatedUser.getCustomer().getId().equals(customerId);

        // the next step would be wiring this Authentication manager to spring security configuration

    }

}
