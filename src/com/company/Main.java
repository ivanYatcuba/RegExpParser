package com.company;

import java.util.ArrayList;
import java.util.Set;

public class Main {

    public static void main(String[] args) {

        ArrayList<String> tests = new ArrayList<String>();
        /*tests.add("a: \t" + (new NFA("a")).toString());
        tests.add("ab: \t" + (new NFA("ab")).toString());
        tests.add("a|b: \t" + (new NFA("a|b")).toString());
        tests.add("a*: \t" + (new NFA("a*")).toString());
        tests.add("ab|c: \t" + (new NFA("ab|c")).toString());
        tests.add("ab|c*: \t" + (new NFA("ab|c*")).toString());
        tests.add("(ab|c)*: \t" + (new NFA("(ab|c)*")).toString());
        tests.add("ab*|c: \t" + (new NFA("ab*|c")).toString());
        tests.add("(ab)*|c*: \t" + (new NFA("(ab)*|c*")).toString());
        tests.add("ab|ce: \t" + (new NFA("ab|ce")).toString());
        tests.add("a*|b: \t" + (new NFA("a*|b")).toString());
        tests.add("ba*: \t" + (new NFA("ba*")).toString());*/

        tests.add("a|b: \t" + (new NFA("a|b")).toString());

        for(String test : tests) {
            System.out.println(test);
        }
        NFA nfa = new NFA("a|b");
        DFA dfa = new DFA();
        Set<Integer> states = dfa.epsClosure(nfa.getStateTable(), 1);
        System.out.println(states);
        System.out.println(dfa.goToState(nfa.getStateTable(), states, "a"));
        dfa.NFAtoDFA(nfa);
    }
}
