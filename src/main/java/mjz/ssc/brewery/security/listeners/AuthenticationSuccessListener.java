package mjz.ssc.brewery.security.listeners;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuthenticationSuccessListener {

    @EventListener // registering the method as an event listener so Spring looks for an event with this annotation and when we have an event with "AuthenticationSuccess" type, this listener get invoked
    public void listen(AuthenticationSuccessEvent event) {
        log.debug("User Logged in okay");
    }
}
