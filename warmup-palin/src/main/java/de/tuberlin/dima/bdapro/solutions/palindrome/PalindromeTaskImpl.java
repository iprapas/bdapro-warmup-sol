package de.tuberlin.dima.bdapro.solutions.palindrome;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.tuple.Tuple2;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class PalindromeTaskImpl implements PalindromeTask {

    public PalindromeTaskImpl() {}

	@Override
	public Set<String> solve(String inputFile) throws Exception {

        final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        DataSet<String> inputText = env.readTextFile(inputFile);
        DataSet<Tuple2<String, Integer>> palindromes = inputText.map(new palinCounter());
        int maxLength = palindromes.max(1).collect().get(0).f1;
        List<String> tempResult = palindromes
                .filter(x -> (x.f1 == maxLength))
                .map(new MapFunction<Tuple2<String, Integer>, String>() {
                    @Override
                    public String map(Tuple2<String, Integer> value) {
                        return value.f0;
                    }
                })
                .collect();
        Set<String> resultSet = new HashSet<>(tempResult);
        return resultSet;
	}

    static class palinCounter implements MapFunction<String, Tuple2<String, Integer>> {
        public Tuple2<String, Integer>  map(String value) {
            String valueProcessed = value.replaceAll("[^A-Za-z0-9]", "").toLowerCase();
            int stringLength = valueProcessed.length();
            for (int i=0; (i< stringLength /2); i++) {
                if (valueProcessed.charAt(i) != valueProcessed.charAt(stringLength-1-i)) return new Tuple2<>(value,0);
            }
            return new Tuple2<>(value,stringLength);
        }
    }
}

