package com.company;

import com.rits.cloning.Cloner;
import tech.tablesaw.api.*;
import java.util.*;
import java.lang.*;
import java.text.*;

public class GeneticAlgorithm {
   public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
      List<Map.Entry<K, V>> list = new ArrayList<>(map.entrySet());
      list.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

      Map<K, V> result = new LinkedHashMap<>();
      for (Map.Entry<K, V> entry : list) {
         result.put(entry.getKey(), entry.getValue());
      }
      return result;
   }

   public static List<City> generateCityList (int listSize) {
      City[] cityList = new City[listSize];
      int x, y;
      for(int i = 0; i < listSize; i++) {
         x =(int)(Math.random() * 200);
         y =(int)(Math.random() * 200);
         cityList[i] = new City(x, y, i);
      }
      return Arrays.asList(cityList);
   }

   public static Route createRoute (List<City> cityList){
      Cloner cloner=new Cloner();
      List<City> newRoute = cloner.deepClone(cityList);
      Collections.shuffle(newRoute);
      return new Route(newRoute);
   }

   public static List<Route> initialPopulation (int popSize, List<City> cityList){
      List<Route> population = new ArrayList<>();
      for (int i = 0; i < popSize; i++){
         population.add(createRoute(cityList));
      }
      return population;
   }

   public static Map<Integer, Double> rankRoutes (List<Route> population) {
      HashMap <Integer, Double> fitnessResults = new LinkedHashMap<> () ;
      for ( int i = 0 ; i < population.size(); i++){
         fitnessResults.put(i, population.get(i).routeFitness()) ;
      }
      return sortByValue(fitnessResults);
   }

   public static List<Integer> selection (Map<Integer, Double> popRanked, int eliteSize){
      List<Integer> selectionResults = new ArrayList<>();
      DoubleColumn index = DoubleColumn.create("Index");
      DoubleColumn fitness = DoubleColumn.create("Route");
      for (Map.Entry<Integer,Double> entry : popRanked.entrySet()){
         index.append(entry.getKey());
         fitness.append(entry.getValue());
      }
      DoubleColumn cumSum = fitness.cumSum();
      DoubleColumn cumPercent = cumSum.multiply(100 / fitness.sum());
      for(int i = 0; i <= eliteSize; i ++) {
         selectionResults.add(index.get(i).intValue());
      }
      for(int i = 0; i <= popRanked.size() - eliteSize; i++){
         int pick = (int) (100 * Math.random());
         for(int j = 0; j < popRanked.size(); j++){
            if(pick <= cumPercent.get(j)){
                selectionResults.add(index.get(i).intValue());
               break;
            }
         }
      }
      return selectionResults;
   }

   public static List<Route> matingPool (List<Route> population, List<Integer> selectionResults) {
       List<Route> pool = new ArrayList<>();
       for (int index : selectionResults) {
          pool.add(population.get(index));
       }
        return pool;
    }

   public static Route breed(Route parent1, Route parent2) {
      Set<City> child = new LinkedHashSet<>();

      int geneA =(int)(Math.random() * parent1.route.size());
      int geneB =(int)(Math.random() * parent1.route.size());

      int startGene = Math.min(geneA, geneB);
      int endGene = Math.max(geneA, geneB);

      for(int i = startGene; i < endGene; i++) {
         child.add(parent1.route.get(i));
      }
//      System.out.println("Child  : " + child);child
      List<City> route = parent2.route;
      for (int i = 0; i < route.size(); i++) {
         City city = route.get(i);
         if (!child.contains(city)) {
            child.add(city);
         }
      }
//      System.out.println("Child  : " + child);

//      System.out.println("Child 2 : " +childP2);
//
//      child.addAll(childP2);
//      System.out.println("Added : " + child);
      return new Route(new ArrayList<City>(child));
   }

   public static List<Route> breedPopulation(List<Route> matingpool, int eliteSize) {
      List<Route> children = new ArrayList<>();
      int length = matingpool.size() - eliteSize;
      Cloner cloner=new Cloner();
      List<Route> pool=cloner.deepClone(matingpool);
      Collections.shuffle(pool);

      for(int i = 0; i < eliteSize; i++) {
         children.add(matingpool.get(i));
      }
      for(int i = 0; i< length; i++) {
         Route child = breed(pool.get(i), pool.get(matingpool.size() - i - 1));
         children.add(child);
      }
      return children;
   }

   public static Route mutate(Route individual, double mutationRate) {
      for(int swapped = 0; swapped < individual.route.size(); swapped++) {
         if(Math.random() < mutationRate){
            int swapWith =(int)(Math.random() * individual.route.size());
            City city1 = individual.route.get(swapped);
            City city2 = individual.route.get(swapWith);
            individual.route.set(swapped,city2);
            individual.route.set(swapWith,city1);
         }
      }
      return individual;
   }

   public static List<Route> mutatePopulation(List<Route> population, double mutationRate) {
      List<Route> mutatedPop = new ArrayList<>();
      for (Route route : population) {
         Route mutatedInd = mutate(route, mutationRate);
         mutatedPop.add(mutatedInd);
      }
      return mutatedPop;
   }

   public static List<Route> nextGeneration(List<Route> currentGen, int eliteSize, double mutationRate) {
      Map<Integer, Double> popRanked = rankRoutes(currentGen);
      List<Integer> selectionResults = selection(popRanked, eliteSize);
      List<Route> matingpool = matingPool(currentGen, selectionResults);
      List<Route> children = breedPopulation(matingpool, eliteSize);
      return mutatePopulation(children, mutationRate);
   }

   public static Route geneticAlgorithm(int popSize, int eliteSize, double mutationRate, int generations) {
      List<City> population = generateCityList(popSize);
      List<Route> pop = initialPopulation(popSize, population);
      DecimalFormat df = new DecimalFormat("#.##");
      Double initialDist = rankRoutes(pop).values().iterator().next();
      System.out.println("Initial distance: " + df.format(1000 / initialDist));

      for(int i = 0; i<= generations; i++) {
         pop = nextGeneration(pop, eliteSize, mutationRate);
         Double tempDist = rankRoutes(pop).values().iterator().next();
         System.out.println("Gen " + i + " : " + df.format(1000 / tempDist));
      }

      Double finalDist = rankRoutes(pop).values().iterator().next();
      System.out.println("Final distance: "+ df.format(1000 / finalDist) );
      int bestRouteIndex = rankRoutes(pop).keySet().iterator().next();
      Route bestRoute = pop.get(bestRouteIndex);
      return bestRoute;
   }

   public static void main(String[] args) {
//      List<City> testList = generateCityList(10);
////      System.out.println(testList);
//      int eliteSize = 5;
//      List<Route> currentGen = initialPopulation(10, testList);
//      System.out.print(pop);
//      Map<Integer, Double> popRanked = rankRoutes(currentGen);
//      List<Integer> selectionResults = selection(popRanked, eliteSize);
//      List<Route> matingpool = matingPool(currentGen, selectionResults);
//      System.out.println(matingpool.get(0));
//      System.out.println(matingpool.get(1));
//      Route children = breed(matingpool.get(0), matingpool.get(1));
//      System.out.println(children);

      Route best = geneticAlgorithm(15, 5, 0.01, 50);
   }
}
