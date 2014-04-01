package GUI.Util.implementation;

import GUI.Util.GraphBuilder;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import org.apache.commons.collections15.Transformer;

import java.awt.*;

public class DfaGraphBuilder implements GraphBuilder {
    @Override
    public BasicVisualizationServer<Integer, String> getLayout() {
        return null;
    }

    @Override
    public Transformer<Integer, Paint> paintTransformerBuilder(Integer finalStates) {
        return null;
    }

    @Override
    public Transformer<Integer, Shape> shapeTransformerBuilder(int scale) {
        return null;
    }

    @Override
    public Transformer<String, String> edgeLableTransformerBuilder() {
        return null;
    }
}
