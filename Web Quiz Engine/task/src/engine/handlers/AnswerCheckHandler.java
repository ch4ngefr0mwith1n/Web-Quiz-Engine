package engine.handlers;

import org.springframework.http.*;
import java.util.*;

public class AnswerCheckHandler {

    public static ResponseEntity<Object> generateResponse(Boolean message, HttpStatus status, Object responseObj) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("success", message);
        map.put("feedback", responseObj);

        return new ResponseEntity<Object>(map, status);
    }

}
