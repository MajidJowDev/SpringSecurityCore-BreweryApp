package mjz.ssc.brewery.web.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mjz.ssc.brewery.repositories.security.UserRepository;
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

    @GetMapping("/register2fa")
    public String register2fa(Model model) {

        model.addAttribute("googleurl", "todo");

        return "user/register2fa";
    }

    @PostMapping//("/register2fa")
    public String confirm2fa(@RequestParam Integer verifyCode) { // the param name must be equal to the html form element Id

        //todo - implement
        return "index";
    }

}
