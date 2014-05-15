package com.Logic;

import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.TreeMap;

/**
 * Created by ivan on 15.05.14.
 */
public class NFAParser {

    public static NFA eval(String op, NFA val1, NFA val2) {
        if (op.equals("*")) return val1.star(val1);
        if (op.equals(".")) return val1.concat(val2);
        if (op.equals("|")) return val1.alter(val2);
        throw new RuntimeException("Invalid operator");
    }

    public NFA parseNFA(String expression){
        expression = prepareExpression(expression);
        // precedence order of operators
        TreeMap<String, Integer> precedence = new TreeMap<>();
        precedence.put("(", 0);
        precedence.put(")", 0);
        precedence.put("*", 3);
        precedence.put(".", 2);
        precedence.put("|", 1);;

        Stack<String> ops  = new Stack<>();
        Stack<NFA> vals = new Stack<>();
        String [] sArr = expression.split("");
        List<String> strList =  Arrays.asList(sArr).subList(1, sArr.length);
        for (String s : strList) {

            // token is a value
            if (!precedence.containsKey(s)) {
                NFA nfa = new NFA();
                nfa.transf(s);
                vals.push(nfa);
                continue;
            }

            // token is an operator
            while (true) {
                // the last condition ensures that the operator with higher precedence is evaluated first
                if (ops.isEmpty() || s.equals("(") || (precedence.get(s) > precedence.get(ops.peek()))) {
                    ops.push(s);
                    break;
                }
                // evaluate expression
                String op = ops.pop();
                // but ignore left parentheses
                if (op.equals("(")) {
                    assert s.equals(")");
                    break;
                }
                // evaluate operator and two operands and push result onto value stack
                else {
                    if(!op.equals("*")){
                        NFA nfa2 = vals.pop();
                        NFA nfa1 = vals.pop();
                        vals.push(eval(op, nfa1, nfa2));
                    }else {
                        NFA nfa1 = vals.pop();
                        vals.push(eval(op, nfa1, null));
                    }
                }
            }
        }
        // finished parsing string - evaluate operator and operands remaining on two stacks
        while (!ops.isEmpty()) {
            String op = ops.pop();
            if(!op.equals("*")){
            NFA nfa2 = vals.pop();
            NFA nfa1 = vals.pop();
            vals.push(eval(op, nfa1, nfa2));
            }else {
                NFA nfa1 = vals.pop();
                vals.push(eval(op, nfa1, null));
            }
        }
        assert vals.isEmpty();
        assert ops.isEmpty();
        return vals.pop();
    }
    private String prepareExpression(String expression){
        String reserved = "|*)";
        String [] sArr = expression.split("");
        List<String> strList =  Arrays.asList(sArr).subList(1, sArr.length);
        StringBuilder sb = new StringBuilder();
        sArr = strList.toArray(new String[strList.size()]);
        for(int i=0; i< sArr.length; i++){
            sb.append(sArr[i]);
            if(i+1<sArr.length){
                if(!reserved.contains(sArr[i+1]) && !sArr[i].equals("(") && !sArr[i].equals("|")){
                    sb.append(".");
                }
            }
        }
        return sb.toString();
    }
}
