import com.Logic.DFAimp.DFA;
import com.Logic.NFA;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;


public class NFAtoDFATest {
    @Test
    public void conversionTest1(){
        NFA nfa = new NFA();
        nfa.transf("a");
        nfa.transf("b");
        nfa.transf("c");
        DFA dfa = new DFA(nfa);
        DFA expectedDfa = new DFA();
        expectedDfa.addTrans(1,2,"a");
        expectedDfa.addTrans(2,3,"b");
        expectedDfa.addTrans(3,4,"c");
        expectedDfa.getStateTable().put(4,new HashMap<String, Integer>());
        assertEquals(dfa.getStateTable(), expectedDfa.getStateTable());
    }


    @Test
    public void conversionTest2(){
        NFA nfa1 = new NFA();
        NFA nfa2 = new NFA();
        nfa1.transf("a");
        nfa2.transf("b");
        nfa1.alter(nfa2);
        DFA dfa = new DFA(nfa1);
        DFA expectedDfa = new DFA();
        expectedDfa.addTrans(1,3,"a");
        expectedDfa.addTrans(1,2,"b");
        expectedDfa.getStateTable().put(2,new HashMap<String, Integer>());
        expectedDfa.getStateTable().put(3,new HashMap<String, Integer>());
        assertEquals(dfa.getStateTable(), expectedDfa.getStateTable());
    }

    @Test
    public void conversionTest3(){
        NFA nfa1 = new NFA();
        NFA nfa2 = new NFA();
        nfa1.transf("a");
        nfa2.transf("b");
        nfa1.concat(nfa2);
        DFA dfa = new DFA(nfa1);
        DFA expectedDfa = new DFA();
        expectedDfa.addTrans(1,2,"a");
        expectedDfa.addTrans(2,3,"b");
        expectedDfa.getStateTable().put(3,new HashMap<String, Integer>());
        assertEquals(dfa.getStateTable(), expectedDfa.getStateTable());
    }

    @Test
    public void conversionTest4(){
        NFA nfa1 = new NFA();
        nfa1.transf("a");
        nfa1.star(nfa1);
        DFA dfa = new DFA(nfa1);
        DFA expectedDfa = new DFA();
        expectedDfa.addTrans(1,2,"a");
        expectedDfa.addTrans(2,2,"a");
        assertEquals(dfa.getStateTable(), expectedDfa.getStateTable());
    }

}
