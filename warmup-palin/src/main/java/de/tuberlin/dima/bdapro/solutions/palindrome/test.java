package de.tuberlin.dima.bdapro.solutions.palindrome;

public class test {
    public static void main(String[] args) throws Exception {

        PalindromeTaskImpl t = new PalindromeTaskImpl();
//        PalindromeTaskImplwithCross t = new PalindromeTaskImplwithCross();
//        PalindromeTaskImplWithBroadcast t = new PalindromeTaskImplWithBroadcast();


        System.out.println(t.solve("warmup-palin/test_input.txt"));
    }
}
