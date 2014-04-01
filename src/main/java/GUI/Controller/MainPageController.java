package GUI.Controller;


import GUI.Util.implementation.NfaGraphBuilder;
import com.Logic.DFAimp.DFA;
import com.Logic.NFA;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import javax.swing.*;


public class MainPageController {
    @FXML
    TextField s_RE;

    @FXML
    TextField s_String;

    @FXML
    Button b_DFA;

    @FXML
    Button b_NFA;

    @FXML
    Label l_Result;

    private NFA nfa;
    private DFA dfa;

    public void checkRedAction() {
        nfa = new NFA(s_RE.getText());
        dfa = new DFA(nfa);
        dfa.minimize();
        boolean result = dfa.checkString(s_String.getText());
        if(result) {
            l_Result.setText("cool");
        }else {
            l_Result.setText("bad");
        }

    }

    public void openNFAScheme() {
        JFrame frame = new JFrame("Card Layout Example");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel contentPane = new JPanel();


        NfaGraphBuilder nfaGraphBuilder = new NfaGraphBuilder(nfa);
        contentPane.add(nfaGraphBuilder.getLayout());
        frame.setContentPane(contentPane);
        frame.setSize(392, 283);

        frame.setVisible(true);
    }


}
