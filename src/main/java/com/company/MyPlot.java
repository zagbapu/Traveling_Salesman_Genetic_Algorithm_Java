package com.company;

import tech.tablesaw.api.Table;
import tech.tablesaw.plotly.Plot;
import tech.tablesaw.plotly.api.TimeSeriesPlot;
import tech.tablesaw.plotly.components.Figure;

import java.io.IOException;
import java.io.InputStream;

public class MyPlot {
    public String js;

    public String getJs() {
        return js;
    }

    public String createPlot() throws IOException {
        String fileName = "btsi_flow.csv";
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream file = classLoader.getResourceAsStream(fileName);
        Table btsi = Table.read().csv(file);
        Figure myTSplot = TimeSeriesPlot.create("SOME RANDOM DSV FILE KARL GAVE ME", btsi, "DateTime", "btsi_qd");
        String js = myTSplot.asJavascript("");
        System.out.println(js);
        Plot.show(myTSplot);
        return js;

    }


}