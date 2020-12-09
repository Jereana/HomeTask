package com.yeremenko.myProject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateHelper {

    public static String convertDateToString(Date date, String format){
        LocalDate localDate;

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(format);
        if (date == null) {
            localDate = LocalDate.now();
        } else {
            localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }
        return dtf.format(localDate);
    }

    public static Date stringToDate(String dateStr)  {
        Date date = null;
        if (dateStr!=null){
            try {
                date = new SimpleDateFormat("dd.MM.yyyy").parse(dateStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return date;
    }

    public static List<Date> getDatesList(int lastDaysCount, Date dateFrom, Date dateTo){
        List<Date> datesList = new ArrayList<>();
        if (lastDaysCount != 0) {
            Date todayDate = null;
            todayDate = DateHelper.stringToDate(DateHelper.convertDateToString(null, "dd.MM.yyyy"));
            datesList = DateHelper.getDatesListByLastDaysCount(lastDaysCount, todayDate);
        } else if (dateFrom!=null && dateTo != null) {
            datesList = DateHelper.getDatesListByPeriod(dateFrom, dateTo);
        }
        return datesList;
    }

    private static List<Date> getDatesListByLastDaysCount(int lastDaysCount, Date date) {

        // Формируем список дат для поиска курсов валют
        List<Date> datesList = new ArrayList<>();
        if (date == null) {
            return datesList;
        }
        LocalDate dateFromLocalDay = date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate().minusDays(lastDaysCount-1);
        Date dateToAdd = Date.from(dateFromLocalDay.atStartOfDay(ZoneId.systemDefault()).toInstant());
        for (int i = 0; i < lastDaysCount; i++) {
            datesList.add(dateToAdd);
            dateFromLocalDay = dateToAdd.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate().plusDays(1);
            dateToAdd = Date.from(dateFromLocalDay.atStartOfDay(ZoneId.systemDefault()).toInstant());
        }
        return datesList;
    }

    private static List<Date> getDatesListByPeriod(Date dateFrom, Date dateTo){
        // получить количество дней в разнице дат.
        Calendar day1 = Calendar.getInstance();
        Calendar day2 = Calendar.getInstance();
        day1.setTime(dateTo);
        day2.setTime(dateFrom);

        int daysBetween = day1.get(Calendar.DAY_OF_YEAR) - day2.get(Calendar.DAY_OF_YEAR);

        return getDatesListByLastDaysCount(daysBetween+1, dateTo);

    }

    public static Boolean checkDateIsInPeriod(Date dateFrom, Date dateTo, Date checkDate) {
        Calendar dayFrom = Calendar.getInstance();
        Calendar dayTo = Calendar.getInstance();
        Calendar dayToCheck = Calendar.getInstance();
        dayFrom.setTime(dateFrom);
        dayTo.setTime(dateTo);
        dayToCheck.setTime(checkDate);

        if ((dayFrom.before(dayToCheck) && dayTo.after(dayToCheck))
                || dayFrom.equals(dayToCheck) || dayTo.equals(dayToCheck) ){
            return true;
        }
        return false;
    }

}
