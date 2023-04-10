package mjz.ssc.brewery.security.listeners;

import lombok.extern.slf4j.Slf4j;
import mjz.ssc.brewery.domain.security.User;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuthenticationSuccessListener {

    @EventListener // registering the method as an event listener so Spring looks for an event with this annotation and when we have an event with "AuthenticationSuccess" type, this listener get invoked
    public void listen(AuthenticationSuccessEvent event) {
        log.debug("User Logged in okay");

        //because we are using simple java objects (event.getSource()) we have to use casting for the object
        if(event.getSource() instanceof UsernamePasswordAuthenticationToken) {
            UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken)event.getSource();

            if(token.getPrincipal() instanceof User) {

                User user = (User) token.getPrincipal();

                log.debug("Username logged in: " + user.getUsername());
            }

            if(token.getDetails() instanceof WebAuthenticationDetails) {
                WebAuthenticationDetails details = (WebAuthenticationDetails) token.getDetails();
                log.debug("Source IP: " + details.getRemoteAddress());
                log.debug("Session Id: " + details.getSessionId());
            }
        }
    }
}
