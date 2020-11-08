package org.beanfabrics.javafx;

import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.model.TextPM;

public class RowPM extends AbstractPM {
  TextPM title = new TextPM();
  private int id;

  public RowPM(int id, String title) {
    this.id = id;
    PMManager.setup(this);
    this.title.setText(title);
  }

  @Override
  public String toString() {
    return "RowPM [id=" + id + "]";
  }

}
