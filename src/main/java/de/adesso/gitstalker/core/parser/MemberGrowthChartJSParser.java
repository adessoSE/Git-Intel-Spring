package de.adesso.gitstalker.core.parser;

import de.adesso.gitstalker.core.objects.ChartJSData;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;

@Service
@NoArgsConstructor
public class MemberGrowthChartJSParser {

    public ChartJSData parseInputToChartJSData(LinkedHashMap<String, Integer> linkedHashMapMemberGrowth) {
        ArrayList<String> chartJSLabels = new ArrayList<>();
        ArrayList<Integer> chartJSDataset = new ArrayList<>();

        for (String dateAsString : linkedHashMapMemberGrowth.keySet()) {
            Date parsedDate = this.convertStringBackToDate(dateAsString);

            chartJSLabels.add(this.getFormattedDate(parsedDate));
            chartJSDataset.add(linkedHashMapMemberGrowth.get(dateAsString));
        }

        return new ChartJSData(chartJSLabels, chartJSDataset);
    }

    private Date convertStringBackToDate(String dateAsString) {
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(
                dateAsString,
                DateTimeFormatter.ofPattern("EEE MMM d HH:mm:ss zzz uuuu", Locale.US)
        );
        return Date.from(zonedDateTime.toInstant());
    }

    private String getFormattedDate(Date date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(date);
    }
}
