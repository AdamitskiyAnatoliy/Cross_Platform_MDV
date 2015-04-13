package com.anatoliyadamitskiy.a_adamitskiy_android_usersanddata;

import java.io.Serializable;

/**
 * Created by Anatoliy on 3/31/15.
 */
public class Note implements Serializable {

    private String note;
    private String title;
    private int timeToComplete;

    public Note (String _note, String _title, int _completion) {
        note = _note;
        title = _title;
        timeToComplete = _completion;
    }

    public String getNote() {
        return note;
    }

    public String getTitle() {
        return title;
    }

    public int getTimeToComplete() {
        return timeToComplete;
    }

    @Override
    public String toString() {
        return getTitle();
    }
}
