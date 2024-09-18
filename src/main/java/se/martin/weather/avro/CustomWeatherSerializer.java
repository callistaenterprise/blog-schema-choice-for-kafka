package se.martin.weather.avro;

import org.apache.kafka.common.serialization.Serializer;

import java.nio.charset.StandardCharsets;

/**
 * An example custom serializer which does not serialize using Avro (for demo purposes)
 */
public class CustomWeatherSerializer implements Serializer<WeatherReading> {

    @Override
    public byte[] serialize(String s, WeatherReading weatherReading) {
        return weatherReading.toString().getBytes(StandardCharsets.UTF_8);
    }
}
