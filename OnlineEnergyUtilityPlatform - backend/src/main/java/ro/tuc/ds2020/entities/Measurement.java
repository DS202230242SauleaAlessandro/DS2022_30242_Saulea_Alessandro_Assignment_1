package ro.tuc.ds2020.entities;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "measurement", schema = "public")
public class Measurement{
    @EmbeddedId
    private MeasurementId measurementId;

    @Column(name = "consumption")
    @Min(value = 0)
    private int consumption;

    public Device getDevice(){
        return measurementId.getDevice();
    }

    public void setDevice(Device device){
        measurementId.setDevice(device);
    }

    public LocalDateTime getTimestamp(){
        return measurementId.getTimestamp();
    }
}