package se.martin.weather.proto;

import org.junit.jupiter.api.Test;
import se.martin.weather.model.Weather;

import java.time.LocalDateTime;
import java.util.UUID;

public class WeatherReportMapperTest {

    @Test
    public void testMapping() {
        // Create a location
        Weather.Location location = new Weather.Location();
        location.setName("Frankfurt");
        location.setStationId("XXKFFKFKFF");
        location.setLatitude(10.00);
        location.setLongitude(-21.00);

        // Create a weather object
        var weather = new Weather();
        weather.setRecordingId(UUID.randomUUID());
        weather.setObservationTimeUtc(LocalDateTime.now());
        weather.setLocation(location);
        WeatherReportMapper.from(weather);
    }
}
