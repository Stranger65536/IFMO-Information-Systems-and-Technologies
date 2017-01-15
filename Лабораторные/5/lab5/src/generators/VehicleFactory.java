package generators;

import ontology.Type;
import ontology.Vehicle;

import java.math.BigInteger;
import java.util.Random;

public enum VehicleFactory {
    PEDESTRIAN {
        @Override
        public Vehicle makeVehicle(final int passengerId) {
            final int numerOfArms = RANDOM.nextInt(3); //0-2
            final int humanSpeed = RANDOM.nextInt(6); //0-5
            final BigInteger ownerUID = BigInteger.valueOf(passengerId);

            return new Vehicle(
                    Type.PEDESTRIAN,
                    NO_SEATS_PROVIDED,
                    numerOfArms,
                    NO_FUEL_PROVIDED,
                    humanSpeed,
                    ownerUID
            );
        }
    },
    CAR {
        @Override
        public Vehicle makeVehicle(final int passengerId) {
            final int carCapacity = RANDOM.nextInt(31) + 1; //1-30
            final int carSpeed = RANDOM.nextInt(101) + 20; //20-120
            final int carFuelLevel = RANDOM.nextInt(101); //0-100
            final BigInteger carOwnerUID = BigInteger.valueOf(passengerId);

            return new Vehicle(
                    Type.CAR,
                    carCapacity,
                    carCapacity,
                    carFuelLevel,
                    carSpeed,
                    carOwnerUID
            );
        }
    },
    BUS {
        @Override
        public Vehicle makeVehicle(final int passengerId) {
            final int numberOfWagons = RANDOM.nextInt(2) + 1; //1-2
            final int wagonCapacity = RANDOM.nextInt(156) + 20; //20-175
            final int busSpeed = RANDOM.nextInt(61) + 40; //40-100
            final int currentFuelLevel = RANDOM.nextInt(101); //0-100
            final BigInteger busOwnerUID = BigInteger.valueOf(RANDOM.nextInt() + (long) Integer.MAX_VALUE); //big value

            return new Vehicle(
                    Type.BUS,
                    numberOfWagons * wagonCapacity,
                    numberOfWagons * wagonCapacity,
                    currentFuelLevel,
                    busSpeed,
                    busOwnerUID
            );
        }
    },
    TROLLEYBUS {
        @Override
        public Vehicle makeVehicle(final int passengerId) {
            final int numberOfWagons = RANDOM.nextInt(2) + 1; //1-2
            final int wagonCapacity = RANDOM.nextInt(41) + 80; //80-120
            final int trolleybusSpeed = RANDOM.nextInt(41) + 50; //50-90
            final BigInteger trolleybusOwnerUID = BigInteger.valueOf(RANDOM.nextInt() + (long) Integer.MAX_VALUE); //big value

            return new Vehicle(
                    Type.TROLLEYBUS,
                    numberOfWagons * wagonCapacity,
                    numberOfWagons * wagonCapacity,
                    NO_FUEL_PROVIDED,
                    trolleybusSpeed,
                    trolleybusOwnerUID
            );
        }
    },
    TRAM {
        @Override
        public Vehicle makeVehicle(final int passengerId) {
            final int numberOfWagons = RANDOM.nextInt(2) + 1; //1-2
            final int wagonCapacity = RANDOM.nextInt(101) + 100; //100-200
            final int tramSpeed = RANDOM.nextInt(101) + 20; //20-120
            final BigInteger tramOwnerUID = BigInteger.valueOf(RANDOM.nextInt() + (long) Integer.MAX_VALUE); //big value

            return new Vehicle(
                    Type.TRAM,
                    numberOfWagons * wagonCapacity,
                    numberOfWagons * wagonCapacity,
                    NO_FUEL_PROVIDED,
                    tramSpeed,
                    tramOwnerUID
            );
        }
    },
    SUBWAY {
        @Override
        public Vehicle makeVehicle(final int passengerId) {
            final int numberOfWagons = RANDOM.nextInt(3) + 3; //3-5
            final BigInteger subwayOwnerUID = BigInteger.valueOf(RANDOM.nextInt() + (long) Integer.MAX_VALUE); //big value

            return new Vehicle(
                    Type.SUBWAY,
                    numberOfWagons * CAPACITY_OF_SUBWAY_WAGON,
                    numberOfWagons * CAPACITY_OF_SUBWAY_WAGON,
                    NO_FUEL_PROVIDED,
                    AVERAGE_SUBWAY_TRAIN_SPEED,
                    subwayOwnerUID
            );
        }
    },
    ABSTRACT_VEHICLE {
        @Override
        public Vehicle makeVehicle(final int passengerId) {
            int ordinal;

            do {
                ordinal = RANDOM.nextInt(VehicleFactory.values().length);
            } while (ordinal == this.ordinal());

            return VehicleFactory.values()[ordinal].makeVehicle(passengerId);
        }
    };

    private static final Random RANDOM = new Random();
    private static final int NO_SEATS_PROVIDED = 0;
    private static final int NO_FUEL_PROVIDED = 0;
    private static final int AVERAGE_SUBWAY_TRAIN_SPEED = 45;
    private static final int CAPACITY_OF_SUBWAY_WAGON = 300;

    public abstract Vehicle makeVehicle(final int passengerId);
}
