package com.company;


import java.util.*;

public class DFA {

    private Map<Integer, Map<String, Integer>> stateTable = new HashMap<Integer, Map<String, Integer>>();
    private Set<String> alphabet = new HashSet<String>();
    private Set<Integer> finalStates = new LinkedHashSet<Integer>();
    private Integer initState = 1;
    private Integer stateNum = 1;
    final int STAR = -1;
    final int CROSS = 1;
    final int CHECK = 2;
    final int EMPTY = 0;

    public DFA() {}

    public void minimize(){

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

        Set<Integer> notFinal = new HashSet<Integer>();
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
        markCross(tableSize, minimizationTable);
        markCross(tableSize, minimizationTable);
        for(int i = 0; i<tableSize; i++) {
            for(int j=i+1; j<tableSize; j++) {
                if(minimizationTable[i][j] == EMPTY){minimizationTable[i][j] = CHECK;}
            }
        }

        //Merge equal states
        for(int i=0; i<tableSize; i++) {
            for(int j=i+1; j<tableSize; j++){
                if(minimizationTable[i][j] == CHECK && stateTable.containsKey(i+1)){
                    for(String c : alphabet){
                        if(stateTable.get(i+1).get(c) == j+1){
                            stateTable.get(i+1).remove(c);
                            stateTable.get(i+1).put(c, stateTable.get(j+1).get(c));
                        }
                    }
                    for(Integer state : stateTable.keySet()) {
                        for(String s : alphabet) {
                            try{
                                if(stateTable.get(state).get(s) == j+1 ){
                                    stateTable.get(state).remove(s);
                                    stateTable.get(state).put(s, i+1);
                                }
                            }catch (Throwable t){
                                stateTable.get(state).put(s, i+1);
                            }
                        }

                    }
                    stateTable.remove(j+1);
                    if(finalStates.contains(j+1)) finalStates.remove(j+1);
                }
            }

        }


        //remove unreachable states
        while(removeUnreachable());


    }

    private boolean removeUnreachable(){
        Set<Integer> unreachable = new HashSet<Integer>();
        unreachable.addAll(stateTable.keySet());
        for(Integer state : stateTable.keySet()) {
            for(String s : alphabet) {
                unreachable.remove(stateTable.get(state).get(s));
            }
        }
        unreachable.remove(initState);
        for(Integer unreach : unreachable) {
            stateTable.remove(unreach);
        }
        return !unreachable.isEmpty();
    }


    private void markCross(int tableSize,  int[][] minimizationTable){
        for(int i=0; i<tableSize; i++){
            for(int j=0; j<tableSize; j++){
                if(minimizationTable[i][j] == EMPTY) {
                    ArrayList<Integer> key = new ArrayList<Integer>();
                    key.add(i);
                    key.add(j);
                    Map<String, ArrayList<Integer>> value = new HashMap<String, ArrayList<Integer>>();
                    for(String c: alphabet) {
                        ArrayList<Integer> states = new ArrayList<Integer>();
                        for(Integer state : key) {
                            states.add(stateTable.get(state+1).get(c));
                        }
                        value.put(c, states);
                        if(minimizationTable[states.get(0)-1][states.get(1)-1] == CROSS) {
                            minimizationTable[i][j] = CROSS; break;

                        }
                    }
                }
            }

        }

    }


    public void addTrans(int from, int to, String s) {
        if(!stateTable.containsKey(from)) {
            stateTable.put(from, new HashMap<String, Integer>());
        }
        stateTable.get(from).put(s,to);
        alphabet.add(s);
    }

    public Map<Integer, Map<String, Integer>> getStateTable() {
        return stateTable;
    }

    @Override
    public String toString() {
        return stateTable.toString();
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
