package com.chatty.service.match;

import com.chatty.dto.chat.request.RoomDto;
import com.chatty.dto.chat.response.RoomResponseDto;
import com.chatty.dto.match.response.MatchResponse;
import com.chatty.entity.match.MatchHistory;
import com.chatty.entity.user.Gender;
import com.chatty.entity.user.User;
import com.chatty.exception.CustomException;
import com.chatty.repository.match.MatchHistoryRepository;
import com.chatty.repository.user.UserRepository;
import com.chatty.service.chat.RoomService;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.List;

import static com.chatty.constants.Code.NOT_EXIST_USER;

@RequiredArgsConstructor
@Slf4j
@Component
public class MatchHandler extends TextWebSocketHandler {

    private final List<WebSocketSession> sessions = new ArrayList<>();
    private final Gson gson;
    private final UserRepository userRepository;
    private final MatchService matchService;
    private final RoomService roomService;
    private final MatchHistoryRepository matchHistoryRepository;

    @Override
    public void afterConnectionEstablished(final WebSocketSession session) throws Exception {
        sessions.add(session);
        log.info("sessionId = {}, sessions size = {}", session.getId(), sessions.size());
    }

    @Override
    protected void handleTextMessage(final WebSocketSession session, final TextMessage message) throws Exception {
        String payload = message.getPayload();
        MatchResponse matchResponse = gson.fromJson(payload, MatchResponse.class);

        log.info("sessionId = {}", session.getId());
        log.info("payload = {}", payload);
        log.info("message = {}", message);

        System.out.println("======================");
        System.out.println("matchResponse.getId() = " + matchResponse.getId());
        System.out.println("matchResponse.getGender() = " + matchResponse.getGender());
        System.out.println("matchResponse.getGender() = " + matchResponse.getGender());
        System.out.println("matchResponse.getGender() = " + matchResponse.getGender());
        System.out.println("matchResponse.getGender() = " + matchResponse.getGender());
        System.out.println("matchResponse.getRequestGender() = " + matchResponse.getRequestGender());
        System.out.println("matchResponse.getRequestCategory() = " + matchResponse.getRequestCategory());
        System.out.println("matchResponse.isBlueCheck() = " + matchResponse.isBlueCheck());
        System.out.println("matchResponse.isRequestBlueCheck() = " + matchResponse.isRequestBlueCheck());
        System.out.println("======================");

        matchService.createUserSession(session, matchResponse);

        System.out.println("===========================");
        System.out.println("===========================");

        Object myRequestGender = session.getAttributes().get("requestGender");
        Object myGender = session.getAttributes().get("gender");

        for (WebSocketSession connected : sessions) {
            Object yourGender = connected.getAttributes().get("gender");
            Object yourRequestGender = connected.getAttributes().get("requestGender");
            log.info("session에 저장되어있는 requestGender 값 = {}", session.getAttributes().get("requestGender"));
            if (session == connected || connected.getAttributes().get("nickname") == null) {
                continue;
            }

            // session requestBlueCheck 값 체크
            Object requestBlueCheck = session.getAttributes().get("requestBlueCheck");
            Object connectedBlueCheck = connected.getAttributes().get("isBlueCheck");
            if (requestBlueCheck.equals(true)) {
                if (connectedBlueCheck.equals(false)) {
                    System.out.println("나는 프사있는 사람이랑 매칭이 되고 싶은데,");
                    System.out.println("웹세션에는 프사없는 사람이 존재하니까 continue를 할게.");
                    continue;
                }
            }

            Long senderId = Long.parseLong(session.getAttributes().get("userId").toString());
            Long receiverId = Long.parseLong(connected.getAttributes().get("userId").toString());

            // 이미 매칭 기록이 존재하면 Skip
//            if (matchHistoryRepository.existsBySenderIdAndReceiverId(senderId, receiverId) ||
//                    matchHistoryRepository.existsBySenderIdAndReceiverId(receiverId, senderId)) {
//                continue;
//            }
            //

            // 1. 내가 원하는 성별이 ALL일 경우
            if (myRequestGender.equals(Gender.ALL)) {
                // 상대방이 원하는 성별이 ALL이 아니거나, 상대방이 원하는 성별이 내 성별과 다르면 매칭이 될 수 없다.
                if (!yourRequestGender.equals(Gender.ALL) && !yourRequestGender.equals(myGender)) {
                    continue;
                }
            }

            // 2. 내가 원하는 성별이 MALE일 경우
            if (myRequestGender.equals(Gender.MALE)) {
                // 상대방의 성별이 MALE이 아닐 경우 매칭이 될 수 없다.
                if (!yourGender.equals(Gender.MALE)) {
                    continue;
                }
                // 상대방이 원하는 성별이 ALL이 아니고 (And)
                // 상대방이 원하는 성별이 내 성별과 일치하지 않을 경우 매칭이 될 수 없다.
                if (!yourRequestGender.equals(Gender.ALL) && !yourRequestGender.equals(myGender)) {
                    continue;
                }
            }

            // 3. 내가 원하는 성별이 FEMALE일 경우
            if (myRequestGender.equals(Gender.FEMALE)) {
                // 상대방의 성별이 FEMALE이 아닐 경우 매칭이 될 수 없다.
                if (!yourGender.equals(Gender.FEMALE)) {
                    continue;
                }

                // 상대방이 원하는 성별이 ALL이 아니고 (And)
                // 상대방이 원하는 성별이 내 성별과 일치하지 않을 경우 매칭이 될 수 없다.
                if (!yourRequestGender.equals(Gender.ALL) && !yourRequestGender.equals(myGender)) {
                    continue;
                }
            }

            log.info("성별 조건에 만족하는 사람과 매칭되었습니다.");
            System.out.println("성별 조건에 만족하는 사람이랑 매칭되었습니다.");


            log.info("============나이 조건을 비교합니다.============");
            // 1. 내가 원하는 최소 나이와 최대 나이, 그리고 상대방의 나이를 비교한다.
            int myRequestMinAge = Integer.parseInt(session.getAttributes().get("requestMinAge").toString());
            int myRequestMaxAge = Integer.parseInt(session.getAttributes().get("requestMaxAge").toString());
            int myAge = Integer.parseInt(session.getAttributes().get("age").toString());

            int yourRequestMinAge = Integer.parseInt(connected.getAttributes().get("requestMinAge").toString());
            int yourRequestMaxAge = Integer.parseInt(connected.getAttributes().get("requestMaxAge").toString());
            int yourAge = Integer.parseInt(connected.getAttributes().get("age").toString());

            if (!(myRequestMinAge <= yourAge && yourAge <= myRequestMaxAge)) { // 내가 원하는 나이대에 상대방 나이가 아니라면
                log.info("내가 정한 최소 나이 = {}", myRequestMinAge);
                log.info("상대방의 나이 = {}", yourAge);
                log.info("내가 정한 최대 나이 = {}", myRequestMaxAge);
                log.info("내가 정한 최소 나이와 최대 나이 사이에 상대방의 나이가 들어가지 않습니다.");
                continue;
            }

            if (!(yourRequestMinAge <= myAge && myAge <= yourRequestMaxAge)) {
                log.info("상대방이 정한 최소 나이와 최대 나이 사이에 내 나이가 들어가지 않습니다.");
                continue;
            }

            log.info("내가 정한 최소 나이 = {}", myRequestMinAge);
            log.info("상대방의 나이 = {}", yourAge);
            log.info("내가 정한 최대 나이 = {}", myRequestMaxAge);
            System.out.println("나이 조건에 만족하는 사람과 매칭되었습니다.");

            log.info("카테고리 일치 여부를 확인합니다.");
            String myCategory = session.getAttributes().get("category").toString();
            String yourCategory = connected.getAttributes().get("category").toString();

//            if (!myCategory.equals(yourCategory)) {
//                log.info("카테고리값이 서로 다릅니다.");
//                continue;
//            }

            System.out.println("카테고리 값이 일치합니다. 매칭되었습니다.");

            Long sessionMatchId = Long.parseLong(session.getAttributes().get("matchId").toString());
            Long connectedMatchId = Long.parseLong(connected.getAttributes().get("matchId").toString());

            Long sessionUserId = Long.parseLong(session.getAttributes().get("userId").toString());
            Long connectedUserId = Long.parseLong(connected.getAttributes().get("userId").toString());

            // 채팅방 생성
            RoomDto request = RoomDto.builder()
                    .senderId(sessionUserId)
                    .receiverId(connectedUserId)
                    .build();
            RoomResponseDto room = roomService.createRoom(request);
            String json = gson.toJson(room);
            TextMessage textMessage2 = new TextMessage(json);
            session.sendMessage(textMessage2);
            connected.sendMessage(textMessage2);
            //

            // 매치 성공하면 true
            matchService.successMatch(sessionMatchId);
            matchService.successMatch(connectedMatchId);
            //


            // 매치 성공하면 history에 저장
            User sender = userRepository.findById(sessionUserId)
                    .orElseThrow(() -> new CustomException(NOT_EXIST_USER));
            User receiver = userRepository.findById(connectedUserId)
                    .orElseThrow(() -> new CustomException(NOT_EXIST_USER));

            MatchHistory matchHistory = MatchHistory.builder()
                    .sender(sender)
                    .receiver(receiver)
                    .build();
            matchHistoryRepository.save(matchHistory);
            //

            sessions.remove(session);
            sessions.remove(connected);

            session.close();
            connected.close();
            break;
        }

    }

    private boolean isInvalidGenderMatch(final WebSocketSession session, final WebSocketSession connected) {
        Object connectedUserRequestGender = connected.getAttributes().get("requestGender");
        Object sessionUserGender = session.getAttributes().get("gender");

        return !connectedUserRequestGender.equals(Gender.ALL) && !connectedUserRequestGender.equals(sessionUserGender);
    }

    @Override
    public void afterConnectionClosed(final WebSocketSession session, final CloseStatus status) throws Exception {
        log.info("세션 삭제 완료 소켓 연결 끊음.");
        sessions.remove(session);
    }

}
