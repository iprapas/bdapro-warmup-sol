package de.tuberlin.dima.bdapro.solutions.gameoflife;

public class test {
    public static void main (String[] args) throws Exception {
        GameOfLifeTaskImpl solver = new GameOfLifeTaskImpl();
        System.out.println(solver.solve("warmup-gol/test.txt", 1000000000, 1000000000,5));
    }
}
