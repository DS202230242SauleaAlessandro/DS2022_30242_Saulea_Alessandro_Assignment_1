package ro.tuc.ds2020.dtos.builders;

import ro.tuc.ds2020.dtos.DeviceDTO;
import ro.tuc.ds2020.entities.Device;

public class DeviceBuilder {
    public static DeviceDTO toDTO(Device device){
        return new DeviceDTO(device.getUuid(), device.getDescription(), device.getAddress(),
                device.getMaxConsumption(), UserBuilder.toDTO(device.getUser()));
    }

    public static Device toEntity(DeviceDTO deviceDTO) {
        return Device.builder().address(deviceDTO.getAddress())
                .description(deviceDTO.getDescription()).maxConsumption(deviceDTO.getMaxConsumption()).build();
    }
}
