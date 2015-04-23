package co.olivemedia.olivemediasampleproject;

/**
 * Created by vivek on 4/23/2015.
 */
public class Meetingrecords {

    public static String meetingTitle,meetingLocation,meetingTime;

    public static String getMeetingTitle() {
        return meetingTitle;
    }

    public static void setMeetingTitle(String meetingTitle) {
        Meetingrecords.meetingTitle = meetingTitle;
    }

    public static String getMeetingLocation() {
        return meetingLocation;
    }

    public static void setMeetingLocation(String meetingLocation) {
        Meetingrecords.meetingLocation = meetingLocation;
    }

    public static String getMeetingTime() {
        return meetingTime;
    }

    public static void setMeetingTime(String meetingTime) {
        Meetingrecords.meetingTime = meetingTime;
    }
}
