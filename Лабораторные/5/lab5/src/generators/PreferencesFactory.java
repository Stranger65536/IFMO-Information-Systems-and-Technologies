package generators;

import ontology.Preferences;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Random;

public enum PreferencesFactory {
    ABSTRACT_PREFERENCES {
        @Override
        @SuppressWarnings("NonReproducibleMathCall")
        public Preferences makePreferences() {
            final int distance = RANDOM.nextInt(51); //0-50
            final Duration duration = Duration.of(RANDOM.nextInt(1_000_000) + 1000, ChronoUnit.MILLIS);

            return new Preferences(distance, duration);
        }
    };

    private static final Random RANDOM = new Random();

    public abstract Preferences makePreferences();
}
