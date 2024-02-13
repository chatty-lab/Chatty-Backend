package com.chatty.service.interest;

import com.chatty.dto.interest.response.InterestResponse;
import com.chatty.entity.user.Interest;
import com.chatty.repository.interest.InterestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class InterestService {

    private final InterestRepository interestRepository;

    public List<InterestResponse> getInterests() {
        List<Interest> interests = interestRepository.findAll();

        return interests.stream()
                .map(InterestResponse::of)
                .collect(Collectors.toList());
    }
}
