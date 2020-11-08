package org.beanfabrics.javafx.validation;

import java.util.List;

import com.google.common.base.Preconditions;
import impl.org.controlsfx.ImplUtils;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.Region;
import javafx.scene.shape.Rectangle;
import org.controlsfx.control.decoration.Decoration;

public class OverlayGraphicDecoration extends Decoration {

  private final Rectangle decorationNode;

  public OverlayGraphicDecoration(Rectangle decorationNode) {
    this.decorationNode = decorationNode;
    this.decorationNode.setManaged(false);
  }

  @Override
  public Node applyDecoration(Node targetNode) {
    Preconditions.checkArgument(targetNode instanceof Region, "Expected %s to be instance of %s", targetNode.getClass(),
        Region.class);

    List<Node> targetNodeChildren = ImplUtils.getChildren((Parent) targetNode.getParent(), true);
    if (!targetNodeChildren.contains(decorationNode)) {
      updateGraphicPosition((Region) targetNode);
      targetNodeChildren.add(decorationNode);
    }
    return null;
  }

  @Override
  public void removeDecoration(Node targetNode) {
    List<Node> targetNodeChildren = ImplUtils.getChildren((Parent) targetNode.getParent(), true);

    if (targetNodeChildren.contains(decorationNode)) {
      targetNodeChildren.remove(decorationNode);

      decorationNode.layoutXProperty().unbind();
      decorationNode.layoutYProperty().unbind();
      decorationNode.widthProperty().unbind();
      decorationNode.heightProperty().unbind();
    }
  }

  private void updateGraphicPosition(Region targetNode) {
    Bounds targetBounds = targetNode.getLayoutBounds();

    double targetWidth = targetBounds.getWidth();
    if (targetWidth <= 0) {
      targetWidth = targetNode.prefWidth(-1);
    }

    double targetHeight = targetBounds.getHeight();
    if (targetHeight <= 0) {
      targetHeight = targetNode.prefHeight(-1);
    }

    /**
     * If both targetWidth and targetHeight are equal to 0, this means the targetNode has not been laid out so we can
     * put a listener in order to catch when the layout will be updated, and then we will place our decorationNode to
     * the proper position.
     */
    if (targetWidth <= 0 && targetHeight <= 0) {
      targetNode.layoutBoundsProperty().addListener(new ChangeListener<Bounds>() {

        @Override
        public void changed(ObservableValue<? extends Bounds> observable, Bounds oldValue, Bounds newValue) {
          targetNode.layoutBoundsProperty().removeListener(this);
          updateGraphicPosition(targetNode);
        }
      });
    }

    decorationNode.layoutXProperty().bind(targetNode.layoutXProperty());
    decorationNode.layoutYProperty().bind(targetNode.layoutYProperty());
    decorationNode.widthProperty().bind(targetNode.widthProperty());
    decorationNode.heightProperty().bind(targetNode.heightProperty());
  }
}
