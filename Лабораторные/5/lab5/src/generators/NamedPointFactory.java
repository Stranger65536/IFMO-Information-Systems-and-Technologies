package generators;

import ontology.NamedPoint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum NamedPointFactory {
    ABSTRACT_NAMED_POINT {
        @Override
        public NamedPoint makeNamedPoint() {
            Collections.shuffle(PLACES);

            return new NamedPoint(
                    PointFactory.ABSTRACT_POINT.makePoint(),
                    PLACES.stream().findAny().orElse("")
            );
        }
    };

    private static final List<String> PLACES = new ArrayList<>(Stream.of(
            "accounting",
            "airport",
            "amusement park",
            "aquarium",
            "art gallery",
            "atm",
            "bakery",
            "bank",
            "bar",
            "beauty salon",
            "bicycle store",
            "book store",
            "bowling alley",
            "bus station",
            "cafe",
            "campground",
            "car dealer",
            "car rental",
            "car repair",
            "car wash",
            "casino",
            "cemetery",
            "church",
            "city hall",
            "clothing store",
            "convenience store",
            "courthouse",
            "dentist",
            "department store",
            "doctor",
            "electrician",
            "electronics store",
            "embassy",
            "establishment",
            "finance",
            "fire station",
            "florist",
            "food",
            "funeral home",
            "furniture store",
            "gas station",
            "general contractor",
            "grocery or supermarket",
            "gym",
            "hair care",
            "hardware store",
            "health",
            "hindu temple",
            "home goods store",
            "hospital",
            "insurance agency",
            "jewelry store",
            "laundry",
            "lawyer",
            "library",
            "liquor store",
            "local government office",
            "locksmith",
            "lodging",
            "meal delivery",
            "meal takeaway",
            "mosque",
            "movie rental",
            "movie theater",
            "moving company",
            "museum",
            "night club",
            "painter",
            "park",
            "parking",
            "pet store",
            "pharmacy",
            "physiotherapist",
            "place of worship",
            "plumber",
            "police",
            "post office",
            "real estate agency",
            "restaurant",
            "roofing contractor",
            "rv park",
            "school",
            "shoe store",
            "shopping mall",
            "spa",
            "stadium",
            "storage",
            "store",
            "subway station",
            "synagogue",
            "taxi stand",
            "train station",
            "travel agency",
            "university",
            "veterinary care",
            "zoo"
    ).collect(Collectors.toList()));

    public abstract NamedPoint makeNamedPoint();
}
