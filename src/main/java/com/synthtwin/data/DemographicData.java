package com.synthtwin.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class DemographicData {

    public static final String[] MALE_FIRST_NAMES = {
        "James", "John", "Robert", "Michael", "William", "David", "Richard", "Joseph", "Thomas", "Charles",
        "Christopher", "Daniel", "Matthew", "Anthony", "Mark", "Donald", "Steven", "Paul", "Andrew", "Joshua",
        "Kenneth", "Kevin", "Brian", "George", "Timothy", "Ronald", "Edward", "Jason", "Jeffrey", "Ryan",
        "Jacob", "Gary", "Nicholas", "Eric", "Jonathan", "Stephen", "Larry", "Justin", "Scott", "Brandon",
        "Benjamin", "Samuel", "Raymond", "Gregory", "Frank", "Alexander", "Patrick", "Jack", "Dennis", "Jerry"
    };

    public static final String[] FEMALE_FIRST_NAMES = {
        "Mary", "Patricia", "Jennifer", "Linda", "Barbara", "Elizabeth", "Susan", "Jessica", "Sarah", "Karen",
        "Lisa", "Nancy", "Betty", "Margaret", "Sandra", "Ashley", "Dorothy", "Kimberly", "Emily", "Donna",
        "Michelle", "Carol", "Amanda", "Melissa", "Deborah", "Stephanie", "Rebecca", "Sharon", "Laura", "Cynthia",
        "Kathleen", "Amy", "Angela", "Shirley", "Anna", "Brenda", "Pamela", "Emma", "Nicole", "Helen",
        "Samantha", "Katherine", "Christine", "Debra", "Rachel", "Carolyn", "Janet", "Catherine", "Maria", "Heather"
    };

    public static final String[] LAST_NAMES = {
        "Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis", "Rodriguez", "Martinez",
        "Hernandez", "Lopez", "Gonzalez", "Wilson", "Anderson", "Thomas", "Taylor", "Moore", "Jackson", "Martin",
        "Lee", "Perez", "Thompson", "White", "Harris", "Sanchez", "Clark", "Ramirez", "Lewis", "Robinson",
        "Walker", "Young", "Allen", "King", "Wright", "Scott", "Torres", "Nguyen", "Hill", "Flores",
        "Green", "Adams", "Nelson", "Baker", "Hall", "Rivera", "Campbell", "Mitchell", "Carter", "Roberts",
        "Gomez", "Phillips", "Evans", "Turner", "Diaz", "Parker", "Cruz", "Edwards", "Collins", "Reyes",
        "Stewart", "Morris", "Morales", "Murphy", "Cook", "Rogers", "Gutierrez", "Ortiz", "Morgan", "Cooper",
        "Peterson", "Bailey", "Reed", "Kelly", "Howard", "Ramos", "Kim", "Cox", "Ward", "Richardson",
        "Watson", "Brooks", "Chavez", "Wood", "James", "Bennett", "Gray", "Mendoza", "Ruiz", "Hughes",
        "Price", "Alvarez", "Castillo", "Sanders", "Patel", "Myers", "Long", "Ross", "Foster", "Jimenez"
    };

    public static final Map<String, String[]> STATES = new HashMap<>();

    static {
        STATES.put("MA", new String[]{"Boston", "Worcester", "Springfield", "Cambridge", "Lowell"});
        STATES.put("CA", new String[]{"Los Angeles", "San Francisco", "San Diego", "Sacramento", "San Jose"});
        STATES.put("TX", new String[]{"Houston", "Austin", "Dallas", "San Antonio", "Fort Worth"});
        STATES.put("NY", new String[]{"New York", "Buffalo", "Albany", "Rochester", "Yonkers"});
        STATES.put("FL", new String[]{"Miami", "Orlando", "Tampa", "Jacksonville", "Fort Lauderdale"});
        STATES.put("OH", new String[]{"Columbus", "Cleveland", "Cincinnati", "Dayton", "Akron"});
        STATES.put("IL", new String[]{"Chicago", "Springfield", "Rockford", "Peoria", "Naperville"});
        STATES.put("PA", new String[]{"Philadelphia", "Pittsburgh", "Allentown", "Erie", "Reading"});
        STATES.put("AZ", new String[]{"Phoenix", "Tucson", "Scottsdale", "Mesa", "Chandler"});
        STATES.put("WA", new String[]{"Seattle", "Spokane", "Tacoma", "Bellevue", "Kirkland"});
    }

    public static final String[] RACES = {
        "White", "Black or African American", "Asian", "American Indian or Alaska Native",
        "Native Hawaiian or Other Pacific Islander", "Other Race", "Mixed Race"
    };

    public static final String[] ETHNICITIES = {
        "Not Hispanic or Latino", "Hispanic or Latino", "Unknown"
    };

    public static final String[] STREET_NAMES = {
        "Main", "Oak", "Pine", "Maple", "Cedar", "Elm", "Washington", "Lake", "Hill", "Park",
        "River", "Sunset", "Highland", "Forest", "Meadow", "Valley", "Spring", "Church", "Mill", "Bridge",
        "School", "Liberty", "Union", "Center", "Garden", "Pleasant", "Willow", "Poplar", "Cherry", "Walnut",
        "Lincoln", "Jefferson", "Madison", "Monroe", "Adams", "Franklin", "Grant", "Sherman", "Sheridan", "Ridge"
    };

    public static final String[] STREET_TYPES = {
        "St", "Ave", "Blvd", "Rd", "Dr", "Ln", "Way", "Pl"
    };

    public static String getRandomState(Random random) {
        String[] stateKeys = STATES.keySet().toArray(new String[0]);
        return stateKeys[random.nextInt(stateKeys.length)];
    }

    public static String getRandomCity(String state, Random random) {
        String[] cities = STATES.get(state);
        if (cities == null || cities.length == 0) return "Unknown";
        return cities[random.nextInt(cities.length)];
    }
}
