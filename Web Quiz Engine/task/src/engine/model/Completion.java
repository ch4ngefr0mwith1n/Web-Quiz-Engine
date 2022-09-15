package engine.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "completions")
public class Completion {
    @JsonProperty(value = "completion_id",access = JsonProperty.Access.WRITE_ONLY)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @JsonProperty("id")
    @NotNull
    private Long quizID;

    @Column(name = "completed_at")
    @NotNull
    private LocalDateTime completedAt;

    public Completion(User user, Long quizID) {
        this.user = user;
        this.quizID = quizID;
        this.completedAt = LocalDateTime.now();
    }
}
