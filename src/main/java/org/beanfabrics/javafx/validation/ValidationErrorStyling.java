package org.beanfabrics.javafx.validation;

import java.net.URL;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;

public class ValidationErrorStyling {
  private static final URL STYLESHEET = ValidationErrorStyling.class.getResource("beanfabrics-validation.css");

  private ValidationErrorStyling() {}

  public static void install(Scene scene) {
    ObservableList<String> stylesheets = scene.getStylesheets();
    install(stylesheets);
  }

  public static void install(ObservableList<String> stylesheets) {
    stylesheets.add(STYLESHEET.toExternalForm());
  }

  public static void setValidationError(Node node, boolean value) {
    if (value) {
      setValidationError(node);
    } else {
      removeValidationError(node);
    }
  }

  public static void setValidationError(Node node) {
    ObservableList<String> styles = node.getStyleClass();
    if (!styles.contains("validation-error")) {
      styles.add("validation-error");
    }
  }

  public static void removeValidationError(Node node) {
    ObservableList<String> styles = node.getStyleClass();
    styles.remove("validation-error");
  }

}
