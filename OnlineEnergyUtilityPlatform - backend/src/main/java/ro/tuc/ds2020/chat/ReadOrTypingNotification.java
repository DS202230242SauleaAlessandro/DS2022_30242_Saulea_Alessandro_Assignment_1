package ro.tuc.ds2020.chat;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Data
@RequiredArgsConstructor
public class ReadOrTypingNotification {
    private UUID from;
    private UUID to;
}
