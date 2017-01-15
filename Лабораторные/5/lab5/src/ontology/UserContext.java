package ontology;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.Preconditions;
import serializers.StringSerializer;
import serializers.ZonedDateTimeDeserializer;

import java.time.ZonedDateTime;

public class UserContext {
    /**
     * User profile
     */
    private final Profile profile;
    /**
     * Current date and time with timezone
     */
    @JsonSerialize(using = StringSerializer.class)
    @JsonDeserialize(using = ZonedDateTimeDeserializer.class)
    private final ZonedDateTime currentDateTime;
    /**
     * Current position
     */
    private final Point currentPoint;
    /**
     * Current speed
     */
    private final int speed;
    /**
     * Around traffic index
     */
    private final int traffic;
    /**
     * Current weather
     */
    private final Weather weather;

    @JsonCreator
    @SuppressWarnings("DuplicateStringLiteralInspection")
    public UserContext(
            @JsonProperty("profile")
            final Profile profile,
            @JsonProperty("currentDateTime")
            final ZonedDateTime currentDateTime,
            @JsonProperty("currentPoint")
            final Point currentPoint,
            @JsonProperty("speed")
            final int speed,
            @JsonProperty("traffic")
            final int traffic,
            @JsonProperty("weather")
            final Weather weather) {
        Preconditions.checkNotNull(profile, "Profile must not be null");
        Preconditions.checkNotNull(currentDateTime, "Current datetime must not be null");
        Preconditions.checkNotNull(currentPoint, "Point must not be null");
        Preconditions.checkNotNull(weather, "Weather must not be null");

        Preconditions.checkArgument(speed >= 0, "Speed must not be negative");
        Preconditions.checkArgument(traffic >= 0, "Traffic must not be negative");

        this.profile = profile;
        this.currentDateTime = currentDateTime;
        this.currentPoint = currentPoint;
        this.speed = speed;
        this.traffic = traffic;
        this.weather = weather;
    }

    public Profile getProfile() {
        return profile;
    }

    public ZonedDateTime getCurrentDateTime() {
        return currentDateTime;
    }

    public Point getCurrentPoint() {
        return currentPoint;
    }

    public int getSpeed() {
        return speed;
    }

    public int getTraffic() {
        return traffic;
    }

    public Weather getWeather() {
        return weather;
    }

    @Override
    public int hashCode() {
        int result = profile.hashCode();
        result = 31 * result + currentDateTime.hashCode();
        result = 31 * result + currentPoint.hashCode();
        result = 31 * result + speed;
        result = 31 * result + traffic;
        result = 31 * result + weather.hashCode();
        return result;
    }

    @Override
    @SuppressWarnings({"InstanceofInterfaces", "OverlyComplexBooleanExpression"})
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof UserContext)) {
            return false;
        }

        final UserContext userContext = (UserContext) o;

        return speed == userContext.speed &&
                traffic == userContext.traffic &&
                profile.equals(userContext.profile) &&
                currentDateTime.equals(userContext.currentDateTime) &&
                currentPoint.equals(userContext.currentPoint) &&
                weather.equals(userContext.weather);

    }
}
