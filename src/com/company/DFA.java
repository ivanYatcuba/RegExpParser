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

    public void NFAtoDFA(NFA nfa) {
        Map<Set<Integer>, Map<String, Set<Integer>>> dfaStateTable = new HashMap<Set<Integer>, Map<String, Set<Integer>>>();
        //building an alphabet
        try{
            for(Integer i : nfa.getStateTable().keySet()) {
                for(String s : nfa.getStateTable().get(i).keySet()) {
                    alphabet.add(s);
                }
            }
        }catch (NullPointerException ex) {};
        if(alphabet.contains("eps")){alphabet.remove("eps");}

        //let`s go and add init state!
        Set<Integer> initState = this.epsClosure(nfa.getStateTable(), 1);
        dfaStateTable.put(initState, new HashMap<String, Set<Integer>>());

        // so on...
        for(Set<Integer> dfaState : dfaStateTable.keySet()){
            for(String s : alphabet){
                Set<Integer> newState = this.goToState(nfa.getStateTable(), initState, s);
                dfaStateTable.put(newState, new HashMap<String, Set<Integer>>());
                dfaStateTable.get(dfaState).put(s, newState);
            }
        }

        //yo, mark dfa states by indices and fill final states set
        Map<Set<Integer>, Integer> stateIndex = new HashMap<Set<Integer>, Integer>();
        for(Set<Integer> dfaState : dfaStateTable.keySet()) {
            stateIndex.put(dfaState, this.stateNum);
            if(dfaState.contains(nfa.getFinalState())) {
                this.finalStates.add(this.stateNum);
            }
            this.stateNum++;
        }

        //get ready for building dfa state table!
        //here we go...
        for(Set<Integer> dfaState : dfaStateTable.keySet()) {
            Integer i = stateIndex.get(dfaState);
            this.stateTable.put(i, new HashMap<String, Integer>());
            for(String s : dfaStateTable.get(dfaState).keySet()) {
                this.stateTable.get(i).put(s, stateIndex.get(dfaStateTable.get(dfaState).get(s)));
            }
        }

        System.out.println(stateTable);
        System.out.println(finalStates);


    }

    public Set<Integer> epsClosure(Map<Integer, Map<String, List<Integer>>> stateTable, Integer state) {
        Set<Integer> closure = new HashSet<Integer>();

        closure.add(state);
        try{
            for(Integer trans : stateTable.get(state).get("eps")) {
                closure.addAll(epsClosure(stateTable, trans));
            }
        }catch (Throwable t){};

        return closure;
    }

    public Set<Integer> goToState(Map<Integer, Map<String, List<Integer>>> stateTable, Set<Integer> states,String s) {
        Set<Integer> transStates = new HashSet<Integer>();

        for(Integer state : states) {
            try{
                transStates.addAll(stateTable.get(state).get(s));
            }catch (NullPointerException ex){};
        }

        Set<Integer> resultStates = new HashSet<Integer>();
        for(Integer state : transStates) {
            resultStates.addAll(this.epsClosure(stateTable, state));
        }

        return resultStates;
    }

}
