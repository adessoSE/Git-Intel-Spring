package processors;

import objects.ChartJSData;
import objects.ResponseWrapper;

import java.nio.channels.CancelledKeyException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public abstract class ResponseProcessor {

    private long DAY_IN_MS = 1000 * 60 * 60 * 24;
    private Date oneWeekAgo = new Date(System.currentTimeMillis() - (7 * DAY_IN_MS));

    public abstract ResponseWrapper processResponse();

    public ChartJSData generateChartJSData(ArrayList<Calendar> arrayOfDates) {
        this.sortArrayOfDatesAscendingOrder(arrayOfDates);

        if (arrayOfDates.isEmpty()) {
            return this.processEmptyChartJSData(arrayOfDates);
        } else return this.processValidChartJSData(arrayOfDates);

    }

    private String getFormattedDate(Date date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(date);
    }

    private void sortArrayOfDatesAscendingOrder(ArrayList<Calendar> arrayOfCalendars) {
//        Comparator<Date> byDate = (Date d1, Date d2) -> d1.compareTo(d2);
//        Collections.sort(arrayOfDates, byDate);
        Collections.sort(arrayOfCalendars, Comparator.naturalOrder());
    }

    private ChartJSData processEmptyChartJSData(ArrayList<Calendar> arrayOfDates) {
        ArrayList<String> chartJSLabels = new ArrayList<>();
        ArrayList<Integer> chartJSDataset = new ArrayList<>();

        for (int x = 0; x != 8; x++) {
            chartJSLabels.add(this.getFormattedDate(new Date(oneWeekAgo.getTime() + DAY_IN_MS * x)));
            chartJSDataset.add(0);
        }
        return new ChartJSData(chartJSLabels, chartJSDataset);
    }

    private ChartJSData processValidChartJSData(ArrayList<Calendar> arrayOfCalendars) {
        ArrayList<String> chartJSLabels = new ArrayList<>();
        ArrayList<Integer> chartJSDataset = new ArrayList<>();

        for (Calendar calendar : arrayOfCalendars) {
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

            this.processStartDateChartJSData(chartJSLabels, chartJSDataset, arrayOfCalendars, calendar, formatter);
            this.processChartJSData(chartJSLabels, chartJSDataset, calendar);
            this.processGapsBetweenChartJSData(chartJSLabels, chartJSDataset, arrayOfCalendars, calendar, formatter);
            this.processEndDateChartJSData(chartJSLabels, chartJSDataset, arrayOfCalendars, calendar);
        }

        return new ChartJSData(chartJSLabels, chartJSDataset);
    }

    private void processEndDateChartJSData(ArrayList<String> chartJSLabels, ArrayList<Integer> chartJSDataset, ArrayList<Calendar> arrayOfCalendars, Calendar selectedCalendar) {
        Calendar currentCalendar = Calendar.getInstance();
        currentCalendar.set(Calendar.MINUTE, 0);
        currentCalendar.set(Calendar.HOUR, 0);

        if (arrayOfCalendars.size() - 1 == arrayOfCalendars.indexOf(selectedCalendar) && currentCalendar.getTimeInMillis() > selectedCalendar.getTimeInMillis()) {
            for (long x = (((currentCalendar.getTimeInMillis() - selectedCalendar.getTimeInMillis()) / DAY_IN_MS)); x >= 0; x--) {
                chartJSLabels.add(this.getFormattedDate(new Date(currentCalendar.getTimeInMillis() - DAY_IN_MS * x)));
                chartJSDataset.add(0);
            }
        }
    }

    private void processStartDateChartJSData(ArrayList<String> chartJSLabels, ArrayList<Integer> chartJSDataset, ArrayList<Calendar> arrayOfCalendars, Calendar selectedCalendar, DateFormat formatter) {
        Date selectedDateFormatted = null;
        Date oneWeekAgoFormatted = null;

        try {
            selectedDateFormatted = formatter.parse(formatter.format(selectedCalendar.getTime()));
            oneWeekAgoFormatted = formatter.parse(formatter.format(oneWeekAgo));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (oneWeekAgoFormatted.getTime() < selectedDateFormatted.getTime() && arrayOfCalendars.indexOf(selectedCalendar) == 0) {
            for (int x = 0; x < (((selectedDateFormatted.getTime() - oneWeekAgoFormatted.getTime()) / DAY_IN_MS)); x++) {
                chartJSLabels.add(this.getFormattedDate(new Date(oneWeekAgoFormatted.getTime() + DAY_IN_MS * x)));
                chartJSDataset.add(0);
            }
        }
    }

    private void processChartJSData(ArrayList<String> chartJSLabels, ArrayList<Integer> chartJSDataset, Calendar selectedCalendar) {
        String formattedDate = this.getFormattedDate(selectedCalendar.getTime());
        if (!chartJSLabels.contains(formattedDate)) {
            chartJSLabels.add(formattedDate);
            chartJSDataset.add(1);
        } else {
            chartJSDataset.set(chartJSLabels.indexOf(formattedDate), chartJSDataset.get(chartJSLabels.indexOf(formattedDate)) + 1);
        }
    }

    private void processGapsBetweenChartJSData(ArrayList<String> chartJSLabels, ArrayList<Integer> chartJSDataset, ArrayList<Calendar> arrayOfCalendar, Calendar selectedCalendar, DateFormat formatter) {
        if (arrayOfCalendar.size() != arrayOfCalendar.indexOf(selectedCalendar) + 1) {
            Date selectedDateFormatted = null;
            Date followingDateInArrayFormatted = arrayOfCalendar.get(arrayOfCalendar.indexOf(selectedCalendar) + 1).getTime();
            try {
                selectedDateFormatted = formatter.parse(formatter.format(selectedCalendar.getTime()));
                followingDateInArrayFormatted = formatter.parse(formatter.format(followingDateInArrayFormatted));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            for (int x = 1; x < (((followingDateInArrayFormatted.getTime() - selectedDateFormatted.getTime()) / DAY_IN_MS)); x++) {
                chartJSLabels.add(this.getFormattedDate(new Date(selectedDateFormatted.getTime() + DAY_IN_MS * x)));
                chartJSDataset.add(0);
            }
        }
    }

}
