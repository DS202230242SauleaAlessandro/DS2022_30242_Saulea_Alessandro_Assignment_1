package ro.tuc.ds2020.dtos.builders;

import lombok.var;
import ro.tuc.ds2020.dtos.DeviceDTO;
import ro.tuc.ds2020.dtos.MeasurementDTO;
import ro.tuc.ds2020.dtos.MeasurementJSON;
import ro.tuc.ds2020.entities.Measurement;
import ro.tuc.ds2020.entities.MeasurementId;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

import static java.lang.Math.ceil;

public class MeasurementBuilder {
    public static MeasurementDTO toDTO(Measurement measurement) {
        return new MeasurementDTO(DeviceBuilder.toDTO(measurement.getDevice()), measurement.getTimestamp(),
                measurement.getConsumption());
    }

    public static Measurement toEntity(MeasurementDTO measurementDTO){
        return new Measurement(MeasurementId.builder().timestamp(measurementDTO.getTimestamp()).build(),
                measurementDTO.getConsumption());
    }

    public static MeasurementDTO toDTO(MeasurementJSON measurementJSON){
        var dateTime = Instant.ofEpochMilli(measurementJSON.getTimestamp()).atZone(ZoneId.of("UTC")).toLocalDateTime();
        return new MeasurementDTO(DeviceDTO.builder().uuid(measurementJSON.getDeviceId()).build(),
                LocalDateTime.of(dateTime.toLocalDate(), LocalTime.of(dateTime.getHour(), 0)),
                (int) ceil(measurementJSON.getConsumption()));
    }
}