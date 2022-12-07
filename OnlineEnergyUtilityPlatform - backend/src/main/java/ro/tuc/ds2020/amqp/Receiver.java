package ro.tuc.ds2020.amqp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.springframework.boot.CommandLineRunner;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import ro.tuc.ds2020.dtos.MeasurementJSON;
import ro.tuc.ds2020.dtos.builders.MeasurementBuilder;
import ro.tuc.ds2020.entities.Measurement;
import ro.tuc.ds2020.services.MeasurementService;
import ro.tuc.ds2020.websocket.Notification;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

// https://www.baeldung.com/jackson-json-view-annotation
// https://www.baeldung.com/jackson-deserialization
// https://www.postgresqltutorial.com/postgresql-jdbc/connecting-to-postgresql-database/
// https://docs.oracle.com/javase/tutorial/jdbc/basics/processingsqlstatements.html
// https://docs.oracle.com/javase/tutorial/jdbc/basics/prepared.html
// https://www.baeldung.com/java-byte-array-to-uuid
// https://spring.io/guides/gs/messaging-stomp-websocket/
// https://stackoverflow.com/questions/54275069/module-not-found-error-cant-resolve-net-in-node-modules-stompjs-lib

@Component
@RequiredArgsConstructor
public class Receiver implements CommandLineRunner {
    private final MeasurementService measurementService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Override
    public void run(String... args) {
        var factory = new ConnectionFactory();
        factory.setHost("localhost");

        var mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        try {
            String QUEUE_NAME = "metering_queue";
            var channel = factory.newConnection().createChannel();
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            System.out.println("Waiting for messages");

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                var measurementJSON = mapper.readValue(delivery.getBody(), MeasurementJSON.class);
                System.out.println(measurementJSON);

                var measurementDTO = MeasurementBuilder.toDTO(measurementJSON);
                var measurement = measurementService.findMeasurementNoException(
                        measurementDTO.getDevice().getUuid(), measurementDTO.getTimestamp());
                if (measurement != null){
                    measurementDTO.setConsumption(measurementDTO.getConsumption() + measurement.getConsumption());
                    measurementService.update(measurementDTO);
                }
                else{
                    measurementService.insert(measurementDTO);
                }

                measurement = measurementService.findMeasurementNoException(
                        measurementDTO.getDevice().getUuid(), measurementDTO.getTimestamp());

                if (measurement.getConsumption() > measurement.getDevice().getMaxConsumption()){
                    simpMessagingTemplate.convertAndSend("/topic/notify", new Notification(measurement));
                }

                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            };
            channel.basicConsume(QUEUE_NAME, false, deliverCallback, consumerTag -> {});

        } catch (IOException | TimeoutException e) {
            System.out.println(e.getMessage());
        }
    }
}