package com.company;

import com.sun.javafx.collections.MappingChange;

public class DFA {
    MappingChange.Map<Integer, MappingChange.Map<String, Integer>> stateTable;
    Integer initState = 1;
    Integer finalState = initState;
    Integer stateNum = 1;

    public DFA() {

    }

    public DFA(NFA nfa) {

    }

}
