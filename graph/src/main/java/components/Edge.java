package components;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;

public class Edge extends Group {

    protected Cell source;
    protected Cell target;
    Arrow arrowLine;

    public Edge(Cell source, Cell target) {

        this.source = source;
        this.target = target;

        source.addCellChild(target);
        target.addCellParent(source);

    }

    public Cell getSource() {
        return source;
    }

    public Cell getTarget() {
        return target;
    }

    public void setEdge(){



      arrowLine = new Arrow();
        arrowLine.startXProperty().bind(Bindings.createDoubleBinding(() -> {
            Bounds b = source.getBoundsInParent();
            return (b.getMinX() + b.getWidth() / 2) - 50 ;
        }, source.boundsInParentProperty()));
        arrowLine.startYProperty().bind(Bindings.createDoubleBinding(() -> {
            Bounds b = source.getBoundsInParent();
            return (b.getMinY() + b.getHeight() / 2) ;
        }, source.boundsInParentProperty()));
        arrowLine.endXProperty().bind(Bindings.createDoubleBinding(() -> {
            Bounds b = target.getBoundsInParent();
            return (b.getMinX() + b.getWidth() / 2 ) - 50;
        }, target.boundsInParentProperty()));
        arrowLine.endYProperty().bind(Bindings.createDoubleBinding(() -> {
            Bounds b = target.getBoundsInParent();
            return (b.getMinY() + b.getHeight() / 2)   ;
        }, target.boundsInParentProperty()));

        getChildren().add(arrowLine);



    }

    public Arrow getArrowLine() {
        return arrowLine;
    }
}