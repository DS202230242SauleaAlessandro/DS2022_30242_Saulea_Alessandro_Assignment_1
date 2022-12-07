package ro.tuc.ds2020.websocket;

import lombok.Data;
import ro.tuc.ds2020.dtos.MeasurementDTO;

import java.util.UUID;

@Data
public class Notification {
    private UUID userId;
    private String message;

    public Notification(MeasurementDTO measurementDTO) {
        this.userId = measurementDTO.getDevice().getUserDTO().getUuid();
        this.message = "Your device " + measurementDTO.getDevice().getDescription() + " has consumed too much energy at "
                + measurementDTO.getTimestamp();
    }
}
