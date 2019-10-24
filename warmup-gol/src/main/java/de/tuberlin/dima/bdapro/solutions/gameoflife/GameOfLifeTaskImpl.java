package de.tuberlin.dima.bdapro.solutions.gameoflife;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.operators.IterativeDataSet;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;

import static java.lang.Math.toIntExact;

public class GameOfLifeTaskImpl implements GameOfLifeTask {



	@Override
	public int solve(String inputFile, int argN, int argM, int numSteps) throws Exception {
		//******************************
		//*Implement your solution here*
		//******************************
        final int n = argN;
        final int m = argM;

        final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        IterativeDataSet<Cell> alive = env
                .readTextFile(inputFile)
                .map(s-> s.split(" "))
                // parse each line into an alive cell
                .map(s-> new Cell(Integer.parseInt(s[0]), Integer.parseInt(s[1]), n, m, true))
                .rebalance()
                .iterate(numSteps);
        DataSet<Cell> aliveOrDead = alive
                // for each alive cell, generate all of its neighbors
                .flatMap(new genNbors()).returns(Cell.class)
                // keep unique cells (alive cells have greater priority)
                .groupBy(new KeySelector<Cell, Tuple2<Integer, Integer>>() {
                    @Override
                    public  Tuple2<Integer, Integer> getKey(Cell c) {
                        return new Tuple2<>(c.getX(),c.getY());
                    }
                    })
                .reduce((a, b) -> new Cell(a.getX(), a.getY(), (a.isAlive() || b.isAlive())));
        DataSet<Cell> newAlive = aliveOrDead
                // (alive or dead Cells) x (alive Cells)
                .cross(alive)
                // keep only pairs of neighbors
                .filter(cellTuple -> cellTuple.f0.isNbor(cellTuple.f1))
                // if neighbor cell is alive, we want to count it 
                .map(new MapFunction<Tuple2<Cell,Cell>, Tuple2<Cell,Integer>>() {
                    @Override
                    public Tuple2<Cell,Integer> map(Tuple2<Cell,Cell> a) {
                        if (a.f1.isAlive()) {
                            return new Tuple2<>(a.f0, 1);
                        }
                        else return new Tuple2<>(a.f0,0);
                    }
                })
                // sum alive neighbor counts for each cell
                .groupBy(new KeySelector<Tuple2<Cell,Integer>, Tuple2<Integer, Integer>>() {
                    @Override
                    public  Tuple2<Integer, Integer> getKey(Tuple2<Cell,Integer> ci) {
                        return new Tuple2<>(ci.f0.getX(),ci.f0.getY());
                    }
                })
                .reduce((a,b) -> new Tuple2<>(a.f0, a.f1 + b.f1) )
                /* ### apply game of life rules for each cell ###
                   # if alive cell and has <2 or >3  #neighbors => cell dies
                   # if dead cell and has exactly 3 #neighbors  => cell becomes alive 
                */ 
                .map(new MapFunction<Tuple2<Cell,Integer>, Cell>() {
                    @Override
                    public Cell map(Tuple2<Cell,Integer> c) {
                        Cell cell = c.f0;
                        int numberNbors = c.f1;
                        if (cell.isAlive() && (numberNbors<2 || numberNbors>3)) {
                            cell.setAlive(false);
                        }
                        if (!cell.isAlive() && numberNbors==3) cell.setAlive(true);
                        return cell;
                    }
                })
                // keep only alive cells for next iteration
                .filter(Cell::isAlive);
        // System.out.println(newAlive.count());
        // doesn't work Throws Exception in thread "main" org.apache.flink.api.common.InvalidProgramException: A data set that is part of an iteration was used as a sink or action. Did you forget to close the iteration?
        // If your intermediate results are not that big, one thing you can try is appending your intermediate results in the partial solution and then output everything in the end of the iteration. [1]
        // [1] https://stackoverflow.com/questions/37224140/possibility-of-saving-partial-outputs-from-bulk-iteration-in-flink-dataset
        DataSet<Cell> res = alive.closeWith(newAlive);
        int result = toIntExact(res.count());
        return result;
	}

    public static class genNbors implements FlatMapFunction<Cell, Cell> {
        @Override
        public void flatMap(Cell cellIn, Collector<Cell> cellsOut) throws Exception {
            for (Cell c: cellIn.getNbors()) {
                cellsOut.collect(c);
            }
            cellsOut.collect(cellIn);
        }
    }
}

