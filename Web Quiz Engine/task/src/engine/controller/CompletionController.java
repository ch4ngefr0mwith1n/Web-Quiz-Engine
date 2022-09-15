package engine.controller;

import engine.model.Completion;
import engine.model.User;
import engine.service.CompletionService;
import engine.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CompletionController {

    private CompletionService completionService;
    private UserService userService;

    @Autowired
    public CompletionController(CompletionService completionService, UserService userService) {
        this.completionService = completionService;
        this.userService = userService;
    }

    @GetMapping("api/quizzes/completed")
    public Page<Completion> getCompletions(@AuthenticationPrincipal UserDetails userDetails,
                                           @RequestParam(defaultValue = "0") Integer page,
                                           @RequestParam(defaultValue = "10") Integer pageSize) {

        User currentUser = userService.findByEmail(userDetails.getUsername()).get();

        return completionService.getCompletions(currentUser, page, pageSize);

    }
}
