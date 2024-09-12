package se.martin.weather.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.apicurio.registry.serde.jsonschema.JsonSchemaKafkaSerializer;
import se.martin.weather.model.Visibility;
import se.martin.weather.model.Weather;

import java.io.IOException;

/**
 * Custom serializer to map from domain object to Json
 */
public class WeatherSerializer extends JsonSchemaKafkaSerializer<Weather> {

    public WeatherSerializer() {
        super();

        // Provide an object mapper to do domain specific deserialization
        var objectMapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.registerModule(new JavaTimeModule());

        var simpleModule = new SimpleModule();
        simpleModule.addSerializer(Visibility.class, new VisibilitySerializer(Visibility.class));
        objectMapper.registerModule(simpleModule);

        this.setObjectMapper(objectMapper);
    }

    private static class VisibilitySerializer extends StdSerializer<Visibility> {

        private VisibilitySerializer(Class<Visibility> v) {
            super(v);
        }

        @Override
        public void serialize(Visibility value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeString(VisibilityMapper.toJsonValue(value));
        }
    }
}
