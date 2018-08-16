package processors;

import config.Config;
import config.RateLimitConfig;
import enums.RequestType;
import objects.ChartJSData;
import org.springframework.beans.factory.annotation.Autowired;
import repositories.OrganizationRepository;
import repositories.RequestRepository;
import resources.rateLimit_Resources.RateLimit;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public abstract class ResponseProcessor {

    @Autowired
    RequestRepository requestRepository;

    @Autowired
    OrganizationRepository organizationRepository;

    private Date dateToStartCrawling = new Date(System.currentTimeMillis() - Config.PAST_DAYS_TO_CRAWL_IN_MS);

    public void test(){
        System.out.println(organizationRepository);
        System.out.println(requestRepository);
    }

    public void updateRateLimit(RateLimit rateLimit, RequestType requestType) {
        RateLimitConfig.setRemainingRateLimit(rateLimit.getRemaining());
        RateLimitConfig.setResetRateLimitAt(rateLimit.getResetAt());
        RateLimitConfig.addPreviousRequestCostAndRequestType(rateLimit.getCost(), requestType);

        System.out.println("Rate Limit remaining: " + RateLimitConfig.getRemainingRateLimit());
        System.out.println("Rate Limit Cost: " + RateLimitConfig.getPreviousRequestCostAndRequestType());
    }

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
        Collections.sort(arrayOfCalendars, Comparator.naturalOrder());
    }

    private ChartJSData processEmptyChartJSData(ArrayList<Calendar> arrayOfDates) {
        ArrayList<String> chartJSLabels = new ArrayList<>();
        ArrayList<Integer> chartJSDataset = new ArrayList<>();

        for (int x = 0; x != Config.PAST_DAYS_AMOUNT_TO_CRAWL + 1; x++) {
            chartJSLabels.add(this.getFormattedDate(new Date(dateToStartCrawling.getTime() + Config.DAY_IN_MS * x)));
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
            for (long x = (((currentCalendar.getTimeInMillis() - selectedCalendar.getTimeInMillis()) / Config.DAY_IN_MS)); x >= 0; x--) {
                chartJSLabels.add(this.getFormattedDate(new Date(currentCalendar.getTimeInMillis() - Config.DAY_IN_MS * x)));
                chartJSDataset.add(0);
            }
        }
    }

    private void processStartDateChartJSData(ArrayList<String> chartJSLabels, ArrayList<Integer> chartJSDataset, ArrayList<Calendar> arrayOfCalendars, Calendar selectedCalendar, DateFormat formatter) {
        Date selectedDateFormatted = null;
        Date oneWeekAgoFormatted = null;

        try {
            selectedDateFormatted = formatter.parse(formatter.format(selectedCalendar.getTime()));
            oneWeekAgoFormatted = formatter.parse(formatter.format(dateToStartCrawling));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (oneWeekAgoFormatted.getTime() < selectedDateFormatted.getTime() && arrayOfCalendars.indexOf(selectedCalendar) == 0) {
            for (int x = 0; x < (((selectedDateFormatted.getTime() - oneWeekAgoFormatted.getTime()) / Config.DAY_IN_MS)); x++) {
                chartJSLabels.add(this.getFormattedDate(new Date(oneWeekAgoFormatted.getTime() + Config.DAY_IN_MS * x)));
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

            for (int x = 1; x < (((followingDateInArrayFormatted.getTime() - selectedDateFormatted.getTime()) / Config.DAY_IN_MS)); x++) {
                chartJSLabels.add(this.getFormattedDate(new Date(selectedDateFormatted.getTime() + Config.DAY_IN_MS * x)));
                chartJSDataset.add(0);
            }
        }
    }

}
