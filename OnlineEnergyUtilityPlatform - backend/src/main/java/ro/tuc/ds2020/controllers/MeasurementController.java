package ro.tuc.ds2020.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.tuc.ds2020.dtos.MeasurementDTO;
import ro.tuc.ds2020.services.MeasurementService;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping("/measurements")
@RequiredArgsConstructor
public class MeasurementController {
    private final MeasurementService measurementService;

    @GetMapping("bydevice&date")
    public ResponseEntity<?> findAllByDeviceAndDate(@RequestParam UUID deviceId,
                                                    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date) {
        return ResponseEntity.ok(measurementService.findAllByDeviceIdAndDate(deviceId, date));
    }

    @GetMapping("bydevice&time")
    public ResponseEntity<?> findByDeviceIdAndTimestamp(@RequestParam UUID deviceId,
                                                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                        LocalDateTime timestamp){
        return ResponseEntity.ok(measurementService.findByDeviceIdAndTimestamp(deviceId, timestamp));
    }

    @GetMapping("bydevice/{deviceId}")
    public ResponseEntity<?> findAllByDeviceId(@PathVariable UUID deviceId){
        return ResponseEntity.ok(measurementService.findAllByDeviceId(deviceId));
    }

    @PostMapping
    public ResponseEntity<?> insert(@RequestBody MeasurementDTO measurementDTO){
        return new ResponseEntity<>(measurementService.insert(measurementDTO), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody MeasurementDTO measurementDTO){
        return ResponseEntity.ok(measurementService.update(measurementDTO));
    }

    @DeleteMapping
    public ResponseEntity<?> delete(@RequestParam UUID deviceId,
                                    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                    LocalDateTime timestamp){
        measurementService.delete(deviceId, timestamp);
        return ResponseEntity.ok("Measurement deleted");
    }
}