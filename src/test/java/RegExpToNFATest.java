import com.Logic.NFA;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RegExpToNFATest {
    @Test
    public void NfaCreationTest1() throws Exception {
        NFA nfa = new NFA("abc");

        NFA expectedNFA = new NFA();
        expectedNFA.transf("a");
        expectedNFA.transf("b");
        expectedNFA.transf("c");
        assertEquals(nfa.getStateTable(), expectedNFA.getStateTable());
    }

    @Test
    public void NfaCreationTest2() throws Exception {
        NFA nfa = new NFA("a|b");

        NFA expectedNFA = new NFA();
        NFA tempNFA = new NFA();
        expectedNFA.transf("b");
        tempNFA.transf("a");
        expectedNFA.alter(tempNFA);
        assertEquals(nfa.getStateTable(), expectedNFA.getStateTable());
    }

    @Test
    public void NfaCreationTest3() throws Exception {
        NFA nfa = new NFA("a*");

        NFA expectedNFA = new NFA();
        expectedNFA.transf("a");
        expectedNFA.star(expectedNFA);

        assertEquals(nfa.getStateTable(), expectedNFA.getStateTable());
    }
}
