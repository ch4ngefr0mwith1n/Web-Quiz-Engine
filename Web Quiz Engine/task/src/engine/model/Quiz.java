package engine.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.*;


@Data
@NoArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name = "quizzes")
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    //@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long id;

    @NotBlank(message = "Title must not be empty")
    private String title;
    @NotBlank(message = "Text must not be empty")
    private String text;

    @NonNull
    @Size(min = 2, message = "Number of options should be equals or more than 2")
    // javlja se error ako ne stavim ovo:
    @ElementCollection(targetClass=String.class)
    private List<String> options;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull
    // javlja se error ako ne stavim ovo:
    @ElementCollection(targetClass=Integer.class)
    private List<Integer> answer = new ArrayList<>();

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id") // naziv spoljnog kljuca kog Hibernate generise unutar "Quiz" entiteta
    private User user;
}
