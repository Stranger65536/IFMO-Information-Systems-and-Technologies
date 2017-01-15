package serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.Duration;

public class StringSerializer extends JsonSerializer {

    @Override
    public void serialize(
            final Object value,
            final JsonGenerator gen,
            final SerializerProvider serializers) throws IOException {
        gen.writeObject(value.toString());
    }
}
