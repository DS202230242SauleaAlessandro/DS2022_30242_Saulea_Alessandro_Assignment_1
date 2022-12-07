package ro.tuc.ds2020.websocket;

import lombok.Data;
import ro.tuc.ds2020.entities.Measurement;

import java.util.UUID;

@Data
public class Notification {
    private UUID userId;
    private String message;

    public Notification(Measurement measurement) {
        this.userId = measurement.getDevice().getUser().getUuid();
        this.message = "Your device " + measurement.getDevice().getDescription() + " has consumed too much energy at "
                + measurement.getTimestamp();
    }
}
