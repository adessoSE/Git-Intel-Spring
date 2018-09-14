package de.adesso.gitstalker.core.objects;

import java.util.ArrayList;

public class ChartJSData {

    private ArrayList<String> chartJSLabels;
    private ArrayList<Integer> chartJSDataset;

    public ChartJSData(ArrayList<String> chartJSLabels, ArrayList<Integer> chartJSDataset){
        this.chartJSDataset = chartJSDataset;
        this.chartJSLabels = chartJSLabels;
    }

    public ArrayList<String> getChartJSLabels() {
        return chartJSLabels;
    }

    public ArrayList<Integer> getChartJSDataset() {
        return chartJSDataset;
    }
}
