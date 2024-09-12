package se.martin.weather.kafka.producer;

import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Message;
import se.martin.weather.avro.WeatherReading;
import se.martin.weather.avro.WeatherReadingMapper;
import se.martin.weather.model.Weather;
import se.martin.weather.proto.WeatherReport;
import se.martin.weather.proto.WeatherReportMapper;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

/**
 * Class to publish weather readings using Json, Avro and Protobuf
 */
@ApplicationScoped
public class WeatherScheduler {

    private static final List<String> stations = Arrays.asList(
            "Stockholm", "Copenhagen", "Berlin", "Madrid", "London", "Edinburgh", "Washington", "New York"
    );

    private Instant stopTime;

    @Inject
    @Channel("out-weather-json")
    Emitter<Weather> jsonEmitter;

    @Inject
    @Channel("out-weather-avro")
    Emitter<WeatherReading> avroEmitter;

    @Inject
    @Channel("out-weather-proto")
    Emitter<WeatherReport> protoEmitter;

    @Inject
    WeatherService weatherService;

    @Scheduled(every = "1s")
    void generate() {
        // Stop the process after 10 minutes
        if (stopTime == null) {
            stopTime = Instant.now().plusSeconds(600);
        }
        if (Instant.now().isBefore(stopTime)) {
            List<Weather> weathers = stations.stream()
                    .map(weatherService::fetch).toList();
            publishJson(weathers);
            publishAvro(weathers);
            publishProto(weathers);
        }
    }

    private void publishJson(List<Weather> weathers) {
        weathers.stream()
                .parallel()
                .map(Message::of)
                .forEach(jsonEmitter::send);
    }

    private void publishAvro(List<Weather> weathers) {
        weathers.stream()
                .parallel()
                .map(WeatherReadingMapper::from)
                .map(Message::of)
                .forEach(avroEmitter::send);
    }

    private void publishProto(List<Weather> weathers) {
        weathers.stream()
                .parallel()
                .map(WeatherReportMapper::from)
                .map(Message::of)
                .forEach(protoEmitter::send);
    }
}