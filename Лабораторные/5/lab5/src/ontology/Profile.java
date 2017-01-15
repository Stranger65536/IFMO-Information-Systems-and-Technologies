package ontology;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.Preconditions;
import serializers.StringSerializer;

import java.net.URL;
import java.util.List;

public class Profile {
    /**
     * Unique ID of person
     */
    private final int id;
    /**
     * Full name of person
     */
    private final String name;
    /**
     * Primary vehicle of person
     */
    private final Vehicle vehicle;
    /**
     * Primary social account link
     */
    @JsonSerialize(using = StringSerializer.class)
    private final URL socialAccount;
    /**
     * Description of interests
     */
    private final String interests;
    /**
     * List of stored places
     */
    private final List<NamedPoint> points;
    /**
     * Personal preferences
     */
    private final Preferences preferences;

    @JsonCreator
    @SuppressWarnings("DuplicateStringLiteralInspection")
    public Profile(
            @JsonProperty("id")
            final int id,
            @JsonProperty("name")
            final String name,
            @JsonProperty("vehicle")
            final Vehicle vehicle,
            @JsonProperty("socialAccount")
            final URL socialAccount,
            @JsonProperty("interests")
            final String interests,
            @JsonProperty("points")
            final List<NamedPoint> points,
            @JsonProperty("preferences")
            final Preferences preferences) {
        Preconditions.checkNotNull(name, "Name must not be null");
        Preconditions.checkNotNull(vehicle, "Vehicle must not be null");
        Preconditions.checkNotNull(socialAccount, "Social account must not be null");
        Preconditions.checkNotNull(interests, "Interests must not be null");
        Preconditions.checkNotNull(points, "Points must not be null");
        Preconditions.checkNotNull(preferences, "Preferences must not be null");

        Preconditions.checkArgument(!name.trim().isEmpty(), "Name must not be empty");
        Preconditions.checkArgument(!name.trim().isEmpty(), "Interests must not be empty");

        this.id = id;
        this.name = name;
        this.vehicle = vehicle;
        this.socialAccount = socialAccount;
        this.interests = interests;
        this.points = points;
        this.preferences = preferences;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public URL getSocialAccount() {
        return socialAccount;
    }

    public String getInterests() {
        return interests;
    }

    public List<NamedPoint> getPoints() {
        return points;
    }

    public Preferences getPreferences() {
        return preferences;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + name.hashCode();
        result = 31 * result + vehicle.hashCode();
        result = 31 * result + socialAccount.toString().hashCode();
        result = 31 * result + interests.hashCode();
        result = 31 * result + points.hashCode();
        result = 31 * result + preferences.hashCode();
        return result;
    }

    @Override
    @SuppressWarnings({"InstanceofInterfaces", "OverlyComplexBooleanExpression"})
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Profile)) {
            return false;
        }

        final Profile profile = (Profile) o;

        return id == profile.id &&
                name.equals(profile.name) &&
                vehicle.equals(profile.vehicle) &&
                socialAccount.toString().equals(profile.socialAccount.toString()) &&
                interests.equals(profile.interests) &&
                points.equals(profile.points) &&
                preferences.equals(profile.preferences);

    }
}
