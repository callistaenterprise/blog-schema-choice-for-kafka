package se.martin.weather.kafka.consumer;

import com.google.protobuf.DynamicMessage;
import com.google.protobuf.InvalidProtocolBufferException;
import io.smallrye.reactive.messaging.kafka.api.IncomingKafkaRecordMetadata;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.martin.weather.model.Weather;
import se.martin.weather.avro.WeatherReading;
import se.martin.weather.proto.WeatherReport;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletionStage;

/**
 * Contains a simple consumer to read from Kafka topics and log metadata on the record
 */
@ApplicationScoped
public class WeatherConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(WeatherConsumer.class);
    private static final String UNKNOWN = "unknown";

    @Incoming("in-weather-json")
    public CompletionStage<Void> consumeJson(Message<Weather> message) {
        var metadata = message.getMetadata(IncomingKafkaRecordMetadata.class);
        var weather = message.getPayload();
        var weatherId = Optional.ofNullable(weather).map(Weather::getRecordingId).map(UUID::toString).orElse(UNKNOWN);
        var topic = metadata.map(IncomingKafkaRecordMetadata::getTopic).orElse(UNKNOWN);
        var partition = metadata.map(IncomingKafkaRecordMetadata::getPartition).map(i -> "" + i).orElse(UNKNOWN);
        var offset = metadata.map(IncomingKafkaRecordMetadata::getOffset).map(l -> "" + l).orElse(UNKNOWN);
        LOGGER.info("Reading message from topic {} partition {} and offset {} with id {}", topic, partition, offset, weatherId);
        return message.ack();
    }

    @Incoming("in-weather-avro")
    public CompletionStage<Void> consumeAvro(Message<WeatherReading> message) {
        var metadata = message.getMetadata(IncomingKafkaRecordMetadata.class);
        var weather = message.getPayload();
        var weatherId = Optional.ofNullable(weather).map(WeatherReading::getRecordingId).orElse(UNKNOWN);
        var topic = metadata.map(IncomingKafkaRecordMetadata::getTopic).orElse(UNKNOWN);
        var partition = metadata.map(IncomingKafkaRecordMetadata::getPartition).map(i -> "" + i).orElse(UNKNOWN);
        var offset = metadata.map(IncomingKafkaRecordMetadata::getOffset).map(l -> "" + l).orElse(UNKNOWN);
        LOGGER.info("Reading message from topic {} partition {} and offset {} with id {}", topic, partition, offset, weatherId);
        return message.ack();
    }

    @Incoming("in-weather-proto")
    public CompletionStage<Void> consumeProto(Message<WeatherReport> message) throws InvalidProtocolBufferException {
        var metadata = message.getMetadata(IncomingKafkaRecordMetadata.class);
        var weather = message.getPayload();
        var weatherId = Optional.ofNullable(weather).map(WeatherReport::getRecordingId).orElse(UNKNOWN);
        var topic = metadata.map(IncomingKafkaRecordMetadata::getTopic).orElse(UNKNOWN);
        var partition = metadata.map(IncomingKafkaRecordMetadata::getPartition).map(i -> "" + i).orElse(UNKNOWN);
        var offset = metadata.map(IncomingKafkaRecordMetadata::getOffset).map(l -> "" + l).orElse(UNKNOWN);
        LOGGER.info("Reading message from topic {} partition {} and offset {} with id {}", topic, partition, offset, weatherId);
        return message.ack();
    }
}
