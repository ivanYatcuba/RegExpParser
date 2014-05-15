package GUI.Util.implementation;

import GUI.Util.GraphBuilder;
import com.Logic.DFAimp.DFA;
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

public class DfaGraphBuilder implements GraphBuilder {
    private Graph<Integer, String> graph;
    private DFA dfa;
    public DfaGraphBuilder(DFA dfa) {
        this.dfa = dfa;
        int deadState = Integer.MIN_VALUE;
        for (int d : dfa.getStateTable().keySet()) {
            if (d > deadState) deadState = d;
        }
        graph = new SparseGraph<>();
        for(Integer state : dfa.getStateTable().keySet()) {
            if(state != deadState){
                graph.addVertex(state);
            }
        }
        int edgeId = 0;
        for(Integer state : dfa.getStateTable().keySet()) {
            if(state != deadState ){
                for(String trans : dfa.getStateTable().get(state).keySet()) {
                    Integer toState = dfa.getStateTable().get(state).get(trans);
                    if(toState != deadState ) {
                        graph.addEdge(edgeId+"|"+trans, state, toState, EdgeType.DIRECTED);
                        edgeId++;
                    }
                }
            }

        }
    }

    @Override
    public BasicVisualizationServer<Integer,String> getLayout() {
        Layout<Integer, String> layout = new ISOMLayout<Integer, String>(graph);

        layout.setSize(new Dimension(500,500));

        BasicVisualizationServer<Integer,String> vv =
                new BasicVisualizationServer<Integer,String>(layout);

        vv.setPreferredSize(new Dimension(500, 500));

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
                if(dfa.getFinalStates().contains(i)) return  new Color(205, 162, 49);
                else if(i == dfa.getInitState()) return new Color(231, 162, 132);
                return new Color(231, 92, 59);
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
