package generators;

import ontology.Point;

import java.util.Random;

public enum PointFactory {
    ABSTRACT_POINT {
        @Override
        public Point makePoint() {
            final double latitude = LATITUDE_ABS_MAX * 2 * (RANDOM.nextDouble() - 0.5);
            final double longitude = LONGITUDE_ABS_MAX * 2 * (RANDOM.nextDouble() - 0.5);

            return new Point(latitude, longitude);
        }
    };

    private static final Random RANDOM = new Random();
    private static final int LATITUDE_ABS_MAX = 90;
    private static final int LONGITUDE_ABS_MAX = 180;

    public abstract Point makePoint();
}
