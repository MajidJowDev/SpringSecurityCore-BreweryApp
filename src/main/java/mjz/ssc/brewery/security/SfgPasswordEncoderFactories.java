package mjz.ssc.brewery.security;

import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;

import java.util.HashMap;
import java.util.Map;

public class SfgPasswordEncoderFactories {

    //we get this snippet originally from Spring main PasswordEncoderFactories
    // so now we set  up our own custom implementation of the Delegating Password Encoder
    public static PasswordEncoder createDelegatingPasswordEncoder() {
        String encodingId = "bcrypt";
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put(encodingId, new BCryptPasswordEncoder());
        encoders.put("ldap", new org.springframework.security.crypto.password.LdapShaPasswordEncoder());
        //encoders.put("MD4", new org.springframework.security.crypto.password.Md4PasswordEncoder());
        //encoders.put("MD5", new org.springframework.security.crypto.password.MessageDigestPasswordEncoder("MD5"));
        encoders.put("noop", org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance());
        //encoders.put("pbkdf2", new Pbkdf2PasswordEncoder());
        //encoders.put("scrypt", new SCryptPasswordEncoder());
        //encoders.put("SHA-1", new org.springframework.security.crypto.password.MessageDigestPasswordEncoder("SHA-1"));
        //encoders.put("SHA-256", new org.springframework.security.crypto.password.MessageDigestPasswordEncoder("SHA-256"));
        encoders.put("sha256", new org.springframework.security.crypto.password.StandardPasswordEncoder());
        //encoders.put("argon2", new Argon2PasswordEncoder());

        return new DelegatingPasswordEncoder(encodingId, encoders);
    }

    //we set this constructor private, so it can not get instantiated
    private SfgPasswordEncoderFactories() {
    }
}
