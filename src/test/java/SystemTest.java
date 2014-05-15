import com.Logic.DFAimp.DFA;
import com.Logic.NFA;
import com.Logic.NFAParser;
import org.junit.Assert;
import org.junit.Test;

public class SystemTest {
    @Test
    public void globalTest1(){
        NFA nfa = null;
        try {
            NFAParser  nfaParser = new NFAParser();
            nfa = nfaParser.parseNFA("(a|b)*abbc");
        } catch (Exception e) {
            e.printStackTrace();
        }
        DFA dfa = new DFA(nfa);
        dfa.minimize();
        Assert.assertEquals(dfa.checkString("baabbc"), true);
        Assert.assertEquals(dfa.checkString("babbc"), true);
        Assert.assertEquals(dfa.checkString("ababc"), false);
    }

    @Test
    public void globalTest2(){
        NFA nfa = null;
        try {
            NFAParser  nfaParser = new NFAParser();
            nfa = nfaParser.parseNFA("a*bc");
        } catch (Exception e) {
            e.printStackTrace();
        }
        DFA dfa = new DFA(nfa);
        dfa.minimize();
        Assert.assertEquals(dfa.checkString("aabc"), true);
        Assert.assertEquals(dfa.checkString("bc"), true);
        Assert.assertEquals(dfa.checkString("abcd"), false);
    }

    @Test
    public void globalTest3(){
        NFA nfa = null;
        try {
            NFAParser  nfaParser = new NFAParser();
            nfa = nfaParser.parseNFA("a|bcdf");
        } catch (Exception e) {
            e.printStackTrace();
        }
        DFA dfa = new DFA(nfa);
        dfa.minimize();
        Assert.assertEquals(dfa.checkString("a"), true);
        Assert.assertEquals(dfa.checkString("bcdf"), true);
        Assert.assertEquals(dfa.checkString("abcdf"), false);
    }

    @Test
    public void globalTest4(){
        NFA nfa = null;
        try {
            NFAParser  nfaParser = new NFAParser();
            nfa = nfaParser.parseNFA(" ");
        } catch (Exception e) {
            e.printStackTrace();
        }
        DFA dfa = new DFA(nfa);
        dfa.minimize();
        Assert.assertEquals(dfa.checkString(" "), true);
        Assert.assertEquals(dfa.checkString("abcdf"), false);
    }

    @Test
    public void globalTest5(){
        NFA nfa = null;
        try {
            NFAParser  nfaParser = new NFAParser();
            nfa = nfaParser.parseNFA("a*b*(cd)*");
        } catch (Exception e) {
            e.printStackTrace();
        }
        DFA dfa = new DFA(nfa);
        dfa.minimize();
        Assert.assertEquals(dfa.checkString("abcd"), true);
        Assert.assertEquals(dfa.checkString("cd"), true);
        Assert.assertEquals(dfa.checkString("acd"), true);
        Assert.assertEquals(dfa.checkString("abc"), false);
    }
}
