package GUI.Util.implementation;

import GUI.Util.GraphBuilder;
import com.Logic.NFA;
import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import org.apache.commons.collections15.Transformer;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;

public class NfaGraphBuilder implements GraphBuilder{
    private Graph<Integer, String> graph;
    private NFA nfa;
    public NfaGraphBuilder(NFA nfa) {
        this.nfa = nfa;
        graph = new SparseGraph<Integer, String>();
        for(Integer state : nfa.getStateTable().keySet()) {
            graph.addVertex(state);
        }
        int edgeId = 0;
        for(Integer state : nfa.getStateTable().keySet()) {
            for(String trans : nfa.getStateTable().get(state).keySet()) {
                for(Integer toState : nfa.getStateTable().get(state).get(trans)) {
                    graph.addEdge(edgeId+"|"+trans, state, toState, EdgeType.DIRECTED);
                    edgeId++;
                }
            }
        }

    }


    @Override
    public BasicVisualizationServer<Integer,String> getLayout() {
        Layout<Integer, String> layout = new ISOMLayout<>(graph);

        layout.setSize(new Dimension(500,500));

        BasicVisualizationServer<Integer,String> vv =
                new BasicVisualizationServer<Integer,String>(layout);

        vv.setPreferredSize(new Dimension(500,500));

        //Transform view
        vv.getRenderContext().setEdgeLabelTransformer(edgeLableTransformerBuilder());
        vv.getRenderContext().setVertexFillPaintTransformer(paintTransformerBuilder());
        vv.getRenderContext().setVertexShapeTransformer(shapeTransformerBuilder(1));
        vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
        vv.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.CNTR);
        return vv;
    }

    @Override
    public Transformer<Integer,Paint> paintTransformerBuilder () {
        return new Transformer<Integer,Paint>() {
            public Paint transform(Integer i) {
                if(i == nfa.getInitState()) return   new Color(80, 152, 178);
                else if(i == nfa.getFinalState()) return   new Color(146, 85, 178);
                return new Color(89, 91, 178);
            }
        };
    }

    @Override
    public Transformer<Integer,Shape> shapeTransformerBuilder (final int scale) {
        return new Transformer<Integer,Shape>(){
            public Shape transform(Integer i){
                Ellipse2D circle = new Ellipse2D.Double(-15, -15, 30, 30);
                return  AffineTransform.getScaleInstance(scale, scale).createTransformedShape(circle);
            }
        };
    }

    //really bad solution
    @Override
    public Transformer<String, String> edgeLableTransformerBuilder () {
        return new Transformer<String, String>() {
            @Override
            public String transform(String c) {
                int i = c.lastIndexOf("|");
                return c.substring(i+1, c.length());
            }
        };
    }

}
