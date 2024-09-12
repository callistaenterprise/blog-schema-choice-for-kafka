package se.martin.weather.proto;

import se.martin.weather.model.Weather;

import java.time.format.DateTimeFormatter;

import static se.martin.weather.proto.Visibility.*;

/**
 * Class to map from domain objects to protobuf generated objects
 */
public class WeatherReportMapper {

    public static WeatherReport from(Weather weather) {
        return WeatherReport.newBuilder()
                .setRecordingId(weather.getRecordingId().toString())
                .setObservationTimeUtc(DateTimeFormatter.ISO_DATE_TIME.format(weather.getObservationTimeUtc()))
                .setObservations(from(weather.getObservations()))
                .setLocation(from(weather.getLocation()))
                .build();
    }

    private static Location from(Weather.Location location) {
        Location.Builder builder = Location.newBuilder();
        builder.setName(location.getName())
                .setStationId(location.getStationId())
                .setLongitude(location.getLongitude())
                .setLatitude(location.getLatitude());
        if (location.getElevation() != null) {
            builder.setElevation(location.getElevation());
        }
        return builder.build();
    }

    private static Observations from(Weather.Observations observations) {
        if (observations == null) {
            return Observations.newBuilder().build();
        }
        return Observations.newBuilder()
                .setSolarRadiation(observations.getSolarRadiation())
                .setUltraViolet(observations.getUltraViolet())
                .setPrecipitationRate(observations.getPrecipitationRate())
                .setPrecipitationRate(observations.getPrecipitationRate())
                .setPrecipitationTotal24Hh(observations.getPrecipitationTotal24h())
                .setTemperatureCelsius(observations.getTemperatureCelsius())
                .setWindChillCelsius(observations.getWindSpeed())
                .setWindSpeed(observations.getWindSpeed())
                .setVisibility(from(observations.getVisibility()))
                .build();
    }

    private static Visibility from(se.martin.weather.model.Visibility visibility) {
        return switch (visibility) {
            case Good -> GOOD;
            case Average -> AVERAGE;
            case Poor -> POOR;
            case Darkness -> DARKNESS;
        };
    }
}
