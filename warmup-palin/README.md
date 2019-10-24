# Description
A palindrome is a word, phrase, number, or other sequence of characters which reads the same backward and forward, such as madam or kayak.

In this task, we apply this notion to sentences: a palindrome sentence has the same order of alphanumeric characters when read backwards. For example:
`was it a car or a cat I saw`
or
`23 yo banana boy 32`

In this task, you are asked to find the *biggest* (longest) palindrome sentences in the input data set, that is, the ones which contain the maximal number of alphanumeric characters.

## Input
The input is in a text file, whose path is given as an argument to your program. The given path might point to HDFS (when it starts with hdfs://). The input will contain only lower-case English letters, spaces, newlines, and numerical characters. Each line contains exactly one sentence. (A sentence can have zero size.) Every line will be shorter than 1000 characters.

*The input file might be very large. You have to use Flink in a way that your solution is scalable to multiple machines.*

## Output
Your program should return all the palindrome sentences that have the maximal length in a *Set<String>*.

Please take into consideration that the returned sentences must be exactly the same as in the input file, with all characters and spaces (excluding the \n character at the end of lines).

You can assume that the output will not contain more than 1000 elements for the inputs that we test your program with.

## Example
If the given input text is

```
abc cba 33
 abc abc cba cba 
4 qwer fg gf rewq 4
a qder fg gf redq a
abcde abcde edcba edcba 33     
```
then the output should be a `Set<String>` with the following two elements:

```
4 qwer fg gf rewq 4
a qder fg gf redq a
```

## Submission process
Please submit a jar file with sources to the evaluation server. Also make sure to not include other maven dependencies than those provided in the sample project.

Your solution class must be named `PalindromeTaskImpl`, implement the interface PalindromeTask and be located in the package de.tuberlin.dima.bdapro.solutions.palindrome. If you implement any additional utility/helper classes, also put them in this package.

You may use Java or Scala to implement the Java interface. However, the interface expects a Java set as return value. You can explicitly do the required conversion from a Scala sequence to a Java set using:

    `JavaConversions.setAsJavaSet(seq.toSet)`
Make sure to thoroughly test your program before submitting it!

## Bonus task
Think about multiple ways for solving the task, and compare their performance characteristics:

* with two Flink job executions
* with only one Flink job execution, using withBroadcastSet
* with only one Flink job execution, using cross (this one is a bit hacky)
* The basic operations in Spark's API are very similar to Flink's API. However, there is RDD.cache [1] in Spark, which is not present in Flink. Think about how would that help here.

[1] http://spark.apache.org/docs/latest/api/scala/index.html#org.apache.spark.rdd.RDD