import com.Logic.DFAimp.DFA;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MinimizationTest {

    @Test
    public void test1() throws Exception {

        DFA dfa = new DFA();

        dfa.addTrans(1,2,"0");
        dfa.addTrans(1,3,"1");

        dfa.addTrans(2,5,"1");
        dfa.addTrans(2,4,"0");

        dfa.addTrans(3,4,"0");
        dfa.addTrans(3,5,"1");

        dfa.addTrans(4,2,"0");
        dfa.addTrans(4,5,"1");

        dfa.addTrans(5,5,"0");
        dfa.addTrans(5,5,"1");

        dfa.getFinalStates().add(4);
        dfa.getFinalStates().add(5);


        DFA expectedDfa = new DFA();

        expectedDfa.addTrans(1,2,"0");
        expectedDfa.addTrans(1,2,"1");

        expectedDfa.addTrans(2,4,"0");
        expectedDfa.addTrans(2,5,"1");

        expectedDfa.addTrans(4,2,"0");
        expectedDfa.addTrans(4,5,"1");

        expectedDfa.addTrans(5,5,"0");
        expectedDfa.addTrans(5,5,"1");

        dfa.minimize();

        assertEquals(dfa.getStateTable(), expectedDfa.getStateTable());
    }

    @Test
    public void test2() throws Exception {
        DFA dfa = new DFA();
        DFA expectedDfa = new DFA();
        dfa.addTrans(1,2,"a");
        dfa.addTrans(1,5,"b");

        dfa.addTrans(2,3,"b");
        dfa.addTrans(2,6,"a");

        dfa.addTrans(3,4,"a");
        dfa.addTrans(3,7,"b");

        dfa.addTrans(4,4,"a");
        dfa.addTrans(4,4,"b");

        dfa.addTrans(5,2,"a");
        dfa.addTrans(5,5,"b");

        dfa.addTrans(6,6,"a");
        dfa.addTrans(6,3,"b");

        dfa.addTrans(7,4,"a");
        dfa.addTrans(7,4,"b");

        dfa.getFinalStates().add(3);
        dfa.getFinalStates().add(7);
        dfa.getFinalStates().add(4);

        expectedDfa = new DFA();

        expectedDfa.addTrans(1,2,"a");
        expectedDfa.addTrans(1,1,"b");

        expectedDfa.addTrans(2,2,"a");
        expectedDfa.addTrans(2,3,"b");

        expectedDfa.addTrans(3,3,"a");
        expectedDfa.addTrans(3,3,"b");

        dfa.minimize();

        assertEquals(dfa.getStateTable(), expectedDfa.getStateTable());
    }

    @Test
    public void test3() throws Exception {
        DFA dfa = new DFA();
        DFA expectedDfa = new DFA();
        dfa = new DFA();

        dfa.addTrans(1,2,"0");
        dfa.addTrans(1,4,"1");

        dfa.addTrans(2,1,"0");
        dfa.addTrans(2,4,"1");

        dfa.addTrans(3,2,"0");
        dfa.addTrans(3,5,"1");

        dfa.addTrans(4,6,"0");
        dfa.addTrans(4,6,"1");

        dfa.addTrans(5,4,"0");
        dfa.addTrans(5,4,"1");

        dfa.addTrans(6,6,"0");
        dfa.addTrans(6,6,"1");

        dfa.getFinalStates().add(6);
        dfa.getFinalStates().add(4);

        expectedDfa = new DFA();

        expectedDfa.addTrans(1,1,"0");
        expectedDfa.addTrans(1,4,"1");

        expectedDfa.addTrans(4,4,"0");
        expectedDfa.addTrans(4,4,"1");

        dfa.minimize();
        assertEquals(dfa.getStateTable(), expectedDfa.getStateTable());
    }

}
