package com.chatty.service.match;

import com.chatty.dto.match.response.MatchResponse;
import com.chatty.entity.match.Match;
import com.chatty.entity.user.Gender;
import com.chatty.entity.user.User;
import com.chatty.exception.CustomException;
import com.chatty.repository.match.MatchRepository;
import com.chatty.repository.user.UserRepository;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.chatty.constants.Code.NOT_EXIST_MATCH;
import static com.chatty.constants.Code.NOT_EXIST_USER;

@RequiredArgsConstructor
@Slf4j
@Component
public class MatchHandler extends TextWebSocketHandler {

    private final List<WebSocketSession> sessions = new ArrayList<>();
    private final Gson gson;
    private final UserRepository userRepository;
    private final MatchService matchService;

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

            if (!myCategory.equals(yourCategory)) {
                log.info("카테고리값이 서로 다릅니다.");
                continue;
            }

            System.out.println("카테고리 값이 일치합니다. 매칭되었습니다.");
//            sessions.remove(session);
            TextMessage textMessage = new TextMessage(payload);
//            String json = gson.toJson("test");
//            TextMessage textMessage = new TextMessage(json);
            session.sendMessage(textMessage);
            connected.sendMessage(textMessage);

            Long sessionMatchId = Long.parseLong(session.getAttributes().get("matchId").toString());
            Long connectedMatchId = Long.parseLong(connected.getAttributes().get("matchId").toString());

            matchService.successMatch(sessionMatchId);
            matchService.successMatch(connectedMatchId);

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
//        super.afterConnectionClosed(session, status);
        log.info("세션 삭제 완료 소켓 연결 끊음.");
        sessions.remove(session);
    }

//    private void registerClientInfo(WebSocketSession session) {
//        String query = session.getUri().getQuery();
//        String[] paramArray = query.split("&");
//
//        for (int i = 0; i < 2; i++) {
//            String param = paramArray[i];
//            paramArray[i] = param.substring(param.indexOf("=") + 1);
//        }
//
//        String mobileNumber = paramArray[0];
//        String scope = paramArray[1];
//
//        System.out.println("mobileNumber = " + mobileNumber);
//        System.out.println("scope = " + scope);
//
//        User user = userRepository.findUserByMobileNumber(mobileNumber)
//                .orElseThrow(() -> new CustomException(NOT_EXIST_USER));
//
//        Map<String, Object> attributes = session.getAttributes();
//        attributes.put("mobileNumber", user.getMobileNumber());
//        attributes.put("nickname", user.getNickname());
//        attributes.put("gender", user.getGender());
//        int age = LocalDate.now().getYear() - user.getBirth().getYear() + 1;
//        attributes.put("age", age);
//        attributes.put("mbti", user.getMbti());
//        attributes.put("location", user.getLocation());
////        attributes.put("imageUrl", user.getImageUrl());
//
//        sessions.add(session);
//
//        log.info("{} 님이 매칭 대기열에 입장했습니다.", user.getNickname());
//        log.info("sessionId = {}", session.getId());
//    }

//    public void connectUser(WebSocketSession session) throws Exception {
//        for (WebSocketSession connected : sessions) {
//            String myGender = session.getAttributes().get("gender").toString();
//            String yourGender = connected.getAttributes().get("gender").toString();
//
//            Point myLocation = (Point) session.getAttributes().get("location");
//            Point yourLocation = (Point) connected.getAttributes().get("location");
//            if (session == connected) {
//                continue;
//            }
//            // connected 에서 뽑았을 때, 여러가지 경우의 수를 다 따져봐야 한다.
//            /*
//            - 세션 정보가 이미 매칭이 되어있는 상태인지 먼저 검증한다.
//            1. 성별이 다른지
//            2. 내가 설정한 거리 범위 안에 존재하는지
//            3. 이미 매칭된 적이 있거나, 차단을 한 유저인지
//            4. 내가 선택한 관심사를 지니고 있는 유저인지
//             */
//            if (connected.getAttributes().containsKey("destination")) {
//                continue;
//            }
//            if (myGender.equals(yourGender)) { // 성별이 같으면 continue
//                continue;
//            }
//            if (userRepository.customFindByDistance2(yourLocation, myLocation, 999.0) == null) {
//                continue;
//            }
//
////            sendUserInfo(session, connected);
////            sendUserInfo(connected, session);
//        }
//    }

//    private void sendUserInfo(WebSocketSession mySession, WebSocketSession duoSession) throws Exception {
//        User user = userRepository.findUserByMobileNumber(mySession.getAttributes().get("mobileNumber").toString())
//                .orElseThrow(() -> new CustomException(NOT_EXIST_USER));
//
//        MatchResponse userResponse = MatchResponse.of(user, mySession.getAttributes().get("age").toString());
//
//        mySession.getAttributes().put("destination", duoSession.getId());
//
//        String json = gson.toJson(userResponse);
//        TextMessage textMessage = new TextMessage(json);
//        duoSession.sendMessage(textMessage);
//    }
}
