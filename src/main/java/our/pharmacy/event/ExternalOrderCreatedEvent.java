package our.pharmacy.event;

import org.springframework.context.ApplicationEvent;
import our.pharmacy.model.ExternalOrder;

public class ExternalOrderCreatedEvent extends ApplicationEvent {

    public ExternalOrderCreatedEvent(Object source) {
        super(source);
    }

    public ExternalOrder getExternalOrder(){
        return ((ExternalOrder) getSource());
    }
}
