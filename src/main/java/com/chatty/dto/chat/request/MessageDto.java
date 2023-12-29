package com.chatty.dto.chat.request;

import com.chatty.constants.MessageType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageDto {

    private MessageType type;

    @NotNull
    private Long roomId;

    @NotNull
    private Long senderId;

    @NotBlank
    private String content;

    public void setContent(String content){
        this.content = content;
    }
}
