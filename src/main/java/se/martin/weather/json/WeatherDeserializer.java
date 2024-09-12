package se.martin.weather.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.apicurio.registry.serde.jsonschema.JsonSchemaKafkaDeserializer;
import se.martin.weather.model.Visibility;
import se.martin.weather.model.Weather;

import java.io.IOException;


/**
 * Custom deserializer to map from Json to domain object
 */
public class WeatherDeserializer extends JsonSchemaKafkaDeserializer<Weather> {

    public WeatherDeserializer() {
        super();

        // Provide an object mapper to do domain specific deserialization
        var objectMapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.registerModule(new JavaTimeModule());

        var simpleModule = new SimpleModule();
        simpleModule.addDeserializer(Visibility.class, new VisibilityDeserializer(Visibility.class));
        objectMapper.registerModule(simpleModule);

        this.setObjectMapper(objectMapper);
    }

    private static class VisibilityDeserializer extends StdDeserializer<Visibility> {

        private VisibilityDeserializer(Class<Visibility> v) {
            super(v);
        }

        @Override
        public Visibility deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
            var node = p.getCodec().readTree(p);
            var textNode = (TextNode) node;
            var jsonValue = textNode.asText();
            return VisibilityMapper.fromJsonValue(jsonValue);
        }
    }
}
