package com.company;

import java.text.DecimalFormat;
import java.util.*;

public class Route {
    double fitness, distance;
    List<City> route = new ArrayList<City>();

    public Route(List<City> route){
        this.route = route;
        this.distance = routeDistance();
        this.fitness = routeFitness();
    }

    public double routeDistance(){
        double pathDistance = 0.0;
        if (distance == 0){
            pathDistance = 0;
        }
        for (int i = 0; i < route.size(); i++){
            City fromCity = route.get(i);
            City toCity = null;
            if (i + 1 < route.size()){
                toCity = route.get(i+1);
            }
            else {
                toCity = route.get(0);
            }
            pathDistance += fromCity.distance(toCity);
        }
        distance = pathDistance;
        return distance;
    }

    public double routeFitness(){
        if (fitness == 0) {
            fitness = 1000 / this.routeDistance();
        }
        return fitness;
    }

    public String toString(){
        StringBuilder string = new StringBuilder();
        DecimalFormat df = new DecimalFormat("#.###");
        for(City city : this.route) {
            string.append(city.cityName).append(", ");
        }
        string.append("\b\b : ").append(df.format(fitness));
        return string.toString();
    }
}
