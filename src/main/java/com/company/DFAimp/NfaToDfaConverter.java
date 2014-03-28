package com.company.DFAimp;


import com.company.NFA;

import java.util.*;

public class NfaToDfaConverter {

    public DFA NFAtoDFA(NFA nfa) {
        DFA dfa = new DFA();
        Map<Set<Integer>, Map<String, Set<Integer>>> dfaStateTable = new HashMap<Set<Integer>, Map<String, Set<Integer>>>();
        Map<Set<Integer>, Integer> stateIndex = new HashMap<Set<Integer>, Integer>();
        int stateNum = 0;

        //building an alphabet
        dfa.setAlphabet(buildDfaAlphabet(nfa));

        //let`s go and add init state!
        Set<Integer> initState = this.epsClosure(nfa.getStateTable(), 1);
        dfaStateTable.put(initState, new HashMap<String, Set<Integer>>());
        stateNum++;
        stateIndex.put(initState, stateNum);
        if(initState.contains(nfa.getFinalState())) {
            dfa.getFinalStates().add(stateNum);
        }

        // so on...
        Stack<Set<Integer>> statesStack = new Stack<Set<Integer>>();
        statesStack.push(initState);
        while(!statesStack.isEmpty()){
            Set<Integer> dfaState = statesStack.pop();
            for(String s : dfa.getAlphabet()){
                Set<Integer> newState = this.goToState(nfa.getStateTable(), dfaState, s);

                if(!dfaStateTable.keySet().contains(newState) && !newState.isEmpty()) {
                    statesStack.push(newState);
                    dfaStateTable.put(newState, new HashMap<String, Set<Integer>>());
                    stateNum++;
                    stateIndex.put(newState, stateNum);
                    if(newState.contains(nfa.getFinalState())) {
                        dfa.getFinalStates().add(stateNum);
                    }
                }

                if(!newState.isEmpty()) dfaStateTable.get(dfaState).put(s, newState);
            }
        }

        //get ready for building dfa state table!
        //here we go...
        for(Set<Integer> dfaState : dfaStateTable.keySet()) {
            Integer i = stateIndex.get(dfaState);
            dfa.getStateTable().put(i, new HashMap<String, Integer>());
            for(String s : dfaStateTable.get(dfaState).keySet()) {
                dfa.getStateTable().get(i).put(s, stateIndex.get(dfaStateTable.get(dfaState).get(s)));
            }
        }

        return dfa;

    }



    private Set<String> buildDfaAlphabet(NFA nfa) {
        Set<String> alphabet = new HashSet<String>();

        try{
            for(Integer i : nfa.getStateTable().keySet()) {
                for(String s : nfa.getStateTable().get(i).keySet()) {
                    alphabet.add(s);
                }
            }
        }catch (NullPointerException ex) {};
        if(alphabet.contains("eps")){alphabet.remove("eps");}

        return alphabet;
    }

    private Set<Integer> epsClosure(Map<Integer, Map<String, List<Integer>>> stateTable, Integer state) {
        Set<Integer> closure = new HashSet<Integer>();

        closure.add(state);
        try{
            for(Integer trans : stateTable.get(state).get("eps")) {
                closure.addAll(epsClosure(stateTable, trans));
            }
        }catch (Throwable t){};

        return closure;
    }

    private Set<Integer> goToState(Map<Integer, Map<String, List<Integer>>> stateTable, Set<Integer> states, String s) {
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
