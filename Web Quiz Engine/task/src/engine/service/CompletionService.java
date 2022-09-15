package engine.service;

import engine.model.Completion;
import engine.model.User;
import engine.repository.CompletionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class CompletionService {
    private CompletionRepository completionRepository;

    @Autowired
    public CompletionService(CompletionRepository completionRepository) {
        this.completionRepository = completionRepository;
    }

    public Completion save(Completion completion) {
        return this.completionRepository.save(completion);
    }

    public Page<Completion> getCompletions(User user, Integer pageNo, Integer pageSize) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by("completedAt").descending());
        return completionRepository.getCompletions(user, paging);
    }


}
