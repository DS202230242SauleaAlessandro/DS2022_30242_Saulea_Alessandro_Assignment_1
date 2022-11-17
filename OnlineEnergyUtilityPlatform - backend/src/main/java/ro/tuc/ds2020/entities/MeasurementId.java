package ro.tuc.ds2020.entities;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

//src: https://www.baeldung.com/jpa-composite-primary-keys
@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MeasurementId implements Serializable {
    @ManyToOne
    @JoinColumn(name = "device_id")
    private Device device;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;
}
