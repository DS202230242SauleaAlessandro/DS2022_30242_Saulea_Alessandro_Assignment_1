package ro.tuc.ds2020.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class MeasurementDTO {
    private DeviceDTO device;
    private LocalDateTime timestamp;
    private int consumption;
}
