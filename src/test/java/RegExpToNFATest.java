import com.Logic.NFA;
import com.Logic.NFAParser;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RegExpToNFATest {
    @Test
    public void NfaCreationTest1() throws Exception {
        NFAParser nfaParser = new NFAParser();
        NFA  nfa = nfaParser.parseNFA("abc");

        NFA expectedNFA = new NFA();
        expectedNFA.transf("a");
        expectedNFA.transf("b");
        expectedNFA.transf("c");
        assertEquals(nfa.getStateTable(), expectedNFA.getStateTable());
    }

    @Test
    public void NfaCreationTest2() throws Exception {
        NFAParser nfaParser = new NFAParser();
        NFA  nfa = nfaParser.parseNFA("a|b");

        NFA expectedNFA = new NFA();
        NFA tempNFA = new NFA();
        expectedNFA.transf("b");
        tempNFA.transf("a");
        expectedNFA.alter(tempNFA);
        assertEquals(nfa.getStateTable(), expectedNFA.getStateTable());
    }

    @Test
    public void NfaCreationTest3() throws Exception {
        NFAParser nfaParser = new NFAParser();
        NFA  nfa = nfaParser.parseNFA("a*");

        NFA expectedNFA = new NFA();
        expectedNFA.transf("a");
        expectedNFA.star(expectedNFA);

        assertEquals(nfa.getStateTable(), expectedNFA.getStateTable());
    }
}
