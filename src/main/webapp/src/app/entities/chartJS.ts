
export class ChartJsData {
    public labels: Array<string>;
    public data: [{ data: Array<number>, label: string }];
    public caption: string;
}

export class ChartJs {
    chartTitle: string;
    chartType: string;
    chartOptions: any;
    chartLegend: boolean;
    chartData: ChartJsData;
    chartColors: Array<any>;
}