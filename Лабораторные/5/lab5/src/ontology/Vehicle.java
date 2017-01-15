package ontology;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.Preconditions;
import serializers.BigIntegerDeserializer;
import serializers.StringSerializer;

import java.math.BigInteger;

public class Vehicle {
    /**
     * Type of transport
     */
    private final Type type;
    /**
     * Total number of seats
     */
    private final int seats;
    /**
     * Total number of item places
     */
    private final int itemPlaces;
    /**
     * Fuel percent (for car and bus only)
     */
    private final int fuel;
    /**
     * Average speed in kmh
     */
    private final int speed;
    /**
     * Unique ID of vehicle owner
     */
    @JsonSerialize(using = StringSerializer.class)
    @JsonDeserialize(using = BigIntegerDeserializer.class)
    private final BigInteger owner;

    @JsonCreator
    @SuppressWarnings("DuplicateStringLiteralInspection")
    public Vehicle(
            @JsonProperty("type")
            final Type type,
            @JsonProperty("seats")
            final int seats,
            @JsonProperty("itemPlaces")
            final int itemPlaces,
            @JsonProperty("fuel")
            final int fuel,
            @JsonProperty("speed")
            final int speed,
            @JsonProperty("owner")
            final BigInteger owner) {
        Preconditions.checkNotNull(type, "Type must not be null");
        Preconditions.checkNotNull(owner, "Owner must not be null");

        Preconditions.checkArgument(seats >= 0, "Number of seats must not be negative");
        Preconditions.checkArgument(itemPlaces >= 0, "Number of item places must not be negative");
        Preconditions.checkArgument(fuel >= 0 && fuel <= 100, "Fuel level must be in range [0; 100]");
        Preconditions.checkArgument(speed >= 0, "Speed must not be negative");
        Preconditions.checkArgument(owner.signum() != -1, "Owner ID must not be negative");

        this.type = type;
        this.seats = seats;
        this.itemPlaces = itemPlaces;
        this.fuel = fuel;
        this.speed = speed;
        this.owner = owner;
    }

    public Type getType() {
        return type;
    }

    public int getSeats() {
        return seats;
    }

    public int getItemPlaces() {
        return itemPlaces;
    }

    public int getFuel() {
        return fuel;
    }

    public int getSpeed() {
        return speed;
    }

    public BigInteger getOwner() {
        return owner;
    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + seats;
        result = 31 * result + itemPlaces;
        result = 31 * result + fuel;
        result = 31 * result + speed;
        result = 31 * result + owner.hashCode();
        return result;
    }

    @Override
    @SuppressWarnings({"InstanceofInterfaces", "OverlyComplexBooleanExpression"})
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Vehicle)) {
            return false;
        }

        final Vehicle vehicle = (Vehicle) o;

        return seats == vehicle.seats &&
                itemPlaces == vehicle.itemPlaces &&
                fuel == vehicle.fuel &&
                speed == vehicle.speed &&
                type == vehicle.type &&
                owner.equals(vehicle.owner);
    }
}
