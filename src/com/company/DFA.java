package com.company;


import java.util.*;

public class DFA {

    Map<Integer, Map<String, Integer>> stateTable = new HashMap<Integer, Map<String, Integer>>();
    Integer initState = 1;
    Set<Integer> finalStates = new HashSet<Integer>();
    Integer stateNum = 1;
    Set<String> alphabet = new HashSet<String>();

    public DFA() {

    }

}
