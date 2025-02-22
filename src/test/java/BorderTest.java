import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import processing.core.PApplet;
import processing.core.PVector;
import uk.qumarth.qlib.Border;

public class BorderTest {
    @Test
    void testTraditionalBorderCorrectDimensions() {
        PApplet app = new PApplet();
        app.width = 1000;
        app.height = 1000;

        Border border = new Border(Border.BorderType.TRADITIONAL, app);

        PVector expectedStart = new PVector(45.45f, 58.3f);
        Assertions.assertEquals(expectedStart.x, border.getStart().x, 0.1);
        Assertions.assertEquals(expectedStart.y, border.getStart().y, 0.1);

        PVector expectedEnd = new PVector(954.55f, 891.67f);
        Assertions.assertEquals(expectedEnd.x, border.getEnd().x, 0.1);
        Assertions.assertEquals(expectedEnd.y, border.getEnd().y, 0.1);
    }

    @Test
    void testModernBorderCorrectDimensions() {
        PApplet app = new PApplet();
        app.width = 1000;
        app.height = 1000;

        Border border = new Border(Border.BorderType.MODERN, app);

        PVector expectedStart = new PVector(45.45f, 45.45f);
        Assertions.assertEquals(expectedStart.x, border.getStart().x, 0.1);
        Assertions.assertEquals(expectedStart.y, border.getStart().y, 0.1);

        PVector expectedEnd = new PVector(954.55f, 954.55f);
        Assertions.assertEquals(expectedEnd.x, border.getEnd().x, 0.1);
        Assertions.assertEquals(expectedEnd.y, border.getEnd().y, 0.1);
    }
}
