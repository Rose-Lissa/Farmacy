package our.pharmacy.event;

import org.springframework.context.ApplicationEvent;
import our.pharmacy.model.ExternalOrder;
import our.pharmacy.model.PurchaseOrder;

public class ExternalOrderReadyEvent extends ApplicationEvent {

    public ExternalOrderReadyEvent(ExternalOrder externalOrder) {
        super(externalOrder);
    }

    public ExternalOrder getExternalOrder() {
        return ((ExternalOrder) getSource());
    }
}
