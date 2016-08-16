package org.secuso.privacyfriendlywifi.logic.util;

/**
 * Util class for time arithmetic
 */
public class TimeHelper {

    /**
     * Calculate the time difference in seconds between current time and target time. If the target
     * time lies in the past (target_time - current_time < 0), a full day will be added to the result.
     * In this way the calculation result is always positive and implies, that the target time is
     * always in the future.
     *
     * @return Time difference in seconds between current_time and target_time, expecting the target
     * time to be within one day in the future.
     */
    public static int getTimeDifference(int current_hour, int current_minute, int current_second, int target_hour, int target_minute, int target_second) {
        int timeDifference = ((target_hour - current_hour) * 60 + (target_minute - current_minute)) * 60 + target_second - current_second;

        if (timeDifference < 0) { // e.g. Hour is 00:00 and currentHour is 23:00 -> change diff from -23:00 to +01:00
            timeDifference += 86400; // 60*60*24 = one day in seconds
        }
        return timeDifference;
    }

    public static boolean inTimespan(int start_hour, int start_minute, int end_hour, int end_minute, int current_hour, int current_minute) {
        if (start_hour < end_hour) {

            if (current_hour == start_hour) {
                return current_minute >= start_minute;
            }

            if (current_hour == end_hour) {
                return current_minute < end_minute;
            }

            return current_hour > start_hour && current_hour < end_hour;
        } else if (start_hour > end_hour) {
            if (current_hour == start_hour) {
                return current_minute >= start_minute;
            }

            if (current_hour == end_hour) {
                return current_minute < end_minute;
            }

            return current_hour > start_hour || current_hour < end_hour;
        } else {
            if (start_minute == end_minute) {
                return false;
            }
            if (current_hour == start_hour) {
                if (start_minute < end_minute) {
                    return current_minute >= start_minute && current_minute < end_minute;
                } else if (start_minute > end_minute) {
                    return current_minute <= start_minute || current_minute > end_minute;
                } else {
                    return false;
                }
            } else {
                return true;
            }
        }
    }

}
