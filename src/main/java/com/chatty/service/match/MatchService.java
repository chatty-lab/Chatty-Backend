package com.chatty.service.match;

import com.chatty.constants.Code;
import com.chatty.dto.match.request.MatchRequest;
import com.chatty.dto.match.response.MatchResponse;
import com.chatty.dto.user.response.UserResponse;
import com.chatty.entity.match.Match;
import com.chatty.entity.user.Gender;
import com.chatty.entity.user.User;
import com.chatty.exception.CustomException;
import com.chatty.repository.match.MatchRepository;
import com.chatty.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.WebSocketSession;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MatchService {

    private final UserRepository userRepository;
    private final MatchRepository matchRepository;

    @Transactional
    public MatchResponse createMatch(final String mobileNumber, final MatchRequest request) {
        User user = userRepository.findUserByMobileNumber(mobileNumber)
                .orElseThrow(() -> new CustomException(Code.NOT_EXIST_USER));

        LocalDateTime now = LocalDateTime.now();

        validateDailyMatchingLimit(user, now);

        int age = calculateUserAge(user);
        Match match = matchRepository.save(request.toEntity(user, now));

        return MatchResponse.of(match, age);
    }

    @Transactional
    public MatchResponse successMatch(final Long matchId) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new CustomException(Code.NOT_EXIST_MATCH));

        int age = calculateUserAge(match.getUser());
        match.updateIsSuccess();

        return MatchResponse.of(match, age);
    }

    public void createUserSession(final WebSocketSession session, final MatchResponse matchResponse) {
        Map<String, Object> attributes = session.getAttributes();
        attributes.put("matchId", matchResponse.getId());
        attributes.put("userId", matchResponse.getUserId());
        attributes.put("nickname", matchResponse.getNickname());
        attributes.put("gender", matchResponse.getGender());
        attributes.put("requestGender", matchResponse.getRequestGender());
        attributes.put("age", matchResponse.getAge());
        attributes.put("requestMinAge", matchResponse.getRequestMinAge());
        attributes.put("requestMaxAge", matchResponse.getRequestMaxAge());
        attributes.put("category", matchResponse.getRequestCategory());
    }

    private int calculateUserAge(final User user) {
        return LocalDate.now().getYear() - user.getBirth().getYear() + 1;
    }

    private void validateDailyMatchingLimit(final User user, final LocalDateTime now) {
        Long matchesCount = matchRepository.countMatchesBy(
                user.getId(),
                now.toLocalDate().atStartOfDay(),
                now.toLocalDate().plusDays(1).atStartOfDay(),
                true
        );

        if (user.getGender().equals(Gender.MALE)) {
            if (matchesCount >= 5) {
                throw new CustomException(Code.MATCH_LIMIT_EXCEEDED);
            }
        } else if (user.getGender().equals(Gender.FEMALE)) {
            if (matchesCount >= 10) {
                throw new CustomException(Code.MATCH_LIMIT_EXCEEDED);
            }
        }
    }
}
