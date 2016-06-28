package gsbap.analyzer;

import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;


public class LoaderAnalyzer {

    public static Photo load(double lon, double lat, String date){
        String charset = "UTF-8";
        double cloudscore = 1.0;
        String API_KEY = "MaqPDdbGPOTsIU3nSjF9ayrAQQ5zEYoY4ApiNSwz";
        try {
            URLConnection connection = new URL(
                    "https://api.nasa.gov/planetary/earth/imagery" + "?"
                    + "lon=" + lon + "&lat=" + lat
                    + "&date=" + date
                    + "&cloud_score=True&api_key=" + API_KEY).openConnection();
            connection.setRequestProperty("Accept-Charset", charset);
            InputStream response = connection.getInputStream();

            Scanner scanner = new Scanner(response);
            String responseBody = scanner.useDelimiter("\\A").next();

            System.out.println(responseBody);

            JSONObject json =  new JSONObject(responseBody);
            cloudscore = json.getDouble("cloud_score");

            Photo pic = new Photo(new URL(json.getString("url")) , cloudscore, json.getString("date").split("T")[0]);
            //ImageIO.write(pic.img, "jpg", new File("./pictures/"+file));
            return pic;
        } catch (Exception e) {
            return new Photo();
        }
    }

    public static String bestAsset(double lon, double lat){
        System.out.println(" best asset");
        String charset = "UTF-8";
        String API_KEY = "MaqPDdbGPOTsIU3nSjF9ayrAQQ5zEYoY4ApiNSwz";
        String date = "2015-06-01";
        double score = 1.0; double minscore = 1.0;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, 2013);
        c.set(Calendar.MONTH, 8);
        c.set(Calendar.DAY_OF_MONTH, 1);
        for(int i = 0; i< 72 ; i++){
            c.add(Calendar.DATE, 16);
            if (lat > 30){
                if( c.get(Calendar.MONTH)<=3 || c.get(Calendar.MONTH)>=10 ) continue;
            } else if (lat < -30){
                if( c.get(Calendar.MONTH)>=5 && c.get(Calendar.MONTH)<=8 ) continue;
            }
            try {
                URLConnection connection = new URL(
                        "https://api.nasa.gov/planetary/earth/imagery" + "?"
                                + "lon=" + lon + "&lat=" + lat
                                + "&date=" + sdf.format(c.getTime())
                                + "&cloud_score=True&api_key=" + API_KEY).openConnection();
                connection.setRequestProperty("Accept-Charset", charset);
                InputStream response = connection.getInputStream();


                Scanner scanner = new Scanner(response);
                String responseBody = scanner.useDelimiter("\\A").next();

                JSONObject json =  new JSONObject(responseBody);
                score = json.getDouble("cloud_score");
                if(score < minscore) {
                    minscore = score;
                    date = sdf.format(c.getTime());
                }
            } catch (Exception e) {
                continue;
            }

        }
        return date;
    }

    public static void save(BufferedImage image, String file){
        try {
            ImageIO.write(image, "jpg", new File("./pictures/"+file));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public static void find(double lon, double lat){

        Grid grid;
        /*
        krakow 19.93 50.06
        brasilia -15.793889, -47.882778
        new york 40.71,  -74.00
         */
        grid = loadImage( -47.88, -15.79 , 1);
        analyse(grid,6);
        try {
            ImageIO.write(grid.img, "jpg", new File("./src/main/resources/templates/"+"ccc1.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Grid loadImage(double lon,double lat, double rad){
        int rad2 = (int)Math.ceil( (rad/111)/0.025 );
        System.out.println(" loading ..........." + rad + " " + rad2);
        double zeroLon = lon - rad2*0.025;
        double zeroLat = lat + rad2*0.025;
        Grid grid = new Grid(rad2*2 + 1);
        String date = bestAsset(lon, lat);
        for(int i = 0; i<rad2*2 + 1; i++){
            for(int j = 0; j<rad2*2 + 1; j++) {
                grid.draw(load(zeroLon + i * 0.025, zeroLat - j * 0.025, date), i, j);
            }
        }
        return grid;
    }

    public static double analyse(Grid grid, int rad){
        System.out.println(" in -----------------------------------------------------" + rad);
        int w = grid.img.getWidth(); int h = grid.img.getHeight();
        int centerX = grid.img.getWidth()/2;
        int centerY = grid.img.getHeight()/2;
        grid.img.getGraphics().setColor(Color.RED);
        grid.img.getGraphics().fillRect(centerX - 10, centerY - 10, 21 , 21);
        grid.img.getGraphics().setColor(Color.WHITE);
        grid.img.getGraphics().drawLine(centerX - 100, centerY, centerX + 100, centerY);
        grid.img.getGraphics().drawLine(centerX, centerY - 100, centerX, centerY + 100);
        boolean array[][] = new boolean[w][h];
        boolean visited[][] = new boolean[w][h];
        for(int x = 0; x < w; x++){
            for(int y = 0; y < h; y++){
                int clr=  grid.img.getRGB(x,y);
                int  red   = (clr & 0x00ff0000) >> 16;
                int  green = (clr & 0x0000ff00) >> 8;
                int  blue  =  clr & 0x000000ff;
                grid.img.getGraphics().setColor(Color.RED);
                //System.out.print(" " + blue);
                if( (red<60 && green < 60) ||
                        (blue > 90 && red<100 && green < 100 ) ) {
                    //grid.img.getGraphics().drawLine(x,y,x,y);
                    array[x][y] = true;
                }

            }
        }
        int X=centerX, Y=centerY; int INF = 1000000000;
        double mindist = INF; Math.min(w/2, h/2);
        for(int x = rad; x < w-rad; x++){
            for(int y = rad; y < h-rad; y++){
                if(array[x][y] && visited[x][y] == false){
                    boolean ch = true;
                    for(int xx = -rad; xx<rad; xx++)
                        for(int yy = -rad; yy<rad; yy++)
                            if(array[x+xx][y+yy]== false) ch = false;
                    if(ch){

                        double dist = Math.sqrt( (x-centerX)*(x-centerX) +(y-centerY)*(y-centerY) );
                        //System.out.println(x + "," + y + "   " + X + "," + Y + " " + mindist + " " + dist);
                        if(dist < mindist){
                            mindist = dist; X = x; Y = y;
                        }
                    }
                }
            }
        }
        System.out.println("-----------> + " + X + " " + Y + " " + mindist);
        grid.img.getGraphics().drawLine(centerX, centerY , X, Y);
        grid.img.getGraphics().drawOval (centerX - (int)mindist, centerY - (int)mindist ,
                (int)(2*mindist), (int)(2*mindist));
        if(mindist < INF) return mindist;
        else return -1.0;
    }


    public static double analyseGreenSpace(Grid grid){
        int w = grid.img.getWidth(); int h = grid.img.getHeight();
        double score = 0;
        for(int x = 0; x < w; x++){
            for(int y = 0; y < h; y++){
                int clr=  grid.img.getRGB(x,y);
                int  red   = (clr & 0x00ff0000) >> 16;
                int  green = (clr & 0x0000ff00) >> 8;
                score = score + green/255.0/3.0;
            }
        }
        return score;
    }
}
