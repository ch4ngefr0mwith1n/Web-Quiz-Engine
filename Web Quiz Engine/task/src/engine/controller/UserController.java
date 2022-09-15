package engine.controller;

import engine.model.User;
import engine.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/api/register")
    public void register(@Valid @RequestBody User user) {

        String regexCheck = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        Pattern pattern = Pattern.compile(regexCheck);
        Matcher matcher = pattern.matcher(user.getEmail());

        if (user.getPassword().length() < 5 || !matcher.matches()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        userService.addUser(user);
    }
}
