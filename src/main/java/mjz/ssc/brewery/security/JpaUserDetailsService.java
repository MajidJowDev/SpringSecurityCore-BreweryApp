package mjz.ssc.brewery.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mjz.ssc.brewery.domain.security.Authority;
import mjz.ssc.brewery.domain.security.User;
import mjz.ssc.brewery.repositories.security.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class JpaUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository; // project lombok creates the required constructor automatically for this repository and inject it


    @Transactional // we can also set our JPA configuration to eagerly fetch data for authorities or use Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.debug("Getting User info via JPA");

        User user = userRepository.findByUsername(username).orElseThrow(() -> {
            return new UsernameNotFoundException("User name: " + username + " not found!");
        });

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                user.isEnabled(), user.isAccountNonExpired(), user.isCredentialsNonExpired(), user.isAccountNonLocked(), 
                convertToSpringAuthorities(user.getAuthorities()));
    }

    private Collection<? extends GrantedAuthority> convertToSpringAuthorities(Set<Authority> authorities) {

        if(authorities != null && authorities.size() > 0) {
            return authorities.stream()
                    .map(Authority :: getRole)
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toSet());
        } else {
            return new HashSet<>();
        }
    }
}
