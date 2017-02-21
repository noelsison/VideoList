package com.example.noel.videolist.data;

/**
 * Created by Noel on 2/21/2017.
 */

public class DbConstants {

    public static final class ContentType {
        public final static int VIDEO = 1;
        public final static int AUDIO_RECORD  = 2;

        public static String toString(int type) {
            switch (type) {
                case VIDEO:
                    return "Lesson: Video";
                case AUDIO_RECORD:
                    return "Activity: Audio";
                default:
                    return null;
            }
        }
    }
}
