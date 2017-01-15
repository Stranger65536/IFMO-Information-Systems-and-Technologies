package ontology;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;

/**
 * Geolocation point of latitude and longitude
 */
public class Point {
    private static final int LATITUDE_ABS_MAX = 90;
    private static final int LONGITUDE_ABS_MAX = 180;
    /**
     * Latitude of point
     */
    private final double latitude;
    /**
     * Latitude of point
     */
    private final double longitude;

    @JsonCreator
    public Point(
            @JsonProperty("latitude")
            final double latitude,
            @JsonProperty("longitude")
            final double longitude) {
        Preconditions.checkArgument(Math.abs(latitude) <= LATITUDE_ABS_MAX, "Latitude must be in range [-90; 90]");
        Preconditions.checkArgument(Math.abs(longitude) <= LONGITUDE_ABS_MAX, "Longitude must be in range [-180; 180]");

        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override
    public int hashCode() {
        final long temp = Double.doubleToLongBits(latitude);
        int result = (int) (temp ^ temp >>> 32);
        final long doubleToLongBits = Double.doubleToLongBits(longitude);
        result = 31 * result + (int) (doubleToLongBits ^ doubleToLongBits >>> 32);
        return result;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final Point point = (Point) o;

        return Double.compare(point.latitude, latitude) == 0 &&
                Double.compare(point.longitude, longitude) == 0;
    }
}
