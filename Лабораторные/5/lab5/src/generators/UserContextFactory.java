package generators;

import ontology.*;

import java.net.MalformedURLException;
import java.time.ZonedDateTime;
import java.util.Random;

public enum UserContextFactory {
    ABSTRACT_USER_CONTEXT {
        @Override
        public UserContext makeContext() throws MalformedURLException {
            final Profile profile = ProfileFactory.ABSTRACT_PROFILE.makeProfile();
            final ZonedDateTime timeNow = ZonedDateTime.now();
            final Point currentPoint = PointFactory.ABSTRACT_POINT.makePoint();
            final int currentSpeed = Math.abs(RANDOM.nextInt(5) - 2 + profile.getVehicle().getSpeed()); //avg+-2
            final int traffic = RANDOM.nextInt(11); //0-10
            final Weather weather = WeatherFactory.ABSTRACT_WEATHER.makeWeather();

            return new UserContext(
                    profile,
                    timeNow,
                    currentPoint,
                    currentSpeed,
                    traffic,
                    weather
            );
        }
    };

    private static final Random RANDOM = new Random();

    public abstract UserContext makeContext() throws MalformedURLException;
}
