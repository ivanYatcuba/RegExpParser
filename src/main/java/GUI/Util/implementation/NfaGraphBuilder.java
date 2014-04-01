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

    public NfaGraphBuilder(NFA nfa) {
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
        Layout<Integer, String> layout = new ISOMLayout<Integer, String>(graph);

        layout.setSize(new Dimension(300,300));

        BasicVisualizationServer<Integer,String> vv =
                new BasicVisualizationServer<Integer,String>(layout);

        vv.setPreferredSize(new Dimension(350,350));

        //Transform view
        vv.getRenderContext().setEdgeLabelTransformer(edgeLableTransformerBuilder());
        vv.getRenderContext().setVertexFillPaintTransformer(paintTransformerBuilder(1));
        vv.getRenderContext().setVertexShapeTransformer(shapeTransformerBuilder(1));
        vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
        vv.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.CNTR);
        return vv;
    }

    @Override
    public Transformer<Integer,Paint> paintTransformerBuilder (final Integer finalStates) {
        return new Transformer<Integer,Paint>() {
            public Paint transform(Integer i) {
                if(i == finalStates) return  Color.BLUE;
                return Color.CYAN;
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
