import com.Logic.DFAimp.DFA;
import com.Logic.NFA;
import org.junit.Assert;
import org.junit.Test;

public class SystemTest {
    @Test
    public void globalTest(){
        NFA nfa = null;
        try {
            nfa = new NFA("(a|b)*abbc");
        } catch (Exception e) {
            e.printStackTrace();
        }
        DFA dfa = new DFA(nfa);
        dfa.minimize();
        System.out.println(nfa.getFinalState());
        System.out.println(dfa.getFinalStates());
        dfa.checkString("baabb");
        Assert.assertEquals(dfa.checkString("baabbc"), true);
        Assert.assertEquals(dfa.checkString("babbc"), true);
        System.out.println(dfa);
    }
}
