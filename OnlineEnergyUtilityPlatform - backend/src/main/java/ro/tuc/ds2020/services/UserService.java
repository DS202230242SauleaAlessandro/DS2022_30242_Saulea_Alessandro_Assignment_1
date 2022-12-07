package ro.tuc.ds2020.services;

import lombok.RequiredArgsConstructor;
import lombok.var;
import org.springframework.stereotype.Service;
import ro.tuc.ds2020.controllers.handlers.exceptions.model.DuplicateResourceException;
import ro.tuc.ds2020.controllers.handlers.exceptions.model.ResourceNotFoundException;
import ro.tuc.ds2020.dtos.DeviceDTO;
import ro.tuc.ds2020.dtos.LoginDTO;
import ro.tuc.ds2020.dtos.UserDTO;
import ro.tuc.ds2020.dtos.builders.DeviceBuilder;
import ro.tuc.ds2020.dtos.builders.UserBuilder;
import ro.tuc.ds2020.entities.User;
import ro.tuc.ds2020.repositories.UserRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public List<UserDTO> findAll() {
        return userRepository.findAll().stream().map(UserBuilder::toDTO).collect(Collectors.toList());
    }

    private User findUserById(UUID uuid){
        return userRepository.findById(uuid).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public UserDTO findById(UUID uuid) {
        return UserBuilder.toDTO(findUserById(uuid));
    }

    public UserDTO insert(UserDTO userDTO){
        if (userRepository.findByUsername(userDTO.getUsername()).isPresent()){
            throw new DuplicateResourceException("Username already used!");
        }
        return UserBuilder.toDTO(userRepository.save(UserBuilder.toEntity(userDTO)));
    }

    public UserDTO update(UserDTO userDTO){
        var user = findUserById(userDTO.getUuid());
        var sameUsernameUser = userRepository.findByUsername(userDTO.getUsername()).orElse(null);
        if (sameUsernameUser != null && !sameUsernameUser.getUuid().equals(user.getUuid())){
            throw new DuplicateResourceException("Username already used!");
        }
        user.setName(userDTO.getName());
        user.setPassword(userDTO.getPassword());
        user.setUsername(userDTO.getUsername());
        user.setAdmin(userDTO.isAdmin());
        return UserBuilder.toDTO(userRepository.save(user));
    }

    public void delete(UUID uuid){
        userRepository.delete(findUserById(uuid));
    }

    public UserDTO login(LoginDTO loginDTO){
        return UserBuilder.toDTO(
                userRepository.findByUsernameAndPassword(loginDTO.getUsername(), loginDTO.getPassword())
                        .orElseThrow(() -> new ResourceNotFoundException("invalid credentials")));
    }

    public List<DeviceDTO> findDevicesByUser(UUID userId){
        return findUserById(userId).getDevices().stream().map(DeviceBuilder::toDTO).collect(Collectors.toList());
    }
}
