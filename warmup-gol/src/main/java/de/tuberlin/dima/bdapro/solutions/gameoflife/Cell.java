package de.tuberlin.dima.bdapro.solutions.gameoflife;

import java.util.HashSet;
import java.util.Set;

public class Cell {

    private static int n,m;

    public Cell(int x, int y, int xsize, int ysize, boolean alive) {
        n = xsize;
        m = ysize;
        this.x = warp(x, n);
        this.y = warp(y, m);
        this.alive = alive;
    }
    
    public Cell(int x, int y, boolean alive) {
        this.x = warp(x, n);
        this.y = warp(y, m);
        this.alive = alive;
    }



    private int warp(int a, int edge) {
        return (a % edge + edge) % edge;
    }
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isAlive() {
        return alive;
    }


    private int x, y;
    private boolean alive;

    @Override
    public String toString() {
        return "(" + x + ", " + y + ") :" + "alive=" + alive;
    }

    public Set<Cell> getNbors() {
        Set<Cell> nborList = new HashSet<>();
        nborList.add(new Cell(this.getX()+1, this.getY(), false));
        nborList.add(new Cell(this.getX(), this.getY()+1, false));
        nborList.add(new Cell(this.getX()-1, this.getY(), false));
        nborList.add(new Cell(this.getX(), this.getY()-1, false));
        nborList.add(new Cell(this.getX()+1, this.getY()-1, false));
        nborList.add(new Cell(this.getX()-1, this.getY()+1, false));
        nborList.add(new Cell(this.getX()-1, this.getY()-1, false));
        nborList.add(new Cell(this.getX()+1, this.getY()+1, false));
        return nborList;
    }


    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public Boolean isNbor(Cell c1) {
        int left = warp(this.getX() - 1 , n);
        int right = warp(this.getX() +  1, n);
        int up = warp(this.getY() - 1, m);
        int down = warp(this.getY() + 1, m);

        // left
        if (left == c1.getX() && this.getY() == c1.getY()) return true;
        // right
        if (right == c1.getX() && this.getY() == c1.getY()) return true;
        // up
        if (up == c1.getY() && this.getX() == c1.getX()) return true;
        // down
        if (down == c1.getY() && this.getX() == c1.getX()) return true;
        // left up
        if (left == c1.getX() && up == c1.getY()) return true;
        // left down
        if (left == c1.getX() && down == c1.getY()) return true;
        // right up
        if (right == c1.getX() && up == c1.getY()) return true;
        // right down
        if (right == c1.getX() && down == c1.getY()) return true;
        return false;
    }

}
