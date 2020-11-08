package org.beanfabrics.javafx.controller;

import org.beanfabrics.ModelSubscriber;
import org.beanfabrics.View;
import org.beanfabrics.model.PresentationModel;
import javafx.fxml.Initializable;

public interface BnFxController<PM extends PresentationModel> extends View<PM>, ModelSubscriber, Initializable {

}
