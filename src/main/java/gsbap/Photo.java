package gsbap;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class Photo {
    public String date;
    public BufferedImage img = new BufferedImage(512,512,BufferedImage.TYPE_INT_RGB);
    public double score = 1.0;

    public Photo(){

    }

    public Photo(URL url, double socre, String date) throws IOException {
        this.img = ImageIO.read(url);;
        this.score = score;
        this.date = date;
    }

    public BufferedImage getImg() {
        return img;
    }

    public void setImg(BufferedImage img) {
        this.img = img;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
