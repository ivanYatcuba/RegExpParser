package com.company;


import java.util.*;

public class DFA {

    private Map<Integer, Map<String, Integer>> stateTable = new HashMap<Integer, Map<String, Integer>>();
    private Set<String> alphabet = new HashSet<String>();
    private Set<Integer> finalStates = new HashSet<Integer>();
    private Integer initState = 1;
    private Integer stateNum = 1;

    public DFA() {}

    public Map<Integer, Map<String, Integer>> getStateTable() {
        return stateTable;
    }

    public Integer getInitState() {
        return initState;
    }

    public void setInitState(Integer initState) {
        this.initState = initState;
    }

    public Set<Integer> getFinalStates() {
        return finalStates;
    }

    public Integer getStateNum() {
        return stateNum;
    }

    public void setStateNum(Integer stateNum) {
        this.stateNum = stateNum;
    }

    public Set<String> getAlphabet() {
        return alphabet;
    }

    public void setAlphabet(Set<String> alphabet) {
        this.alphabet = alphabet;
    }
}
