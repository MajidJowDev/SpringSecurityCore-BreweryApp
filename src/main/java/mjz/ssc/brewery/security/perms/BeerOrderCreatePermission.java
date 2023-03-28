package mjz.ssc.brewery.security.perms;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasAuthority('order.create') OR " +
        " hasAuthority('customer.order.create') AND " +
        " @beerOrderAuthenticationManager.customerIdMatches(authentication, #customerId)") // name of the bean should start with lowercase, and for referencing the bean we have to use @
//authentication tells spring security to provide authentication object into this method
public @interface BeerOrderCreatePermission {
}
