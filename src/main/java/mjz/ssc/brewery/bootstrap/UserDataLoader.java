package mjz.ssc.brewery.bootstrap;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mjz.ssc.brewery.domain.security.Authority;
import mjz.ssc.brewery.domain.security.User;
import mjz.ssc.brewery.repositories.security.AuthorityRepository;
import mjz.ssc.brewery.repositories.security.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class UserDataLoader implements CommandLineRunner {

    private final AuthorityRepository authorityRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public void run(String... args) throws Exception {
        if (authorityRepository.count() == 0) {
            loadSecurityData();
        }
    }


    private void loadSecurityData() {
        Authority admin = authorityRepository.save(Authority.builder().role("ROLE_ADMIN").build());
        Authority userRole = authorityRepository.save(Authority.builder().role("ROLE_USER").build());
        Authority customer = authorityRepository.save(Authority.builder().role("ROLE_CUSTOMER").build());

        userRepository.save(User.builder()
                .username("springuser")
                .password(passwordEncoder.encode("springpass"))
                .authority(admin)
                .build());

        userRepository.save(User.builder()
                .username("user")
                .password(passwordEncoder.encode("pass"))
                .authority(userRole)
                .build());

        userRepository.save(User.builder()
                .username("ali")
                .password(passwordEncoder.encode("test"))
                .authority(customer)
                .build());

        log.debug("Users Loaded: " + userRepository.count());
    }
}
