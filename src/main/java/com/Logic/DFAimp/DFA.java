package com.Logic.DFAimp;


import com.Logic.NFA;

import java.util.*;

public class DFA {

    private Map<Integer, Map<String, Integer>> stateTable = new HashMap<>();
    private Set<String> alphabet = new HashSet<>();
    private Set<Integer> finalStates = new LinkedHashSet<>();
    private Integer initState = 1;
    private Integer stateNum = 1;


    public DFA() { stateTable.put(1, new HashMap<String, Integer>());}

    public DFA(NFA nfa) {
        NfaToDfaConverter converter = new NfaToDfaConverter();
        DFA dfa = converter.NFAtoDFA(nfa);
        this.stateTable = dfa.stateTable;
        this.alphabet = dfa.alphabet;
        this.finalStates = dfa.finalStates;
        this.initState = dfa.initState;
        this.stateNum = dfa.stateNum;
    }

    /**
     * Check if string belongs to regular expression
     * @param s  actual string
     * @return true if string belongs to regular expression
     */
    public boolean checkString(String s) {
        if(s.isEmpty() && alphabet.isEmpty()) return true; //dirty empty string cheat
        char[] characters = s.toCharArray();
        int currentState = 1;

        for(char c : characters) {
            Character ch = new Character(c);
            try {
                currentState = stateTable.get(currentState).get(ch.toString());
            } catch (NullPointerException e) { return false;}

        }

        return finalStates.contains(currentState);
    }

    /**
     * Minimize dfa using DFaMinimizer
     */
    public void minimize(){
        DFAMinimizer minimizer = new DFAMinimizer();
        minimizer.minimize(this);
        initState = 1;
    }

    /**
     * Add a new transaction between DFA states
     * @param from what state make transaction
     * @param to what state make transaction
     * @param s which character is used for transaction
     */
    public void addTrans(int from, int to, String s) {
        if(!stateTable.containsKey(from)) {
            stateTable.put(from, new HashMap<String, Integer>());
            stateNum++;
        }
        stateTable.get(from).put(s,to);
        alphabet.add(s);

    }

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

    @Override
    public String toString() {
        return stateTable.toString();
    }
}
