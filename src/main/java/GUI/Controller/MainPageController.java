package GUI.Controller;


import GUI.Util.GraphBuilder;
import GUI.Util.implementation.DfaGraphBuilder;
import GUI.Util.implementation.NfaGraphBuilder;
import com.Logic.DFAimp.DFA;
import com.Logic.NFA;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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
    ImageView i_result;

    @FXML
    Button ok;
    private NFA nfa = new NFA();
    private DFA dfa = new DFA();
    private String lastRE ="";

    public void checkRedAction() {
        build();
        boolean result = dfa.checkString(s_String.getText());
        if(result) {
            Image image = new Image("/GUI/img/good.png");
            i_result.setImage(image);
        }else {
            Image image = new Image("/GUI/img/bad.png");
            i_result.setImage(image);
        }
    }

    public void openNFAScheme() {
        build();
        buildSchemeWindow(new NfaGraphBuilder(nfa), "NFA");
    }

    public void openDFAScheme() {
        build();
        buildSchemeWindow(new DfaGraphBuilder(dfa), "DFA");
    }

    private void buildSchemeWindow(GraphBuilder graphBuilder, String title) {
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel contentPane = new JPanel();

        contentPane.add(graphBuilder.getLayout());
        frame.setContentPane(contentPane);
        frame.setSize(500, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void build(){
        if(!s_RE.getText().equals(lastRE)){
            try {
                nfa = new NFA(s_RE.getText());
                System.out.println("----------------INFO-----------------");
                System.out.println("Regular expression: "+s_RE.getText());
                System.out.println("NFA: "+nfa);
                dfa = new DFA(nfa);
                System.out.println("DFA: "+dfa);
                dfa.minimize();
                System.out.println("Minimized DFA: "+dfa);
                lastRE = s_RE.getText();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Wrong regular expression!");
            }

        }
    }

}
