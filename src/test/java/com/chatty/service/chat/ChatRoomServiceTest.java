package com.chatty.service.chat;

import com.chatty.constants.Authority;
import com.chatty.dto.chat.response.ChatRoomListResponse;
import com.chatty.entity.chat.ChatMessage;
import com.chatty.entity.chat.ChatRoom;
import com.chatty.entity.user.User;
import com.chatty.repository.chat.ChatRoomRepository;
import com.chatty.repository.chat.MessageRepository;
import com.chatty.repository.user.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class ChatRoomServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoomService chatRoomService;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private MessageRepository chatMessageRepository;

    @AfterEach
    void tearDown() {
        chatMessageRepository.deleteAllInBatch();
        chatRoomRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @DisplayName("채팅방 목록을 조회한다. '채팅방만 생성되었을 때'")
    @Test
    void getRoomList() {
        // given
        User user1 = createUser("박지성", "01011112222", "profile1.jpg", true);
        User user2 = createUser("김연아", "01012345678", "profile2.jpg", true);
        userRepository.saveAll(List.of(user1, user2));

        ChatRoom chatRoom = createChatRoom(user1, user2);
        chatRoomRepository.save(chatRoom);

        // when
        List<ChatRoomListResponse> roomList = chatRoomService.getChatRoomList(user1.getMobileNumber());

        // then
        assertThat(roomList).hasSize(1)
                .extracting("senderId", "senderNickname", "senderImageUrl", "blueCheck")
                .containsExactlyInAnyOrder(
                        tuple(user2.getId(), "김연아", "profile2.jpg", true)
                );
    }

    /** user1 을 기준으로 조회하기 때문에, 채팅방 목록에는 user2 닉네임, 사진이 뜨는게 맞다. */
    @DisplayName("채팅방 목록을 조회한다. '채팅 내역이 존재할 때 마지막 메세지, 시간, 읽지 않은 개수도 같이 조회한다.'")
    @Test
    void getRoomListWithLastMessageAndTimeAndUnreadCount() {
        // given
        User user1 = createUser("박지성", "01011112222", "profile1.jpg", true);
        User user2 = createUser("김연아", "01012345678", "profile2.jpg", true);
        userRepository.saveAll(List.of(user1, user2));

        ChatRoom chatRoom = createChatRoom(user1, user2);
        chatRoomRepository.save(chatRoom);

        final LocalDateTime now = LocalDateTime.now();
        ChatMessage message1 = createChatMessage(chatRoom, "안녕하세요1", user2, user1, now);
        ChatMessage message2 = createChatMessage(chatRoom, "반가워요2", user2, user1, now);
        ChatMessage message3 = createChatMessage(chatRoom, "잘있어요3", user2, user1, now);
        ChatMessage message4 = createChatMessage(chatRoom, "다시만나요4", user2, user1, now);
        chatMessageRepository.saveAll(List.of(message1, message2, message3, message4));

        // when
        List<ChatRoomListResponse> roomList = chatRoomService.getChatRoomList(user1.getMobileNumber());

        // then
        assertThat(roomList).hasSize(1)
                .extracting("senderId", "senderNickname", "senderImageUrl", "blueCheck", "lastMessage", "unreadMessageCount")
                .containsExactlyInAnyOrder(
                        tuple(user2.getId(), "김연아", "profile2.jpg", true, "다시만나요4", 4)
                );
    }

    private User createUser(final String nickname, final String mobileNumber, final String imageUrl, final boolean blueCheck) {
        return User.builder()
                .mobileNumber(mobileNumber)
                .nickname(nickname)
                .deviceId("123456")
                .authority(Authority.USER)
                .imageUrl(imageUrl)
                .blueCheck(blueCheck)
                .build();
    }

    private ChatRoom createChatRoom(final User sender, final User receiver) {
        return ChatRoom.builder()
                .sender(sender)
                .receiver(receiver)
                .build();
    }

    private ChatMessage createChatMessage(final ChatRoom chatRoom, final String content, final User sender, final User receiver, final LocalDateTime sendTime) {
        return ChatMessage.builder()
                .chatRoom(chatRoom)
                .content(content)
                .sender(sender)
                .receiver(receiver)
                .isRead(false)
                .sendTime(sendTime)
                .build();
    }
}