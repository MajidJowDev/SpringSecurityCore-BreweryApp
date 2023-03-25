package mjz.ssc.brewery.web.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.DigestUtils;

public class PasswordEncodingTests {

    private final String PASSWORD = "password";

    @Test
    void testNoOp() {

        PasswordEncoder noOp = NoOpPasswordEncoder.getInstance(); //instantiate a noop password encoder

        System.out.println(noOp.encode(PASSWORD));
    }

    @Test
    void hashingExample() {
        // just to demonstrate how the hashing works
        System.out.println(DigestUtils.md5DigestAsHex(PASSWORD.getBytes()));
        System.out.println(DigestUtils.md5DigestAsHex(PASSWORD.getBytes()));

        String salted = PASSWORD + "ThisIsMySALTVALUE";
        System.out.println(DigestUtils.md5DigestAsHex(salted.getBytes()));
    }
}
