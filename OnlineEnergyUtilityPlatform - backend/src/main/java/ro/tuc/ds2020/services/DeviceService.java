package ro.tuc.ds2020.services;

import lombok.RequiredArgsConstructor;
import lombok.var;
import org.springframework.stereotype.Service;
import ro.tuc.ds2020.controllers.handlers.exceptions.model.ResourceNotFoundException;
import ro.tuc.ds2020.dtos.DeviceDTO;
import ro.tuc.ds2020.dtos.builders.DeviceBuilder;
import ro.tuc.ds2020.entities.Device;
import ro.tuc.ds2020.entities.User;
import ro.tuc.ds2020.repositories.DeviceRepository;
import ro.tuc.ds2020.repositories.UserRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeviceService {
    private final DeviceRepository deviceRepository;
    private final UserRepository userRepository;

    public List<DeviceDTO> findAll(){
        return deviceRepository.findAll().stream().map(DeviceBuilder::toDTO).collect(Collectors.toList());
    }

    public Device findDeviceById(UUID uuid){
        return deviceRepository.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("Device not found"));
    }

    public DeviceDTO findById(UUID uuid){
        return DeviceBuilder.toDTO(findDeviceById(uuid));
    }

    private User findUserById(UUID uuid){
        return userRepository.findById(uuid).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public DeviceDTO insert(DeviceDTO deviceDTO){
        System.out.println(deviceDTO.getUserDTO().getUuid());
        var user = findUserById(deviceDTO.getUserDTO().getUuid());
        var device = DeviceBuilder.toEntity(deviceDTO);
        device.setUser(user);
        return DeviceBuilder.toDTO(deviceRepository.save(device));
    }

    public DeviceDTO update(DeviceDTO deviceDTO){
        var device = findDeviceById(deviceDTO.getUuid());
        var user = findUserById(deviceDTO.getUserDTO().getUuid());
        device.setAddress(deviceDTO.getAddress());
        device.setDescription(deviceDTO.getDescription());
        device.setMaxConsumption(deviceDTO.getMaxConsumption());
        device.setUser(user);
        return DeviceBuilder.toDTO(deviceRepository.save(device));
    }

    public void delete(UUID uuid){
        deviceRepository.delete(findDeviceById(uuid));
    }
}