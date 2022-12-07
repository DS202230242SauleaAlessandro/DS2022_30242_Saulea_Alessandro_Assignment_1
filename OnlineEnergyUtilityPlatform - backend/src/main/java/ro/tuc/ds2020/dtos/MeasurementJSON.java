package ro.tuc.ds2020.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;


import java.util.UUID;

@RequiredArgsConstructor
@Getter
public class MeasurementJSON{
    private UUID deviceId;
    private long timestamp;
    private float consumption;

    @Override
    public String toString() {
        return "MeasurementJSON{" +
                "deviceId=" + deviceId +
                ", timestamp=" + timestamp +
                ", consumption=" + consumption +
                '}';
    }
}
