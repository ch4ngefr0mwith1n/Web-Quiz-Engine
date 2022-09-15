package engine.handlers;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Data
public class UserAnswer {

    public UserAnswer() {
        answer = new ArrayList<>();
    }

    @NotNull
    @JsonProperty(value = "answer")
    List<Integer> answer;
}
