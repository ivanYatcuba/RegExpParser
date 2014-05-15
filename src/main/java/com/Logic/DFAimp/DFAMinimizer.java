package com.Logic.DFAimp;


import java.util.*;

public class DFAMinimizer {
    final int STAR = -1;
    final int CROSS = 1;
    final int CHECK = 2;
    final int EMPTY = 0;

    /**
     * Minimize DFA
     * @param dfa DFA to be minimized
     */
    public void minimize(DFA dfa) {
        Map<Integer, Map<String, Integer>> stateTable = dfa.getStateTable();
        Set<Integer> finalStates = dfa.getFinalStates();

        int tableSize = stateTable.size();
        int[][] minimizationTable = new int[tableSize][tableSize];


        //Mark diagonal with check
        for(int i=0; i<tableSize; i++) {
            minimizationTable[i][i] = CHECK;
        }

        //Mark half of table with star
        for(int i=0; i<tableSize; i++) {
            for(int j=0; j<i; j++){
                minimizationTable[i][j] = STAR;
            }

        }

        Set<Integer> notFinal = new HashSet<>();
        notFinal.addAll(stateTable.keySet());
        notFinal.removeAll(finalStates);

        for(int i=1; i<tableSize+1; i++) {
            if(finalStates.contains(i)) {
                for(Integer j : notFinal ){
                    minimizationTable[i-1][j-1] = CROSS;
                }
            }
            if(notFinal.contains(i)) {
                for(Integer j : finalStates ){
                    minimizationTable[i-1][j-1] = CROSS;
                }
            }
        }

        //Main algorithm
        boolean f = true;
        while (f){
            f = markCross(tableSize, minimizationTable, dfa);
        }


        for(int i = 0; i<tableSize; i++) {
            for(int j=i+1; j<tableSize; j++) {
                if(minimizationTable[i][j] == EMPTY){minimizationTable[i][j] = CHECK;}
            }
        }

        //Merge equal states
        mergeEqualStates(tableSize, minimizationTable, dfa);

        //remove unreachable states
        while(removeUnreachable(dfa));
    }


    private boolean removeUnreachable(DFA dfa){
        Map<Integer, Map<String, Integer>> stateTable = dfa.getStateTable();
        Set<Integer> finalStates = dfa.getFinalStates();
        Set<String> alphabet = dfa.getAlphabet();

        Set<Integer> unreachable = new HashSet<>();
        unreachable.addAll(stateTable.keySet());
        for(Integer state : stateTable.keySet()) {
            for(String s : alphabet) {
                unreachable.remove(stateTable.get(state).get(s));
            }
        }
        unreachable.remove(dfa.getInitState());
        for(Integer unreach : unreachable) {
            stateTable.remove(unreach);
            finalStates.remove(unreach);
            dfa.setStateNum(dfa.getStateNum()-1);
        }
        return !unreachable.isEmpty();
    }

    /**
     * Checks in minimization table which states are not unic
     * @param tableSize size of minimization table
     * @param minimizationTable minimization table
     * @param dfa dfa to be minimized
     */
    private boolean markCross(int tableSize,  int[][] minimizationTable, DFA dfa){
        Map<Integer, Map<String, Integer>> stateTable = dfa.getStateTable();
        Set<String> alphabet = dfa.getAlphabet();
        boolean wasChanged = false;
        for(int i=0; i<tableSize; i++){
            for(int j=0; j<tableSize; j++){
                if(minimizationTable[i][j] == EMPTY) {
                    ArrayList<Integer> key = new ArrayList<>();
                    key.add(i);
                    key.add(j);
                    Map<String, ArrayList<Integer>> value = new HashMap<>();
                    for(String c: alphabet) {
                        ArrayList<Integer> states = new ArrayList<>();
                        for(Integer state : key) {
                            states.add(stateTable.get(state+1).get(c));
                        }
                        value.put(c, states);
                        try{
                            if(minimizationTable[states.get(0)-1][states.get(1)-1] == CROSS) {
                                minimizationTable[i][j] = CROSS;
                                wasChanged = true; break;
                            }
                        }catch (Throwable e){ minimizationTable[i][j] = CROSS;wasChanged = true; break;}
                    }
                }
            }
        }
        return wasChanged;
    }


    /**
     * Merge equal states (removeing unuseful states)
     * @param tableSize size of minimization table
     * @param minimizationTable minimization table
     * @param dfa DFA to be minimized
     */
    private void mergeEqualStates (int tableSize,  int[][] minimizationTable, DFA dfa){
        Map<Integer, Map<String, Integer>> stateTable = dfa.getStateTable();
        Set<String> alphabet = dfa.getAlphabet();

        for(int i=0; i<tableSize; i++) {
            for(int j=i+1; j<tableSize; j++){
                if(minimizationTable[i][j] == CHECK && stateTable.containsKey(i+1)){
                    for(String c : alphabet){
                        try{
                            if(stateTable.get(i+1).get(c) == j+1){
                                stateTable.get(i+1).remove(c);
                                stateTable.get(i+1).put(c, stateTable.get(j+1).get(c));
                            }
                        }catch (Throwable t) {}
                    }
                    for(Integer state : stateTable.keySet()) {
                        for(String s : alphabet) {
                            try{
                                if(stateTable.get(state).get(s) == j+1 ){
                                    stateTable.get(state).remove(s);
                                    stateTable.get(state).put(s, i+1);
                                }
                            }catch (Throwable t){}
                        }

                    }
                    stateTable.remove(j+1);
                    dfa.setStateNum(dfa.getStateNum()-1);
                    if(dfa.getFinalStates().contains(j + 1)) dfa.getFinalStates().remove(j + 1);
                }
            }

        }

    }
}
