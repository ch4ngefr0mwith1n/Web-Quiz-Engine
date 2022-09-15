package engine.service;

import engine.model.Quiz;
import engine.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
public class QuizService {

    private QuizRepository quizRepository;

    @Autowired
    public QuizService(QuizRepository quizRepository) {
        this.quizRepository = quizRepository;
    }

    public Quiz save(Quiz quiz) {
        return quizRepository.save(quiz);
    }

    public Optional<Quiz> findByID(Long id) {
        return quizRepository.findById(id);
    }

    public void delete(Long id, String username) {
        Optional<Quiz> optQuiz = findByID(id);
        // u slučaju da kviz ne postoji:
        if (optQuiz.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        // u slučaju da korisnik nema ovlašćenja:
        if (!optQuiz.get().getUser().getUsername().equals(username)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        quizRepository.deleteById(id);
    }

    public Page<Quiz> getAllQuizzes(Integer pageNo, Integer pageSize) {
        // kreiraćemo "Pageable" objekat bez ikakvog sortiranja:
        Pageable paging = PageRequest.of(pageNo, pageSize);
        // instancu prosljeđujemo u "findAll" metodu:
        return quizRepository.findAll(paging);
    }

    /*
    // OVO MORA DA SE SREDI:
    public List<Map<Object, Object>> getAll() {
        List<Map<Object, Object>> allQuizes = new ArrayList<>();
        Iterable<Quiz> quizzesIt = quizRepository.findAll();

        quizzesIt.forEach(x -> {
            Map<Object,Object> quizProperties = new HashMap<>();

            quizProperties.put("id", x.getId());
            quizProperties.put("title", x.getTitle());
            quizProperties.put("text", x.getText());
            quizProperties.put("options", x.getOptions());

            allQuizes.add(quizProperties);
        });

        return allQuizes;
    }

     */

    // provjera ispravnosti odgovora nakon slanja odgovora unutar kviza:
    public boolean checkSubmission(Long id, List<Integer> userAnswer) {
        Optional<Quiz> optQuiz = quizRepository.findById(id);
        if (optQuiz.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        Quiz foundQuiz = optQuiz.get();

        return checkAnswers(foundQuiz.getAnswer(), userAnswer);
    }

    // provjera da li su dvije liste istog sadržaja:
    public boolean checkAnswers(List<Integer> answers, List<Integer> answersToCheck) {
        return answers.size() == answersToCheck.size() && answers.containsAll(answersToCheck);
    }

    public Quiz getDummyQuiz() {
        return new Quiz()
                .setTitle("The Java Logo")
                .setText("What is depicted on the Java logo?")
                .setOptions(List.of("Robot","Tea leaf","Cup of coffee","Bug"));
    }
}
