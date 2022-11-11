package object;

import javax.imageio.ImageIO;
import java.io.IOException;

public class Door extends SuperObject {

    public Door() {
        name = "Door";
        try {
            image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("objects/door.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
