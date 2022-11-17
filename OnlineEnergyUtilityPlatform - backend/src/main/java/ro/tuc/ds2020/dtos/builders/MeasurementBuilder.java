package ro.tuc.ds2020.dtos.builders;

import ro.tuc.ds2020.dtos.MeasurementDTO;
import ro.tuc.ds2020.entities.Measurement;
import ro.tuc.ds2020.entities.MeasurementId;

public class MeasurementBuilder {
    public static MeasurementDTO toDTO(Measurement measurement) {
        return new MeasurementDTO(DeviceBuilder.toDTO(measurement.getDevice()), measurement.getTimestamp(),
                measurement.getConsumption());
    }

    public static Measurement toEntity(MeasurementDTO measurementDTO){
        return new Measurement(MeasurementId.builder().timestamp(measurementDTO.getTimestamp()).build(),
                measurementDTO.getConsumption());
    }
}