package components;

import dev.JustRed23.redbit.engine.obj.Component;
import org.slf4j.Logger;

public class TestComponent extends Component {

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(TestComponent.class);

    private boolean once = false;

    protected void init() {
        LOGGER.info("TestComponent init");
    }

    protected void update() {
        if (!once) {
            once = true;
            LOGGER.info("TestComponent update");
        }
    }
}
