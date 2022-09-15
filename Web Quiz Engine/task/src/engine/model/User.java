package engine.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "users")
public class User implements UserDetails {

    // regex za email:
    private static final String emailRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    @Email(regexp = emailRegex)
    private String email;

    @NotBlank
    @Size(min = 5)
    private String password;

    @OneToMany(mappedBy = "user") // pokazuje na "User" polje unutar "Quiz" entiteta
    private List<Quiz> quizzes = new ArrayList<>();

    @OneToMany(mappedBy = "user") // pokazuje na "User" polje unutar "Completion" entiteta
    private List<Completion> completions = new ArrayList<>();

    @JsonIgnore
    private String role; // imamo samo jednu ulogu - "USER"

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role));
    }

    // "getUsername" će da vraća email:
    @Override
    public String getUsername() {
        return email;
    }

    // ---------------------> ostale metode vraćaju "true" <---------------------
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
