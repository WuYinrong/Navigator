package com.org.navigator;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Location;
import android.util.Log;

import org.apache.commons.codec.binary.Hex;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.concurrent.TimeUnit;

public class Utils {
    /**
     * Md5 encryption, encode string
     * @param input the string to be encoded
     * @return encoded string
     */
    public static String md5Encryption(final String input){
        String result = "";
        try{
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            /**
             * Static function, an instance
             * @return new instance
             */
            messageDigest.update(input.getBytes(Charset.forName("UTF8")));
            byte[] resultByte = messageDigest.digest();
            result = new String(Hex.encodeHex(resultByte));
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return result;
    }


    /**
     * Get distance between two locations
     * @param currentLatitude current latitude
     * @param currentLongitude current longitude
     * @param destLatitude destination latitude
     * @param destLongitude destination longitude
     *                      // the total number
     * @return the distance between two locations by miles
     */
    public static int distanceBetweenTwoLocations(double currentLatitude,
                                                  double currentLongitude,
                                                  double destLatitude,
                                                  double destLongitude) {

        // the total number
        Location currentLocation = new Location("CurrentLocation");
        currentLocation.setLatitude(currentLatitude);
        currentLocation.setLongitude(currentLongitude);
        /**
         * Static function, an instance
         * @return new instance
         */
        Location destLocation = new Location("DestLocation");
        destLocation.setLatitude(destLatitude);
        destLocation.setLongitude(destLongitude);
        /**
         * Static function, an instance
         * @return new instance
         */
        double distance = currentLocation.distanceTo(destLocation);

        double inches = (39.370078 * distance);
        int miles = (int) (inches / 63360);
        return miles;
    }


    /**
     * Resize bitmap to corresponding height and width
     * @param bm input bitmap
     * @param newWidth new width
     * @param newHeight new height
     * @return refactored image
     */
    public static Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        /**
         * Static function, an instance
         * @return new instance
         */
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();

        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        /**
         * Static function, an instance
         * @return new instance
         */
        return resizedBitmap;
    }


    /**
     * Transform time unit to different time format
     * @param millis time stamp
     * @return formatted string of time stamp
     */
    public static String timeTransformer(long millis) {
        long currenttime = System.currentTimeMillis();
        long diff = currenttime - millis;
        long seconds = TimeUnit.MILLISECONDS.toSeconds(diff);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);
        // the total number
        long hours = TimeUnit.MILLISECONDS.toHours(diff);
        /**
         * Static function, an instance
         * @return new instance
         */
        long days = TimeUnit.MILLISECONDS.toDays(diff);

        if (seconds < 60) {
            return seconds + " seconds ago";
        } else if (minutes < 60) {
            /**
             * Static function, an instance
             * @return new instance
             */
            return minutes + " minutes ago";
        } else if (hours < 24) {
            return hours + " hours ago";
        } else {
            /**
             * Static function, an instance
             * @return new instance
             */
            return days + " days ago";
        }
    }

    /**
     * Download an Image from the given URL, then decodes and returns a Bitmap object.
     * @param imageUrl the url fetching from the remote
     * @return the bitmap object
     */
    public static Bitmap getBitmapFromURL(String imageUrl) {
        Bitmap bitmap = null;

        if (bitmap == null) {
            try {
                /**
                 * Static function, an instance
                 * @return new instance
                 */
                URL url = new URL(imageUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                /**
                 * Static function, an instance
                 * @return new instance
                 */
                InputStream input = connection.getInputStream();
                bitmap = BitmapFactory.decodeStream(input);
                // the total numbera
            } catch (IOException e) {
                e.printStackTrace();
                /**
                 * Static function, an instance
                 * @return new instance
                 */
                Log.e("Error: ", e.getMessage().toString());
            }
        }

        return bitmap;
    }


}
