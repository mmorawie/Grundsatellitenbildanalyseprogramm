package gsbap;

import gsbap.analyzer.*;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static gsbap.analyzer.LoaderAnalyzer.analyse;
import static gsbap.analyzer.LoaderAnalyzer.analyseGreenSpace;
import static gsbap.analyzer.LoaderAnalyzer.loadImage;

public class Main {

    private static String lon;
    private static String lat;
    private static String rad;
    static double min = 0;
    static double gs = 0;
    static String other = "";
    static boolean error = false;

    public void setRad(String rad) {this.rad = rad;}

    public String getRad() {return rad;}

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getMin(){
        if (error) return "--";
        return "" + min;
    }

    public String getGs(){
        if (error) return "--";
        return "" + gs;
    }

    public String getOther(){
        return "" + other;
    }

    //public URL getUrl(){
    //    return url;
    //}

    public static void calculate(){
        try {
            double r = 1;
            try {
                r = Double.parseDouble(rad);
            } catch (NumberFormatException e) {
                r = 1;
            }
            int rad2 = (int)Math.floor( (r/111)/0.025 );
            Grid grid = loadImage(Double.parseDouble(lon), Double.parseDouble(lat) , r);
            min = analyse(grid, rad2);
            gs = analyseGreenSpace(grid);
            other = "";
            //bfi = grid.img;
            ImageIO.write(grid.img, "jpg", new File("./pictures/"+"ccc1.jpg"));
            error = false;
        } catch (IOException e) {
            //e.printStackTrace();
            other = "Server error, sorry!"; error = true;
        } catch (NumberFormatException e) {
            //e.printStackTrace();
            other = "Wrong input, try again. \n Use following format: [00.00] \n use negative numbers to indicate southern latitudes or western longitudes"; error = true;
        }
    }

}
