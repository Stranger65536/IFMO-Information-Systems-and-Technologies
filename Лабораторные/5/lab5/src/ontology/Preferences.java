package ontology;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.Preconditions;
import serializers.DurationDeserializer;
import serializers.StringSerializer;

import java.time.Duration;

public class Preferences {
    /**
     * Max walking distance
     */
    private final int detour;
    /**
     * Max duration of waiting
     */
    @JsonSerialize(using = StringSerializer.class)
    @JsonDeserialize(using = DurationDeserializer.class)
    private final Duration delay;

    @JsonCreator
    public Preferences(
            @JsonProperty("detour")
            final int detour,
            @JsonProperty("delay")
            final Duration delay) {
        Preconditions.checkNotNull(delay);

        Preconditions.checkArgument(detour >= 0, "Detour must be positive");
        Preconditions.checkArgument(!delay.isNegative(), "Duration must be not null and not negative");

        this.detour = detour;
        this.delay = delay;
    }

    public int getDetour() {
        return detour;
    }

    public Duration getDelay() {
        return delay;
    }

    @Override
    public int hashCode() {
        int result = detour;
        result = 31 * result + delay.hashCode();
        return result;
    }

    @Override
    @SuppressWarnings("InstanceofInterfaces")
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Preferences)) {
            return false;
        }

        final Preferences preferences = (Preferences) o;

        return detour == preferences.detour &&
                delay.equals(preferences.delay);
    }
}
