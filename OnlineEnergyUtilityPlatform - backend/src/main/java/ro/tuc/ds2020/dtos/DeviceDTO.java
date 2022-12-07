package ro.tuc.ds2020.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
@Builder
public class DeviceDTO {
    private UUID uuid;
    private String description;
    private String address;
    private int maxConsumption;
    private UserDTO userDTO;
}
