******* MAXIMAL LENGTH PALINDROMES *******


Think about multiple ways for solving the task, and compare their performance characteristics:

with two Flink job executions
with only one Flink job execution, using withBroadcastSet
with only one Flink job execution, using cross (this one is a bit hacky)
The basic operations in Spark's API are very similar to Flink's API. However, there is RDD.cache [1] in Spark, which is not present in Flink. Think about how would that help here.

******* CONWAY'S GAME OF LIFE *******

(1) Think about how to return the number of alive cells not only at the end of the simulation, but also print the same after each step of the simulation.

System.out.println(newAlive.count()) doesn't work Throws Exception in thread "main" org.apache.flink.api.common.InvalidProgramException: A data set that is part of an iteration was used as a sink or action. Did you forget to close the iteration?
Unrolling loop is not efficient. If your intermediate results are not that big, one thing you can try is appending your intermediate results in the partial solution and then output everything in the end of the iteration. [1]
[1] https:stackoverflow.com/questions/37224140/possibility-of-saving-partial-outputs-from-bulk-iteration-in-flink-dataset


(2) You should optimize your program for performance, including the case when the program runs on a cluster (reading input from HDFS in this case). Hint: pay attention to partitioning.

.rebalance()

(3) Make sure that you write your program in such a way, that the memory and running time requirements only depend on (roughly) the number of alive cells, and not on the size of the board (that is, your program will work even with a billion rows and billion columns, provided that the number of alive cells is small (say, 10000)).

Done. Work with neighbors of alive cells. 