package mjz.ssc.brewery.security.google;


import lombok.extern.slf4j.Slf4j;
import mjz.ssc.brewery.domain.security.User;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.boot.autoconfigure.security.servlet.StaticResourceRequest;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class Google2faFilter extends GenericFilterBean {

    // we need to use the new impl, because it's not normally in spring context
    private final AuthenticationTrustResolver authenticationTrustResolver = new AuthenticationTrustResolverImpl(); // for checking if the authentication is not anonymous
    private final Google2faFailureHandler google2faFailureHandler = new Google2faFailureHandler();
    private final RequestMatcher urlIs2fa = new AntPathRequestMatcher("/user/verify2fa");
    private final RequestMatcher urlResource = new AntPathRequestMatcher("/resources/**");
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //Spring MVC way to get static resources
        //Quick way to set up matcher for all general static resources like .js files, .css files, images
        StaticResourceRequest.StaticResourceRequestMatcher staticResourceRequestMatcher = PathRequest.toStaticResources().atCommonLocations();

        if(urlIs2fa.matches(request) || // to avoid getting into endless loop
                urlResource.matches(request) ||
                staticResourceRequestMatcher.matches(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // check to see if we are authenticated and not anonymous
        if(authentication != null && !authenticationTrustResolver.isAnonymous(authentication)) {
            log.debug("Processing 2FA Filter");

            if(authentication.getPrincipal() != null && authentication.getPrincipal() instanceof User) {

                User user = (User) authentication.getPrincipal();

                if(user.getUseGoogle2fa() && user.getGoogle2faRequired()) {
                    log.debug("2FA Required");

                    google2faFailureHandler.onAuthenticationFailure(request, response, null);
                    return; // because if we fail on authentication we do not want to continue to execute filterchain
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}
