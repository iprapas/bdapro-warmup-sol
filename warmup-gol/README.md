# Description
Program a fixed number of iterations of Conway's Game of Life in Flink. (See the rules of the game in the Wikipedia page.) You are given an initial state of the game with periodic board (wraps around the edges) of size N x M. Run the simulation for a given number of steps and write the number of cells that are alive at the end of the simulation.

## Input
The arguments `(argN, argM)` of your program will specify the number of rows and columns of the board, respectively (at least 3, and at most a billion). The board wraps around at its edges (for example, a cell that is at the rightmost edge of the board has some neighbors that are at the leftmost edge of the board). The argument (inputFile) of your program will be a path to a text file (the path might start with hdfs://), which contains the initial state of the board: each line contains two numbers separated by a space, a and b, which specify that the cell in the ath row and bth column is alive (the cells are indexed from 0). All other cells are dead at the beginning of the simulation. a and b will be non-negative integers smaller than 1 billion. The fourth argument (numSteps) of your program will specify the number of steps to simulate (at least 1, and at most 100).

The input file might be very large. You have to use Flink in a way that your solution is scalable to multiple machines.

## Output
The number of alive cells at the end of the simulation should be returned from your method as an Integer.

## Example
Input Arguments

` solve(inputFile, 100, 100, 5)`
Input file
```
1 2
2 2
3 2
7 7
7 8
8 7
8 8
9 9
9 10
10 9
10 10
```
Output
```
9
```
Explanation. The initial board contains a "Blinker" and a "Beacon" (see the Wikipedia page for pattern nicknames). Therefore, it should have 11 cells alive after an even number of steps, and 9 cells alive after an odd number of steps.

## Resources

Numberphile video about Conway's Game of Life
Wikipedia page
An online simulation
Submission Process
Please submit a jar file with sources to the evaluation server. Also make sure to not include other maven dependencies than those provided in the sample project.

Your solution class must be named GameOfLifeTaskImpl, implement the interface GameOfLifeTask and be located in the package de.tuberlin.dima.bdapro.solutions.gameoflife. If you implement any additional utility/helper classes, also put them in this package.

You may use Java or Scala to implement the Java interface.

Make sure to thoroughly test your program before submitting it!

##  Bonus tasks
1. Think about how to return the number of alive cells not only at the end of the simulation, but also print the same after each step of the simulation.

`System.out.println(newAlive.count())` doesn't work. Throws `Exception in thread "main" org.apache.flink.api.common.InvalidProgramException: A data set that is part of an iteration was used as a sink or action. Did you forget to close the iteration?`
Unrolling loop is not efficient. If your intermediate results are not that big, one thing you can try is appending your intermediate results in the partial solution and then output everything in the end of the iteration. [1]

2. You should optimize your program for performance, including the case when the program runs on a cluster (reading input from HDFS in this case). Hint: pay attention to partitioning.

`.rebalance()`

3. Make sure that you write your program in such a way, that the memory and running time requirements only depend on (roughly) the number of alive cells, and not on the size of the board (that is, your program will work even with a billion rows and billion columns, provided that the number of alive cells is small (say, 10000)).

Work with neighbors of alive cells. 

[1] https://stackoverflow.com/questions/37224140/possibility-of-saving-partial-outputs-from-bulk-iteration-in-flink-dataset

