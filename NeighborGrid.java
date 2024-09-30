package Java.RenderingTests.PictureTest;

import java.awt.Color;
import java.util.ArrayList;

public class NeighborGrid {
    ArrayList<Point>[] grid;
    int sizeX;
    int sizeY;
    int cellSize;
    double overflow;

    /**
     * Constructor for NeighborGrid
     * @param sizeX The total width of the grid
     * @param sizeY The total height of the grid
     * @param cellSize The size of each cell in the grid
     * @param overflow % of the cell size that a point can overflow into another cell
     */
    @SuppressWarnings("unchecked")
    public NeighborGrid(int sizeX, int sizeY, int cellSize, double overflow){
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.cellSize = cellSize;
        grid = new ArrayList[(int) Math.ceil(sizeX / cellSize) * (int) Math.ceil(sizeY / cellSize)];

        for(int i = 0; i < grid.length; i++){
            grid[i] = new ArrayList<Point>();
        }

        this.overflow = overflow;
    }

    public void addPoint(Point point){
        int index = (int) Math.floor(point.x / cellSize) + (int) Math.floor(point.y / cellSize) * (int) Math.ceil(sizeX / cellSize);
        if(grid[index] == null){
            grid[index] = new ArrayList<Point>();
        }
        grid[index].add(point);
    }

    public void movePoint(int index, int x, int y){
        int newGridIndex = (int) Math.floor(x / cellSize) + (int) Math.floor(y / cellSize) * (int) Math.ceil(sizeX / cellSize);
        newGridIndex = Math.min(grid.length-1, Math.max(0, newGridIndex));

        x = Math.min(sizeX, Math.max(0, x));
        y = Math.min(sizeY, Math.max(0, y));
        
        for(int i = 0; i < grid.length; i++){
            if(grid[i] != null){
                for(int j = 0; j < grid[i].size(); j++){
                    if(grid[i].get(j).index == index){
                        grid[i].get(j).moveTo(x, y);
                        if(newGridIndex != i){
                            grid[newGridIndex].add(grid[i].get(j));
                            grid[i].remove(j);
                        }
                        return;
                    }
                }
            }
        }
    }

    public Point getNearestPoint(int x, int y){
        int singleIndex = (int) Math.floor(x / cellSize) + (int) Math.floor(y / cellSize) * (int) Math.ceil(sizeX / cellSize);
        ArrayList<Integer> overflowIndices = new ArrayList<Integer>();

        int relativeX = x % cellSize;
        int relativeY = y % cellSize;

        overflowIndices.add(singleIndex);

        if(relativeX < cellSize * overflow){
            if(singleIndex % (int) Math.ceil(sizeX / cellSize) != 0){
                overflowIndices.add(singleIndex - 1);
            }
        }

        if(relativeX > cellSize * (1 - overflow)){
            if(singleIndex % (int) Math.ceil(sizeX / cellSize) != (int) Math.ceil(sizeX / cellSize) - 1){
                overflowIndices.add(singleIndex + 1);
            }
        }

        if(relativeY < cellSize * overflow){
            if(singleIndex >= (int) Math.ceil(sizeX / cellSize)){
                overflowIndices.add(singleIndex - (int) Math.ceil(sizeX / cellSize));
            }
        }

        if(relativeY > cellSize * (1 - overflow)){
            if(singleIndex < grid.length - (int) Math.ceil(sizeX / cellSize)){
                overflowIndices.add(singleIndex + (int) Math.ceil(sizeX / cellSize));
            }
        }
        

        Point nearest = null;
        double nearestDist = Double.MAX_VALUE;
        for(int index : overflowIndices){
            if(grid[index] != null){
                for(int j = 0; j < grid[index].size(); j++){
                    double dist = Math.sqrt(Math.pow(grid[index].get(j).x - x, 2) + Math.pow(grid[index].get(j).y - y, 2));
                    if(dist < nearestDist){
                        nearest = grid[index].get(j);
                        nearestDist = dist;
                    }
                }
            }
        }
        return (nearest == null) ? new Point(x, y, Color.BLACK) : nearest;
    }

    public void printGrid(){
        for(int i = 0; i < grid.length; i++){
            if(grid[i] != null){
                if(grid[i].size() == 0){
                    continue;
                }
                System.out.println("Grid index " + i + ":");
                for(int j = 0; j < grid[i].size(); j++){
                    System.out.println("    Point " + grid[i].get(j).index + ": " + grid[i].get(j).x + ", " + grid[i].get(j).y);
                }
            }
        }
    }
}
