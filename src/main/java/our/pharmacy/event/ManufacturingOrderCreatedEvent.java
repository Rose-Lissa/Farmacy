package our.pharmacy.event;

import org.springframework.context.ApplicationEvent;
import our.pharmacy.model.ManufacturingOrder;

public class ManufacturingOrderCreatedEvent extends ApplicationEvent {

    public ManufacturingOrderCreatedEvent(Object source) {
        super(source);
    }

    public ManufacturingOrder getManufacturingOrder(){
        return ((ManufacturingOrder) getSource());
    }
}
