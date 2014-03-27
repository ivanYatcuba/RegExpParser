package com.company;

import java.util.ArrayList;

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

        tests.add("(a|b)*abb: \t" + (new NFA("(a|b)*abb")).toString());

        for(String test : tests) {
            System.out.println(test);
        }
        NFA nfa = new NFA("(a|b)*abb");
        DFA dfa = new DFA();
        //NfaToDfaConverter converter = new NfaToDfaConverter();
        //dfa = converter.NFAtoDFA(nfa);
        //dfa.minimize();
       /* dfa.addTrans(1,2,"a");
        dfa.addTrans(1,5,"b");

        dfa.addTrans(2,3,"b");
        dfa.addTrans(2,6,"a");

        dfa.addTrans(3,4,"a");
        dfa.addTrans(3,7,"b");

        dfa.addTrans(4,4,"a");
        dfa.addTrans(4,4,"b");

        dfa.addTrans(5,2,"a");
        dfa.addTrans(5,5,"b");

        dfa.addTrans(6,6,"a");
        dfa.addTrans(6,3,"b");

        dfa.addTrans(7,4,"a");
        dfa.addTrans(7,4,"b");

        dfa.getFinalStates().add(3);
        dfa.getFinalStates().add(7);
        dfa.getFinalStates().add(4);     */
        dfa.addTrans(1,2,"0");
        dfa.addTrans(1,3,"1");

        dfa.addTrans(2,5,"1");
        dfa.addTrans(2,4,"0");

        dfa.addTrans(3,4,"0");
        dfa.addTrans(3,5,"1");

        dfa.addTrans(4,2,"0");
        dfa.addTrans(4,5,"1");

        dfa.addTrans(5,5,"0");
        dfa.addTrans(5,5,"1");

        dfa.getFinalStates().add(4);
        dfa.getFinalStates().add(5);

        System.out.println(dfa);

        dfa.minimize();
    }
}
