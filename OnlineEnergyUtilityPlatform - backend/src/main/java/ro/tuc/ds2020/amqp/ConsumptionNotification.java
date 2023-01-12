package ro.tuc.ds2020.amqp;

import lombok.Data;
import ro.tuc.ds2020.dtos.MeasurementDTO;

import java.util.UUID;

@Data
public class ConsumptionNotification {
    private UUID userId;
    private String message;

    public ConsumptionNotification(MeasurementDTO measurementDTO) {
        this.userId = measurementDTO.getDevice().getUserDTO().getUuid();
        this.message = "Your device " + measurementDTO.getDevice().getDescription() + " has consumed too much energy at "
                + measurementDTO.getTimestamp();
    }
}
