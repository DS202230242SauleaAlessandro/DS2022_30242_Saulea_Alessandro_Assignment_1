package ro.tuc.ds2020.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ro.tuc.ds2020.entities.Device;
import ro.tuc.ds2020.entities.Measurement;
import ro.tuc.ds2020.entities.MeasurementId;

import java.util.Date;
import java.util.List;

public interface MeasurementRepository extends JpaRepository<Measurement, MeasurementId> {

    @Query(value = "select m from Measurement m where m.measurementId.device = :device and cast(m.measurementId.timestamp as date) = :date")
    List<Measurement> findAllByDeviceAndDate(Device device, Date date);

    @Query(value = "select m from Measurement m where m.measurementId.device = :device")
    List<Measurement> findAllByDevice(Device device);
}
