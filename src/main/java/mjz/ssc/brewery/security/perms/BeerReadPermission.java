package mjz.ssc.brewery.security.perms;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME) // tells java compiler that this annotation should be retained at runtime, so reflection can be done at runtime, so the compiler can see this annotation
@PreAuthorize("hasAuthority('beer.read')")
public @interface BeerReadPermission {
}
