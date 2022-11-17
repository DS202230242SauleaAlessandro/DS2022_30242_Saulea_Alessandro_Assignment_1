package ro.tuc.ds2020.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class UserDTO {
    private UUID uuid;
    private String name;
    private String username;
    private String password;
    private boolean isAdmin;
}
