package com.company;


import java.util.*;

public class NFA {

    private Map<Integer, Map<String, List<Integer>>> stateTable;
    private Integer stateNum = 1;
    private Integer initState;
    private Integer finalState;

    public NFA() {

        initState = 1;
        finalState = initState;
        stateTable = new HashMap<Integer, Map<String, List<Integer>>>();
        stateTable.put(initState, new HashMap<String, List<Integer>>());
    }

    public Map<Integer, Map<String, List<Integer>>> getStateTable(){
        return this.stateTable;
    }

    public Integer getFinalState() {
        return finalState;
    }

    public NFA(String exp){

        Stack<NFA> NFAs = new Stack<NFA>();
        Stack<String> ops  = new Stack<String>();
        List<String> posibleOps = new ArrayList<String>(Arrays.asList("*","|","(",")"));
        Stack<Integer> opCount = new Stack<Integer>();
        Integer count = 0;
        String perviousChar = null;

        for (int i = -1; (i = exp.indexOf("*", i + 1)) != -1; ) {
            if(exp.charAt(i-1) != ')') {
                exp = exp.substring(0, i-1) + "(" + exp.substring(i-1,i+1) + ")" + exp.substring(i+1,exp.length());
                i+=2;
            }
        }
        exp = "(" +exp + ")";
        char [] arr = exp.toCharArray();
        for(char c : arr) {
            String s = String.valueOf(c);
            if      (s.equals("("))    ops.push(s);
            else if (s.equals("|"))    ops.push(s);
            else if (s.equals("*"))    ops.push(s);
            else if (s.equals(")")) {
                while (!ops.isEmpty() && !ops.equals("(")) {
                    NFA nfa1 = NFAs.pop();
                    String op = ops.pop();
                    if      (op.equals("|"))    nfa1.alter(NFAs.pop());
                    else if (op.equals("*"))    nfa1.star(nfa1);
                    NFAs.add(nfa1);
                }
            }
            else {
                if(posibleOps.contains(perviousChar) || perviousChar == null) {
                    NFA newNFA = new NFA();
                    newNFA.transf(s);
                    NFAs.add(newNFA);
                }else{
                    NFAs.peek().transf(s);
                }

            }
            perviousChar = s;
        }
        Stack<NFA> lNFA = new Stack<NFA>();
        while(!NFAs.isEmpty()) {
            lNFA.push(NFAs.pop());
        }
        NFA resNFA = lNFA.pop();
        while(!lNFA.isEmpty()) {
            resNFA.concat(lNFA.pop());
        }

        this.stateTable = resNFA.stateTable;
        this.stateNum = resNFA.stateNum;
        this.initState = resNFA.initState;
        this.finalState = resNFA.finalState;
    }


    public void transf(String in){
        stateNum++;
        stateTable.put(stateNum, new HashMap<String, List<Integer>>());
        try{
            stateTable.get(finalState).get(in).add(stateNum);
        }catch (Throwable t){
            stateTable.get(finalState).put(in, new ArrayList<Integer>(Arrays.asList(stateNum)));
        }
        finalState = stateNum;
    }



    private void changeAllIndexBy(NFA nfa, int position, Map<Integer, Map<String, List<Integer>>> tempStateTable ) {
        for(Integer state : nfa.stateTable.keySet()) {
            tempStateTable.put(state+position, new HashMap<String, List<Integer>>());
            for(String key : nfa.stateTable.get(state).keySet()) {
                tempStateTable.get(state+position).put(key, new ArrayList<Integer>());
                for(Integer trans : nfa.stateTable.get(state).get(key)) {
                    tempStateTable.get(state+position).get(key).add(trans+position);
                }
            }
        }

    }


    public NFA  alter(NFA nfa2) {
        NFA resNFA = this;

        //Add new eps transf to nfa1
        Map<Integer, Map<String, List<Integer>>> tempStateTable = new HashMap<Integer, Map<String, List<Integer>>>();

        tempStateTable.put(1, new HashMap<String, List<Integer>>());
        changeAllIndexBy(resNFA, 1, tempStateTable);

        tempStateTable.get(1).put("eps", new ArrayList<Integer>(Arrays.asList(2)));
        resNFA.stateTable = tempStateTable;
        resNFA.stateNum++;
        resNFA.finalState = stateNum;
        ////////////////////////////////
        //Now let's merge nfa1 and nfa2///
        tempStateTable = new HashMap<Integer, Map<String, List<Integer>>>();
        changeAllIndexBy(nfa2, resNFA.stateNum, tempStateTable);

        resNFA.stateTable.putAll(tempStateTable);
        resNFA.stateTable.get(1).get("eps").add(resNFA.stateNum+1);
        resNFA.stateNum += nfa2.stateNum;

        //add final state
        resNFA.stateTable.put(resNFA.stateNum+1, new HashMap<String, List<Integer>>());
        resNFA.stateNum++;
        resNFA.stateTable.get(resNFA.stateNum-nfa2.stateNum-1).put("eps", new ArrayList<Integer>(Arrays.asList(resNFA.stateNum)));
        resNFA.stateTable.get(resNFA.stateNum-1).put("eps", new ArrayList<Integer>(Arrays.asList(resNFA.stateNum)));
        resNFA.finalState = resNFA.stateNum;
        return resNFA;
    }


    public NFA star(NFA nfa) {
        NFA resNfa = nfa;
        //connect nfa init state and final state
        resNfa.stateTable.get(finalState).put("eps", new ArrayList<Integer>(Arrays.asList(resNfa.initState)));

        //add init eps state
        Map<Integer, Map<String, List<Integer>>> tempStateTable = new HashMap<Integer, Map<String, List<Integer>>>();

        tempStateTable.put(1, new HashMap<String, List<Integer>>());
        changeAllIndexBy(resNfa, 1, tempStateTable);

        tempStateTable.get(1).put("eps", new ArrayList<Integer>(Arrays.asList(2)));
        resNfa.stateTable = tempStateTable;
        resNfa.stateNum++;
        resNfa.finalState = stateNum;

        //add last state
        resNfa.transf("eps");

        //connect last state with first
        try {
            resNfa.stateTable.get(1).get("eps").add(resNfa.finalState);
        }catch (Throwable t){
            resNfa.stateTable.get(1).put("eps", new ArrayList<Integer>(Arrays.asList(resNfa.finalState)));
        }
        return resNfa;
    }

    public NFA concat(NFA nfa2){
        NFA resNFA = this;

        resNFA.stateTable.remove(resNFA.finalState);
        Map<Integer, Map<String, List<Integer>>> tempStateTable = new HashMap<Integer, Map<String, List<Integer>>>();
        changeAllIndexBy(nfa2, resNFA.stateNum - 1, tempStateTable);

        resNFA.stateTable.putAll(tempStateTable);

        resNFA.stateNum += nfa2.stateNum-1;
        resNFA.finalState = resNFA.stateNum;
        return resNFA;
    }

    @Override
    public String toString(){
        return this.stateTable.toString();
    }
}
