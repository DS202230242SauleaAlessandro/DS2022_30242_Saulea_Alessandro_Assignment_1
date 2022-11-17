package ro.tuc.ds2020.dtos.builders;

import ro.tuc.ds2020.dtos.UserDTO;
import ro.tuc.ds2020.entities.User;

import java.util.HashSet;

public class UserBuilder {
    public static UserDTO toDTO(User user) {
        return new UserDTO(user.getUuid(), user.getName(), user.getUsername(), user.getPassword(), user.isAdmin());
    }

    public static User toEntity(UserDTO userDTO) {
        return User.builder().name(userDTO.getName()).username(userDTO.getUsername())
                .password(userDTO.getPassword()).isAdmin(userDTO.isAdmin())
                .devices(new HashSet<>()).build();
    }
}
