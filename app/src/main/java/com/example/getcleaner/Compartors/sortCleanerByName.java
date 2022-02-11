package com.example.getcleaner.Compartors;

import com.example.getcleaner.objects.userCleaner;

import java.util.Comparator;

public class sortCleanerByName implements Comparator<userCleaner> {
    @Override
    public int compare(userCleaner o1, userCleaner o2) {
        return o1.getFirstNameStr().compareToIgnoreCase(o2.getFirstNameStr());
    }
}
