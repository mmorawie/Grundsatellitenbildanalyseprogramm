package gsbap;


import java.awt.*;
import java.awt.image.BufferedImage;

public class Grid {

    public double score = 0.0;
    public BufferedImage img;
    public Graphics2D graph;
    public int size;

    public Grid(int size) {
        this.size = size*size;
        img = new BufferedImage(size*512,size*512, BufferedImage.TYPE_INT_RGB);
        graph = img.createGraphics();
        //graph.setPaint(Color.WHITE);
        //graph.fillRect(0, 0, size*512, size*512);
    }

    public void draw(Photo pho, int x, int y){
        graph.drawImage(pho.img, null, x * 512, y * 512);
        score = score + pho.score/size;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public BufferedImage getImg() {
        return img;
    }

    public void setImg(BufferedImage img) {
        this.img = img;
    }
}
