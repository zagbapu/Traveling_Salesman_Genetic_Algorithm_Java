package com.company;

import tech.tablesaw.api.DoubleColumn;
import tech.tablesaw.plotly.Plot;
import tech.tablesaw.plotly.components.Axis;
import tech.tablesaw.plotly.components.Figure;
import tech.tablesaw.plotly.components.Font;
import tech.tablesaw.plotly.components.Layout;
import tech.tablesaw.plotly.traces.ScatterTrace;
import tech.tablesaw.plotly.traces.Trace;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class PlotPractice {

    public static void main(String[] args) throws IOException {

//        double[] numbers = {1, 2, 3, 4};
//        DoubleColumn colX = DoubleColumn.create("colX", numbers);
//        double[] numbers2 = {10, 20, 30, 40};
//        DoubleColumn colY = DoubleColumn.create("colY", numbers2);
//        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
//        LocalDateTime now = LocalDateTime.now();
//        String date = now.toString();
//        Font f = Font.builder()
//                .family(Font.Family.ARIAL)
//                .size(24)
//                .color("green")
//                .build();
//        Axis.Spikes spikes = Axis.Spikes.builder()
//                .dash("solid")
//                .color("yellow")
//                .build();
//// put the spikes in the xAxis so we get vertical spikes
//        Axis xAxis = Axis.builder()
//                .spikes(spikes)
//                .build();
//// put the xAxis in the builder
//        Layout layout = Layout.builder()
//                .title(date)
//                .titleFont(f)
//                .xAxis(xAxis)
//                .hoverMode(Layout.HoverMode.CLOSEST)
//                .build();
//// define your trace
//        Trace trace = ScatterTrace.builder(colX, colY).build();
//// put the builder in your figure
//        Figure plot = new Figure(layout, trace);
//        Plot.show(plot);
        double[] x = {1, 2, 3, 4, 5, 6};
        double[] y = {0, 1, 6, 14, 25, 39};
        String[] labels = {"a", "b", "c", "d", "e", "f"};

        ScatterTrace trace = ScatterTrace.builder(x, y)
                .text(labels)
                .build();

        Plot.show(new Figure(trace));
    }

}