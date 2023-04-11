package mjz.ssc.brewery.web.controllers;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mjz.ssc.brewery.domain.security.User;
import mjz.ssc.brewery.repositories.security.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final GoogleAuthenticator googleAuthenticator;

    @GetMapping("/register2fa")
    public String register2fa(Model model) {

        // since we are authenticated by username and password in first step, so we have the user on the spring security context, and we can access it from there
        User user = getUser();

        String url = GoogleAuthenticatorQRGenerator.getOtpAuthURL("MJZ-SPRING-SECURITY", user.getUsername(),
                googleAuthenticator.createCredentials(user.getUsername()));

        log.debug("Google QR URL: " + url);

        model.addAttribute("googleurl", url);

        return "user/register2fa";
    }

    @PostMapping//("/register2fa")
    public String confirm2fa(@RequestParam Integer verifyCode) { // the param name must be equal to the html form element Id

        User user = getUser();

        log.debug("Entered Code is: " + verifyCode );

        if(googleAuthenticator.authorizeUser(user.getUsername(), verifyCode)) { // gets the shared secret from database and verify the code with that

            User savedUser = userRepository.findById(user.getId()).orElseThrow();
            savedUser.setUseGoogle2fa(true); // the user completed registration for 2fa
            userRepository.save(savedUser);

            return "index";
        } else {
            // bad code
            return "user/register2fa";
        }


    }

    // since we are authenticated by username and password in first step, so we have the user on the spring security context, and we can access it
    private static User getUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
