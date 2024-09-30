package Java.RenderingTests.PictureTest;

import java.awt.Color;

public class Point {
    public int x;
    public int y;
    public Color color;
    public int index;

    public Point(int x, int y){
        this.x = x;
        this.y = y;
        this.color = Color.BLACK;
    }

    public Point(int x, int y, Color color){
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public Point(int x, int y, int index){
        this.x = x;
        this.y = y;
        this.color = Color.BLACK;
        this.index = index;
    }

    public Point(int x, int y, Color color, int index){
        this.x = x;
        this.y = y;
        this.color = color;
        this.index = index;
    }

    public void moveTo(int x, int y){
        this.x = x;
        this.y = y;
    }
}
