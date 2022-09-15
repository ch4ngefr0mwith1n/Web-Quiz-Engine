package engine.controller;

import engine.exceptions.QuizNotFoundException;
import engine.handlers.UserAnswer;
import engine.model.Completion;
import engine.model.Quiz;
import engine.model.User;
import engine.service.CompletionService;
import engine.service.QuizService;
import engine.handlers.AnswerCheckHandler;
import engine.handlers.QuizHandler;
import engine.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.Optional;

@RestController
public class QuizController {
    private QuizService quizService;
    private UserService userService;
    private CompletionService completionService;

    @Autowired
    public QuizController(QuizService quizService, UserService userService, CompletionService completionService) {
        this.quizService = quizService;
        this.userService = userService;
        this.completionService = completionService;
    }

    @GetMapping("/api/quizzes")
    public Page<Quiz> getAllQuizzes(@RequestParam(defaultValue = "0") Integer page,
                                    @RequestParam(defaultValue = "10") Integer pageSize) {
        return quizService.getAllQuizzes(page, pageSize);
    }

    @PostMapping("/api/quizzes/{id}/solve")
    public ResponseEntity<Object> solveQuiz(@PathVariable(value = "id") Long id,
                                            @Valid @RequestBody UserAnswer userAnswer,
                                            @AuthenticationPrincipal UserDetails userDetails) {

        Optional<Quiz> optQuiz  = quizService.findByID(id);

        if (optQuiz.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        boolean isCorrect = quizService.checkSubmission(id, userAnswer.getAnswer());
        if (isCorrect) {
            // pronalazimo trenutnog korisnika:
            User currentUser = userService.findByEmail(userDetails.getUsername()).get();
            // nakon toga sačuvamo podatke o uspješno odrađenom kvizu:
            completionService.save(new Completion(currentUser, id));
        }

        return AnswerCheckHandler.generateResponse(
                isCorrect ? true : false,
                HttpStatus.OK,
                isCorrect ? "Congratulations, you're right!" : "Wrong answer! Please, try again."
        );
    }

    @DeleteMapping("/api/quizzes/{id}")
    public ResponseEntity<Quiz> deleteQuizByID(@PathVariable Long id,
                                               @AuthenticationPrincipal UserDetails userDetails) {

        quizService.delete(id, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

    @GetMapping("/api/quizzes/{id}")
    public ResponseEntity<Object> getQuizByID(@PathVariable Long id) throws QuizNotFoundException {
        Optional<Quiz> optQuiz = quizService.findByID(id);
        if (optQuiz.isEmpty()) {
            throw new QuizNotFoundException("");
        }

        Quiz quiz = optQuiz.get();

        return QuizHandler.generateResponse(
                quiz.getId(),
                quiz,
                HttpStatus.OK
        );
    }



    @PostMapping("/api/quizzes")
    public ResponseEntity<Object> addQuiz(@Valid @RequestBody Quiz quiz,
                                          @AuthenticationPrincipal UserDetails userDetails) {

        if (quiz.getOptions() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        quiz.setUser(userService.findByEmail(userDetails.getUsername()).get());
        Quiz addedQuiz = quizService.save(quiz);

        return QuizHandler.generateResponse(
                addedQuiz.getId(),
                addedQuiz,
                HttpStatus.OK
        );
    }

    @GetMapping("/api/quiz")
    public Quiz getQuiz() {
        return quizService.getDummyQuiz();
    }

}

