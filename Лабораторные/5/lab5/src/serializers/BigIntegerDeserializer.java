package serializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.math.BigInteger;

public class BigIntegerDeserializer extends JsonDeserializer<BigInteger> {

    @Override
    public BigInteger deserialize(
            final JsonParser p,
            final DeserializationContext ctxt) throws IOException {
        return new BigInteger(p.getValueAsString());
    }
}
