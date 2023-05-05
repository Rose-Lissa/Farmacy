package our.pharmacy.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import our.pharmacy.dto.ConsumerRegistrationForm;
import our.pharmacy.dto.WorkerRegistrationForm;
import our.pharmacy.service.UserService;

@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/signup")
    public void signUpWorker(@Valid ConsumerRegistrationForm registrationForm) {
        userService.signUpConsumer(registrationForm);
    }

    @PostMapping("admin/signup")
    public void signUpWorker(@Valid WorkerRegistrationForm registrationForm) {
        userService.signUpWorker(registrationForm);
    }
}
