package org.beanfabrics.javafx.property;

import java.io.ByteArrayInputStream;

import javafx.scene.image.Image;
import org.beanfabrics.model.image.ImagePM;
import org.beanfabrics.testenv.TestBase;
import org.junit.Test;

public class ImagePmPropertyTest extends TestBase {

  ImagePmProperty underTest = new ImagePmProperty();

  @Test
  public void test_getImage() {
    // Given:
    Image expected = new Image(new ByteArrayInputStream(new byte[64]));
    ImagePM pm = new ImagePM();
    pm.setImage(expected);
    underTest.set(pm);

    // When:
    Image actual = underTest.getImage();

    // Then:
    assertThat(actual).isEqualTo(expected);
  }
}
