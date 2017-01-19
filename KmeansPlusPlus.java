package com.mycompany.kmeans;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;
import static java.lang.Math.round;
import static java.lang.Math.abs;
import static java.lang.Math.pow;

public class KmeansPlusPlus {
    private static final int X = 0;
    private static final int Y = 1;
    private ArrayList<Point> points = null;
    private double[][] centroids = null;
    private final int k;
    private static final Random RANDOM = new Random(System.currentTimeMillis());
    
    public KmeansPlusPlus(ArrayList<Point> points, double[][] centroids, int k) {
        this.points = points;
        this.centroids = centroids;
        this.k = k;
    }
    
    public double min(double[] values) {
         double min = values[0];
         for(double aValue : values) {
             if (aValue < min)
                 min = aValue;
         }
         return min;
     }
    
    public void run() {
        int count;
        int kTemp = 1;
        int number;
        double sumDistance;
        double randomSum;
        double[] distance;
        double[] distanceTemp;
        
        number = (int)round(RANDOM.nextDouble()*(points.size()-1));        
        centroids[0][X] = points.get(number).getX();
        centroids[0][Y] = points.get(number).getY();
        for(int i = 0; kTemp < k; i++) {
            distance = new double[points.size() * (i+1)];
            distanceTemp = new double[kTemp];
            sumDistance = 0;
            for (int j = 0; j < points.size(); j++) {               
                for(int m = 0; m < kTemp; m++) {                   
                    distanceTemp[m] =
                    pow(abs(points.get(j).getX() - centroids[m][X]), 2) +
                    pow(abs(points.get(j).getY() - centroids[m][Y]), 2);              
                } 
                distance[j] = min(distanceTemp);
                sumDistance += distance[j];
            }           
            randomSum = RANDOM.nextDouble()*sumDistance;
            for(count = 0, sumDistance = 0; sumDistance <  randomSum; count++) {               
                sumDistance += distance[count];
            }
            centroids[kTemp][X] = points.get(count - 1).getX();
            centroids[kTemp][Y] = points.get(count - 1).getY();
            kTemp++;
        }
    }
    
}
