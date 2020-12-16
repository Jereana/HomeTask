package com.yeremenko.myProject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateHelper {

    public static List<LocalDate> getLocalDatesList(LocalDate date, int lastDaysCount, LocalDate dateFrom, LocalDate dateTo){
        List<LocalDate> datesList = new ArrayList<>();
        if (date != null) {
            datesList.add(date);
        } else if (date == null && lastDaysCount == 0 && dateFrom == null && dateTo == null) {
            datesList.add(LocalDate.now().atStartOfDay().toLocalDate());
        } else if (lastDaysCount != 0) {
            LocalDate todayDate = LocalDate.now();
            datesList = getLocalDatesListByLastDaysCount(lastDaysCount, todayDate);
        } else if (dateFrom != null && dateTo != null) {
            datesList = getLocalDatesListByPeriod(dateFrom, dateTo);
        }
        return datesList;
    }

    private static List<LocalDate> getLocalDatesListByPeriod(LocalDate dateFrom, LocalDate dateTo){
        Calendar day1 = Calendar.getInstance();
        Calendar day2 = Calendar.getInstance();
        day1.setTime(convertToDateViaSqlDate(dateTo));
        day2.setTime(convertToDateViaSqlDate(dateFrom));
        int daysBetween = day1.get(Calendar.DAY_OF_YEAR) - day2.get(Calendar.DAY_OF_YEAR);

        return getLocalDatesListByLastDaysCount(daysBetween+1, dateTo);
    }

    private static List<LocalDate> getLocalDatesListByLastDaysCount(int lastDaysCount, LocalDate date) {
        List<LocalDate> datesList = new ArrayList<>();
        LocalDate dateToAdd = date.minusDays(lastDaysCount-1L);
        for (int i = 0; i < lastDaysCount; i++) {
            datesList.add(dateToAdd);
            dateToAdd = dateToAdd.plusDays(1L);
        }
        return datesList;
    }

    public static Date convertToDateViaSqlDate(LocalDate dateToConvert) {
        return java.sql.Date.valueOf(dateToConvert);
    }

}

