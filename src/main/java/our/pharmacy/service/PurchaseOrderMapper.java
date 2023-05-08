package our.pharmacy.service;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import our.pharmacy.model.Medicine;
import our.pharmacy.model.PurchaseOrder;
import our.pharmacy.model.ShoppingCartItem;

@Mapper
public interface PurchaseOrderMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "derived", ignore = true)
    ShoppingCartItem map(PurchaseOrder purchaseOrder, Medicine medicine, Integer number);
}
