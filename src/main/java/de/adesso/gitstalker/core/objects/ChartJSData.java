package de.adesso.gitstalker.core.objects;

import lombok.Data;

import java.util.ArrayList;

@Data
public class ChartJSData {

    private ArrayList<String> chartJSLabels;
    private ArrayList<Integer> chartJSDataset;

    public ChartJSData(ArrayList<String> chartJSLabels, ArrayList<Integer> chartJSDataset){
        this.setChartJSDataset(chartJSDataset);
        this.setChartJSLabels(chartJSLabels);
    }
}
