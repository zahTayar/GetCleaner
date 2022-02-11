package com.example.getcleaner.Compartors;

import com.example.getcleaner.objects.userCleaner;

import java.util.Comparator;

public class sortCleanerByHourCost implements Comparator<userCleaner> {
    @Override
    public int compare(userCleaner o1, userCleaner o2) {
        return Integer.parseInt(o1.getPerHour())-Integer.parseInt(o2.getPerHour());
    }
}
