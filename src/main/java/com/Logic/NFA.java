package com.Logic;


import java.util.*;

public class NFA {

    private Map<Integer, Map<String, List<Integer>>> stateTable;
    private Integer stateNum = 1;
    private Integer initState;
    private Integer finalState;

    public NFA() {

        setInitState(1);
        finalState = getInitState();
        stateTable = new HashMap<>();
        stateTable.put(getInitState(), new HashMap<String, List<Integer>>());
    }

    public Map<Integer, Map<String, List<Integer>>> getStateTable(){
        return this.stateTable;
    }

    public Integer getFinalState() {
        return finalState;
    }

    /**
     * Create NFA using regular expression
     * @param exp regular expression
     * @throws Exception
     */
    public NFA(String exp) throws Exception{

        Stack<NFA> NFAs = new Stack<>();
        Stack<String> ops  = new Stack<>();
        List<String> posibleOps = new ArrayList<>(Arrays.asList("*","|","("));
        String perviousChar = null;
        int alterIndex = 0;

        exp = "(" +exp + ")";
        for (int i = -1; (i = exp.indexOf("*", i + 1)) != -1; ) {
            if(exp.charAt(i-1) != ')') {
                exp = exp.substring(0, i-1) + "(" + exp.substring(i-1,i+1) + ")" + exp.substring(i+1,exp.length());
                i+=2;
            }else{
                int y = exp.substring(0,i-1).lastIndexOf('(');
                exp = exp.substring(0, y) + "(" + exp.substring(y,i-1) + exp.substring(i-1,i+1) + ")" + exp.substring(i+1,exp.length());
                i+=2;
            }
        }


         System.out.print(exp);
        char [] arr = exp.toCharArray();
        for(char c : arr) {
            String s = String.valueOf(c);
            if      (s.equals("("))    ops.push(s);
            else if (s.equals("|"))    ops.push(s);
            else if (s.equals("*"))    ops.push(s);
            else if (s.equals(")")) {
                precalculate(NFAs, ops);
            }
            else {
                if(posibleOps.contains(perviousChar) || perviousChar == null) {
                    NFA newNFA = new NFA();
                    newNFA.transf(s);
                    NFAs.add(newNFA);
                }else{
                    if(NFAs.isEmpty()) NFAs.push(new NFA());
                    NFAs.peek().transf(s);
                }

            }
            perviousChar = s;
        }

        NFA resNFA = mergeNfas(NFAs);
        this.stateTable = resNFA.stateTable;
        this.stateNum = resNFA.stateNum;
        this.setInitState(resNFA.getInitState());
        this.finalState = resNFA.finalState;
    }
    private void precalculate(Stack<NFA> NFAs, Stack<String> ops){
        while (!ops.isEmpty() && !ops.peek().equals("(")) {
            NFA nfa1 = NFAs.pop();
            String op = ops.pop();
            if(op.equals("|")) {
                if(!ops.peek().equals("|")) {
                    try{
                    NFA temp = mergeNfas(NFAs);

                    NFAs.push(temp);

                    nfa1.alter(NFAs.pop());
                    }catch(EmptyStackException ese){}
                }else nfa1.alter(NFAs.pop());
            }
            else if(op.equals("*")) {
                nfa1.star(nfa1);
                try{
                if(!ops.peek().equals("|")) {
                    NFA tmp = NFAs.pop();
                    tmp.concat(nfa1);
                    nfa1 = tmp;
                }
                }catch (EmptyStackException ese){}
            }
            NFAs.add(nfa1);
        }
        if(!ops.isEmpty()) ops.pop();
    }

    private NFA mergeNfas(Stack<NFA> NFAs) {
        Stack<NFA> lNFA = new Stack<>();
        while(!NFAs.isEmpty()) {
            lNFA.push(NFAs.pop());
        }
        NFA resNFA = lNFA.pop();
        while(!lNFA.isEmpty()) {
            resNFA.concat(lNFA.pop());
        }
        return resNFA;
    }

    /**
     * Add a new state to NFA
     * @param in transaction symbol
     */
    public void transf(String in){
        stateNum++;
        stateTable.put(stateNum, new HashMap<String, List<Integer>>());
        try{
            stateTable.get(finalState).get(in).add(stateNum);
        }catch (Throwable t){
            stateTable.get(finalState).put(in, new ArrayList<>(Arrays.asList(stateNum)));
        }
        finalState = stateNum;
    }


    /**
     * Increase all indices by constant number
     * @param nfa in which nfa
     * @param position how much change index
     * @param tempStateTable temporary state table
     */
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

    /**
     * create OR statement
     * @param nfa2 with which dfa create OR
     * @return new nfa
     */
    public NFA  alter(NFA nfa2) {
        NFA resNFA = this;

        //Add new eps transf to nfa1
        Map<Integer, Map<String, List<Integer>>> tempStateTable = new HashMap<>();

        tempStateTable.put(1, new HashMap<String, List<Integer>>());
        changeAllIndexBy(resNFA, 1, tempStateTable);

        tempStateTable.get(1).put("eps", new ArrayList<>(Arrays.asList(2)));
        resNFA.stateTable = tempStateTable;
        resNFA.stateNum++;
        resNFA.finalState = stateNum;
        ////////////////////////////////
        //Now let's merge nfa1 and nfa2///
        tempStateTable = new HashMap<>();
        changeAllIndexBy(nfa2, resNFA.stateNum, tempStateTable);

        resNFA.stateTable.putAll(tempStateTable);
        resNFA.stateTable.get(1).get("eps").add(resNFA.stateNum+1);
        resNFA.stateNum += nfa2.stateNum;

        //add final state
        resNFA.stateTable.put(resNFA.stateNum+1, new HashMap<String, List<Integer>>());
        resNFA.stateNum++;
        resNFA.stateTable.get(resNFA.stateNum-nfa2.stateNum-1).put("eps", new ArrayList<>(Arrays.asList(resNFA.stateNum)));
        resNFA.stateTable.get(resNFA.stateNum-1).put("eps", new ArrayList<>(Arrays.asList(resNFA.stateNum)));
        resNFA.finalState = resNFA.stateNum;
        return resNFA;
    }

    /**
     * Create loop statment
     * @param nfa in which dfa
     * @return new dfa with loop
     */
    public NFA star(NFA nfa) {
        NFA resNfa = nfa;
        //connect nfa init state and final state
        resNfa.stateTable.get(finalState).put("eps", new ArrayList<>(Arrays.asList(resNfa.getInitState())));

        //add init eps state
        Map<Integer, Map<String, List<Integer>>> tempStateTable = new HashMap<>();

        tempStateTable.put(1, new HashMap<String, List<Integer>>());
        changeAllIndexBy(resNfa, 1, tempStateTable);

        tempStateTable.get(1).put("eps", new ArrayList<>(Arrays.asList(2)));
        resNfa.stateTable = tempStateTable;
        resNfa.stateNum++;
        resNfa.finalState = stateNum;

        //add last state
        resNfa.transf("eps");

        //connect last state with first
        try {
            resNfa.stateTable.get(1).get("eps").add(resNfa.finalState);
        }catch (Throwable t){
            resNfa.stateTable.get(1).put("eps", new ArrayList<>(Arrays.asList(resNfa.finalState)));
        }
        return resNfa;
    }

    /**
     * Concat two nfas
     * @param nfa2 with which nfa we need concatination
     * @return new concatinated nfa
     */
    public NFA concat(NFA nfa2){
        NFA resNFA = this;

        resNFA.stateTable.remove(resNFA.finalState);
        Map<Integer, Map<String, List<Integer>>> tempStateTable = new HashMap<>();
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

    public Integer getInitState() {
        return initState;
    }

    public void setInitState(Integer initState) {
        this.initState = initState;
    }
}
