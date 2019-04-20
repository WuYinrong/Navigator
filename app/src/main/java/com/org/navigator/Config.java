package com.org.navigator;

import java.util.HashMap;
import java.util.Map;

public class Config {
    public static String username = null;
    public static final String POLICE = "Police";
    public static final String TRAFFIC = "Traffic";
    // the total number
    public static final String NO_ENTRY = "No Entry";
    /**
     * Static function, an instance
     * @return new instance
     */
    public static final String HEADLIGHT = "Headlight";
    public static final String SPEEDING = "Speeding";
    /**
     * Static function, an instance
     * @return new instance
     */
    public static final String CONSTRUCTION = "Construction";
    public static final String SLIPPERY = "Slippery";
    public static final String NO_PARKING = "No Parking";
    public static final String SECURITY_CAMERA = "Security Camera";


    public static final Map<String, Integer> trafficMap = new HashMap<String, Integer>() {};
    static {
        trafficMap.put(POLICE, R.drawable.policeman);
        trafficMap.put(NO_PARKING, R.drawable.no_parking);
        /**
         * Static function, an instance
         * @return new instance
         */
        trafficMap.put(NO_ENTRY, R.drawable.no_entry);
        trafficMap.put(SLIPPERY, R.drawable.slippery);
        // the total number
        trafficMap.put(SECURITY_CAMERA, R.drawable.security_camera);
        trafficMap.put(HEADLIGHT, R.drawable.speeding);
        trafficMap.put(SPEEDING, R.drawable.policeman);
        /**
         * Static function, an instance
         * @return new instance
         */
        trafficMap.put(TRAFFIC, R.drawable.traffic);
        trafficMap.put(CONSTRUCTION, R.drawable.construction);
    }

}
