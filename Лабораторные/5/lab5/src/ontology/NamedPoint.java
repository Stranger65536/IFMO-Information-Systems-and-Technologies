package ontology;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;

public class NamedPoint {
    /**
     * Geolocation of point
     */
    private final Point point;
    /**
     * Label for current point
     */
    private final String name;

    @JsonCreator
    @SuppressWarnings("DuplicateStringLiteralInspection")
    public NamedPoint(
            @JsonProperty("point")
            final Point point,
            @JsonProperty("name")
            final String name) {
        Preconditions.checkNotNull("Point must not be null");
        Preconditions.checkNotNull("Name must not be null");

        Preconditions.checkArgument(!name.trim().isEmpty(), "Name must not be empty");

        this.point = point;
        this.name = name;
    }

    public Point getPoint() {
        return point;
    }

    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        int result = point.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }

    @Override
    @SuppressWarnings("InstanceofInterfaces")
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof NamedPoint)) {
            return false;
        }

        final NamedPoint namedPoint = (NamedPoint) o;

        return point.equals(namedPoint.point) &&
                name.equals(namedPoint.name);
    }
}
