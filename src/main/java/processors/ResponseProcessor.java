package processors;

import config.Config;
import objects.ChartJSData;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public abstract class ResponseProcessor {

    private long DAY_IN_MS = 1000 * 60 * 60 * 24;
    private Date dateToStartCrawling = new Date(System.currentTimeMillis() - (Config.PAST_DAYS_AMOUNT_TO_CRAWL * DAY_IN_MS));

    public ChartJSData generateChartJSData(ArrayList<Date> arrayOfDates) {
        this.sortArrayOfDatesAscendingOrder(arrayOfDates);

        if (arrayOfDates.isEmpty()) {
            return this.processEmptyChartJSData(arrayOfDates);
        } else return this.processValidChartJSData(arrayOfDates);

    }

    private String getFormattedDate(Date date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(date);
    }

    private void sortArrayOfDatesAscendingOrder(ArrayList<Date> arrayOfDates) {
        Comparator<Date> byDate = (Date d1, Date d2) -> d1.compareTo(d2);
        Collections.sort(arrayOfDates, byDate);
    }

    private ChartJSData processEmptyChartJSData(ArrayList<Date> arrayOfDates) {
        ArrayList<String> chartJSLabels = new ArrayList<>();
        ArrayList<Integer> chartJSDataset = new ArrayList<>();

        for (int x = 0; x != Config.PAST_DAYS_AMOUNT_TO_CRAWL+1; x++) {
            chartJSLabels.add(this.getFormattedDate(new Date(dateToStartCrawling.getTime() + DAY_IN_MS * x)));
            chartJSDataset.add(0);
        }
        return new ChartJSData(chartJSLabels, chartJSDataset);
    }

    private ChartJSData processValidChartJSData(ArrayList<Date> arrayOfDates) {
        ArrayList<String> chartJSLabels = new ArrayList<>();
        ArrayList<Integer> chartJSDataset = new ArrayList<>();

        for (Date date : arrayOfDates) {
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

            this.processStartDateChartJSData(chartJSLabels, chartJSDataset, arrayOfDates, date, formatter);
            this.processChartJSData(chartJSLabels, chartJSDataset, date);
            this.processGapsBetweenChartJSData(chartJSLabels, chartJSDataset, arrayOfDates, date, formatter);
            this.processEndDateChartJSData(chartJSLabels, chartJSDataset, arrayOfDates, date);
        }

        return new ChartJSData(chartJSLabels, chartJSDataset);
    }

    private void processEndDateChartJSData(ArrayList<String> chartJSLabels, ArrayList<Integer> chartJSDataset, ArrayList<Date> arrayOfDates, Date selectedDate) {
        Date currentDate = new Date(System.currentTimeMillis());
        currentDate.setMinutes(0);
        currentDate.setHours(0);

        if (arrayOfDates.size() - 1 == arrayOfDates.indexOf(selectedDate) && currentDate.getTime() > selectedDate.getTime()) {
            for (long x = (((currentDate.getTime() - selectedDate.getTime()) / DAY_IN_MS)); x >= 0; x--) {
                chartJSLabels.add(this.getFormattedDate(new Date(currentDate.getTime() - DAY_IN_MS * x)));
                chartJSDataset.add(0);
            }
        }
    }

    private void processStartDateChartJSData(ArrayList<String> chartJSLabels, ArrayList<Integer> chartJSDataset, ArrayList<Date> arrayOfDates, Date selectedDate, DateFormat formatter) {
        Date selectedDateFormatted = null;
        Date oneWeekAgoFormatted = null;

        try {
            selectedDateFormatted = formatter.parse(formatter.format(selectedDate));
            oneWeekAgoFormatted = formatter.parse(formatter.format(dateToStartCrawling));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (oneWeekAgoFormatted.getTime() < selectedDateFormatted.getTime() && arrayOfDates.indexOf(selectedDate) == 0) {
            for (int x = 0; x < (((selectedDateFormatted.getTime() - oneWeekAgoFormatted.getTime()) / DAY_IN_MS)); x++) {
                chartJSLabels.add(this.getFormattedDate(new Date(oneWeekAgoFormatted.getTime() + DAY_IN_MS * x)));
                chartJSDataset.add(0);
            }
        }
    }

    private void processChartJSData(ArrayList<String> chartJSLabels, ArrayList<Integer> chartJSDataset, Date selectedDate) {
        String formattedDate = this.getFormattedDate(selectedDate);
        if (!chartJSLabels.contains(formattedDate)) {
            chartJSLabels.add(formattedDate);
            chartJSDataset.add(1);
        } else {
            chartJSDataset.set(chartJSLabels.indexOf(formattedDate), chartJSDataset.get(chartJSLabels.indexOf(formattedDate)) + 1);
        }
    }

    private void processGapsBetweenChartJSData(ArrayList<String> chartJSLabels, ArrayList<Integer> chartJSDataset, ArrayList<Date> arrayOfDates, Date selectedDate, DateFormat formatter) {
        if (arrayOfDates.size() != arrayOfDates.indexOf(selectedDate) + 1) {
            Date selectedDateFormatted = null;
            Date followingDateInArrayFormatted = arrayOfDates.get(arrayOfDates.indexOf(selectedDate) + 1);
            try {
                selectedDateFormatted = formatter.parse(formatter.format(selectedDate));
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
