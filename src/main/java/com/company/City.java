package com.company;

import java.lang.*;
import java.util.*;

public class City {
    public int x, y;
    public char cityName;

    public City(int x, int y, int nameIndex){
        this.x = x;
        this.y = y;
        cityName = (char) (nameIndex + 65);
    }

    public double distance(City city){
        double xDis = Math.abs(this.x - city.x);
        double yDis = Math.abs(this.y - city.y);
        double distance = Math.sqrt((xDis * xDis) + (yDis * yDis));
        return distance;
    }

    public String toString(){
        //String string = cityName + " : ( " + x + ", " + y + " )";
        String string = cityName + "";
        return string;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + x;
        result = prime * result + y;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        City other = (City) obj;
        if (x != other.x)
            return false;
        if (y != other.y)
            return false;
        return true;
    }
}
