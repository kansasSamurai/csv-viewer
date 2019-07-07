package org.jwellman.foarchive;

/**
 *
 * @author Rick
 */
public class uUtility {

    public static String preferStringOverNull(String candidate, String override) {
        return (candidate == null) ? override : candidate;
    }

}
