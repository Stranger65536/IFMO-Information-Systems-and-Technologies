package ontology;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;

public class Weather {
    private static final int ABSOLUTE_ZERO_CELSIUS = -273;
    /**
     * Current temperature in celsius
     */
    private final int temperature;
    /**
     * Humidity in percents
     */
    private final int humidity;
    /**
     * Atmosphere pressure in mm of mercury column
     */
    private final int pressure;
    /**
     * Precipitation in free format
     */
    private final String precipitation;

    @JsonCreator
    public Weather(
            @JsonProperty("temperature")
            final int temperature,
            @JsonProperty("humidity")
            final int humidity,
            @JsonProperty("pressure")
            final int pressure,
            @JsonProperty("precipitation")
            final String precipitation) {
        Preconditions.checkNotNull(precipitation, "Precipitation must not be null");

        Preconditions.checkArgument(temperature >= ABSOLUTE_ZERO_CELSIUS, "Temperate must be greater than absolute zero");
        Preconditions.checkArgument(humidity <= 100 && humidity >= 0, "Humidity must be in range [0; 100]");
        Preconditions.checkArgument(pressure >= 0, "Pressure must be greater or equals than 0");
        Preconditions.checkArgument(!precipitation.trim().isEmpty(), "Precipitation must not be empty");

        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
        this.precipitation = precipitation;
    }

    public int getTemperature() {
        return temperature;
    }

    public int getHumidity() {
        return humidity;
    }

    public int getPressure() {
        return pressure;
    }

    public String getPrecipitation() {
        return precipitation;
    }

    @Override
    public int hashCode() {
        int result = temperature;
        result = 31 * result + humidity;
        result = 31 * result + pressure;
        result = 31 * result + precipitation.hashCode();
        return result;
    }

    @Override
    @SuppressWarnings({"InstanceofInterfaces", "OverlyComplexBooleanExpression"})
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Weather)) {
            return false;
        }

        final Weather weather = (Weather) o;

        return temperature == weather.temperature &&
                humidity == weather.humidity &&
                pressure == weather.pressure &&
                precipitation.equals(weather.precipitation);

    }
}
