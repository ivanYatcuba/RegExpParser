package GUI.Util;

import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import org.apache.commons.collections15.Transformer;

import java.awt.*;

public interface GraphBuilder {

    public BasicVisualizationServer<Integer,String> getLayout() ;

    public Transformer<Integer,Paint> paintTransformerBuilder ();

    public Transformer<Integer,Shape> shapeTransformerBuilder (final int scale);

    public Transformer<String, String> edgeLableTransformerBuilder ();

}
