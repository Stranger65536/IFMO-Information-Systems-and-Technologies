package ru.ifmo.m3405;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.io.StringReader;

@SuppressWarnings("FieldNotUsedInToString")
public class Robot {
    private static final int RADIUS = 28;
    private static final int PI_DEGREES = 180;
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @JsonProperty
    private final String uuid;
    @JsonProperty
    private final int width;
    @JsonProperty
    private final int height;
    @JsonProperty
    private final int speed;
    @JsonProperty
    private final int initialDistanceToCross;

    @JsonCreator
    public Robot(
            @JsonProperty("uuid") final String uuid,
            @JsonProperty("initialDistanceToCross") final int initialDistanceToCross,
            @JsonProperty("speed") final int speed,
            @JsonProperty("height") final int height,
            @JsonProperty("width") final int width) {
        this.uuid = uuid;
        this.initialDistanceToCross = initialDistanceToCross;
        this.speed = speed;
        this.height = height;
        this.width = width;
    }

    public static Robot valueOf(final String s) throws IOException {
        return MAPPER.readerFor(Robot.class).<Robot>readValue(new StringReader(s));
    }

    public static double angleSpeedToMetric(final int angleSpeed) {
        return Math.PI * RADIUS / PI_DEGREES * angleSpeed;
    }

    public static int metricSpeedToAngle(final double metricSpeed) {
        return Double.valueOf(PI_DEGREES * metricSpeed / (Math.PI * RADIUS)).intValue();
    }

    @Override
    public String toString() {
        try {
            return MAPPER.enable(SerializationFeature.INDENT_OUTPUT).writeValueAsString(this);
        } catch (JsonProcessingException ignored) {
            return "Parsing error";
        }
    }

    public String getUuid() {
        return uuid;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getSpeed() {
        return speed;
    }

    public int getInitialDistanceToCross() {
        return initialDistanceToCross;
    }
}
