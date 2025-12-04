package com.chat.dto;



import java.time.Instant;
import java.time.LocalDateTime;

public record ClientMessage(
        String user,
        String text,
        Instant timeStamp
) {
}
