package engine.handlers;

import engine.model.Quiz;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.validation.Valid;
import java.util.*;

public class QuizHandler {

    public static ResponseEntity<Object> generateResponse(Long id, @Valid Quiz quiz, HttpStatus status) {
        Map<Object,Object> map = new LinkedHashMap<>();
        map.put("id", id);
        map.put("title", quiz.getTitle());
        map.put("text", quiz.getText());
        map.put("options", quiz.getOptions());

        return new ResponseEntity<Object>(map, status);
    }
}
