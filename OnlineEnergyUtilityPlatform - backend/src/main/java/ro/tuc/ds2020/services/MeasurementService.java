package ro.tuc.ds2020.services;

import lombok.RequiredArgsConstructor;
import lombok.var;
import org.springframework.stereotype.Service;
import ro.tuc.ds2020.controllers.handlers.exceptions.model.DuplicateResourceException;
import ro.tuc.ds2020.controllers.handlers.exceptions.model.ResourceNotFoundException;
import ro.tuc.ds2020.dtos.MeasurementDTO;
import ro.tuc.ds2020.dtos.builders.MeasurementBuilder;
import ro.tuc.ds2020.entities.Device;
import ro.tuc.ds2020.entities.Measurement;
import ro.tuc.ds2020.entities.MeasurementId;
import ro.tuc.ds2020.repositories.DeviceRepository;
import ro.tuc.ds2020.repositories.MeasurementRepository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MeasurementService {
    private final MeasurementRepository measurementRepository;
    private final DeviceRepository deviceRepository;

    private Device findDeviceById(UUID uuid){
        return deviceRepository.findById(uuid).orElseThrow(() -> new ResourceNotFoundException("Device not found"));
    }

    public List<MeasurementDTO> findAllByDeviceIdAndDate(UUID deviceId, Date date) {
        return measurementRepository.findAllByDeviceAndDate(findDeviceById(deviceId), date).stream()
                .map(MeasurementBuilder::toDTO).collect(Collectors.toList());
    }

    private Measurement findMeasurementByDeviceIdAndTimestamp(UUID deviceId, LocalDateTime timestamp){
        return measurementRepository.findById(new MeasurementId(findDeviceById(deviceId), timestamp))
                .orElseThrow(() -> new ResourceNotFoundException("Measurement not found"));
    }

    public MeasurementDTO findMeasurementNoException(UUID deviceId, LocalDateTime timestamp){
        var measurement = measurementRepository.findById(new MeasurementId(findDeviceById(deviceId), timestamp)).orElse(null);
        return measurement != null ? MeasurementBuilder.toDTO(measurement) : null;
    }

    public MeasurementDTO findByDeviceIdAndTimestamp(UUID deviceId, LocalDateTime timestamp){
        return MeasurementBuilder.toDTO(findMeasurementByDeviceIdAndTimestamp(deviceId, timestamp));
    }

    public List<MeasurementDTO> findAllByDeviceId(UUID deviceId){
        return measurementRepository.findAllByDevice(findDeviceById(deviceId)).stream()
                .map(MeasurementBuilder::toDTO).collect(Collectors.toList());
    }

    public MeasurementDTO insert(MeasurementDTO measurementDTO){
        var device = findDeviceById(measurementDTO.getDevice().getUuid());
        if (measurementRepository.findById(new MeasurementId(device, measurementDTO.getTimestamp())).isPresent()){
            throw new DuplicateResourceException("There is already this timestamp on this device!");
        }
        if (measurementDTO.getTimestamp().isAfter(LocalDateTime.now())){
            throw new RuntimeException("Cannot add a future timestamp");
        }
        var measurement = MeasurementBuilder.toEntity(measurementDTO);
        measurement.setDevice(device);
        return MeasurementBuilder.toDTO(measurementRepository.save(measurement));
    }

    public MeasurementDTO update(MeasurementDTO measurementDTO){
        var measurement = findMeasurementByDeviceIdAndTimestamp(measurementDTO.getDevice().getUuid(), measurementDTO.getTimestamp());
        measurement.setConsumption(measurementDTO.getConsumption());
        return MeasurementBuilder.toDTO(measurementRepository.save(measurement));
    }

    public void delete(UUID deviceId, LocalDateTime timestamp){
        measurementRepository.delete(findMeasurementByDeviceIdAndTimestamp(deviceId, timestamp));
    }
}