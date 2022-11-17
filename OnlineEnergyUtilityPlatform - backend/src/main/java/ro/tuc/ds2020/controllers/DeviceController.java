package ro.tuc.ds2020.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.tuc.ds2020.dtos.DeviceDTO;
import ro.tuc.ds2020.services.DeviceService;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping("/devices")
@RequiredArgsConstructor
public class DeviceController {
    private final DeviceService deviceService;

    @GetMapping
    public ResponseEntity<?> findAll(){
        return ResponseEntity.ok(deviceService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable UUID id){
        return ResponseEntity.ok(deviceService.findById(id));
    }

    @PostMapping
    public ResponseEntity<?> insert(@RequestBody @Valid DeviceDTO deviceDTO){
        return new ResponseEntity<>(deviceService.insert(deviceDTO), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody @Valid DeviceDTO deviceDTO){
        return ResponseEntity.ok(deviceService.update(deviceDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id){
        deviceService.delete(id);
        return ResponseEntity.ok("Device deleted");
    }
}