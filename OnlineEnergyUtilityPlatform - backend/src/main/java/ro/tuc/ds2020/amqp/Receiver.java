package ro.tuc.ds2020.amqp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.springframework.boot.CommandLineRunner;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import ro.tuc.ds2020.controllers.handlers.exceptions.model.ResourceNotFoundException;
import ro.tuc.ds2020.dtos.MeasurementJSON;
import ro.tuc.ds2020.dtos.builders.MeasurementBuilder;
import ro.tuc.ds2020.services.MeasurementService;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

@Component
@RequiredArgsConstructor
public class Receiver implements CommandLineRunner {
    private final MeasurementService measurementService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Override
    public void run(String... args) {
        final String user = "kdayjlqa";
        final String password = "Jh7phazVIlRJrACUZBYSMlLD23aj9dCA";
        final String host = "goose.rmq2.cloudamqp.com";
        final String QUEUE_NAME = "metering_queue";

        var mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        var factory = new ConnectionFactory();

        try {
            factory.setUri("amqps://" + user + ":" + password + "@" + host + "/" + user);
        } catch (URISyntaxException | NoSuchAlgorithmException | KeyManagementException e) {
            System.out.println(e.getMessage());
        }
        factory.setConnectionTimeout(30000);

        try {
            var channel = factory.newConnection().createChannel();
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            System.out.println("Waiting for messages");

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                var measurementJSON = mapper.readValue(delivery.getBody(), MeasurementJSON.class);
                System.out.println(measurementJSON);
                var measurementFromJSON = MeasurementBuilder.toDTO(measurementJSON);

                try {
                    var measurementDTO = measurementService.findMeasurementNoException(
                            measurementJSON.getDeviceId(), measurementFromJSON.getTimestamp());
                    if (measurementDTO != null){
                        measurementDTO.setConsumption(measurementDTO.getConsumption() + measurementFromJSON.getConsumption());
                        measurementDTO = measurementService.update(measurementDTO);
                    }
                    else{
                        measurementDTO = measurementService.insert(measurementFromJSON);
                    }
                    if (measurementDTO.getConsumption() > measurementDTO.getDevice().getMaxConsumption()){
                        simpMessagingTemplate.convertAndSend("/topic/notify", new ConsumptionNotification(measurementDTO));
                    }
                }
                catch(ResourceNotFoundException e){
                    System.out.println("Device not found");
                }

                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            };
            channel.basicConsume(QUEUE_NAME, false, deliverCallback, consumerTag -> {});

        } catch (IOException | TimeoutException e) {
            System.out.println(e.getMessage());
        }
    }
}