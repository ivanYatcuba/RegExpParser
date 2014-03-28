package com.company.DFAimp;


import com.company.NFA;

import java.util.*;

public class DFA {

    private Map<Integer, Map<String, Integer>> stateTable = new HashMap<Integer, Map<String, Integer>>();
    private Set<String> alphabet = new HashSet<String>();
    private Set<Integer> finalStates = new LinkedHashSet<Integer>();
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


    public boolean checkString(String s) {
        char[] characters = s.toCharArray();
        int currentState = 1;

        for(char c : characters) {
            Character ch = new Character(c);
            currentState = stateTable.get(currentState).get(ch.toString());
        }

        return finalStates.contains(currentState);
    }

    public void minimize(){
        DFAMinimizer minimizer = new DFAMinimizer();
        minimizer.minimize(this);
    }

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
