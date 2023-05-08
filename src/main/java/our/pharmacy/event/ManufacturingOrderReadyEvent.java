package our.pharmacy.event;

import org.springframework.context.ApplicationEvent;
import our.pharmacy.model.ManufacturingOrder;

public class ManufacturingOrderReadyEvent extends ApplicationEvent {

    public ManufacturingOrderReadyEvent(ManufacturingOrder source) {
        super(source);
    }

    public ManufacturingOrder getManufacturingOrder() {
        return ((ManufacturingOrder) getSource());
    }
}
