package ontology;

/**
 * onthology.Vehicle types
 */
public enum Type {
    PEDESTRIAN("Pedestrian"),
    CAR("Car"),
    BUS("Bus"),
    TROLLEYBUS("Trolleybus"),
    TRAM("Tram"),
    SUBWAY("Subway");

    private final String value;

    Type(final String text) {
        this.value = text;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
