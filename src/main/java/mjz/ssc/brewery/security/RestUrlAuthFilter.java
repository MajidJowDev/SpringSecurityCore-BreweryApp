package mjz.ssc.brewery.security;

import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;

// to pass the username and password as params
public class RestUrlAuthFilter extends AbstractRestAuthFilter{
    public RestUrlAuthFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
        super(requiresAuthenticationRequestMatcher);
    }

    protected String getPassword(HttpServletRequest request) {
        return request.getParameter("apiSecret");
    }

    protected String getUsername(HttpServletRequest request) {
        return request.getParameter("apiKey");
    }


}
