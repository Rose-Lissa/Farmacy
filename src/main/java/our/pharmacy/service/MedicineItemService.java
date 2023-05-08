package our.pharmacy.service;

import org.springframework.stereotype.Service;
import our.pharmacy.model.Medicine;
import our.pharmacy.model.MedicineItem;
import our.pharmacy.model.PurchaseOrder;
import our.pharmacy.repository.MedicineItemRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class MedicineItemService {

    private final MedicineItemRepository medicineItemRepository;
    private final MedicineService medicineService;

    public MedicineItemService(MedicineItemRepository medicineItemRepository, MedicineService medicineService) {
        this.medicineItemRepository = medicineItemRepository;
        this.medicineService = medicineService;
    }

    public void addMedicineItem(Long id){
        MedicineItem medicineItem = new MedicineItem();
        medicineItem.setMedicine(medicineService.getMedicineReferenceById(id));
        medicineItem.setSales(false);
        medicineItem.setSerialNumber(UUID.randomUUID().toString());
        medicineItem.setPurchaseOrder(null);
        medicineItemRepository.save(medicineItem);
    }

    public void addMedicineItemToPurchase(Medicine medicine, PurchaseOrder purchaseOrder, Integer quantity) {
        List<MedicineItem> items = new ArrayList<>();
        for (int i = 0; i < quantity; i++) {
            MedicineItem medicineItem = new MedicineItem();
            medicineItem.setMedicine(medicine);
            medicineItem.setSales(false);
            medicineItem.setSerialNumber(UUID.randomUUID().toString());
            medicineItem.setPurchaseOrder(purchaseOrder);
            items.add(medicineItem);
        }

        medicineItemRepository.saveAll(items);
    }

    void saveAll(Iterable<MedicineItem> medicineItems) {
        medicineItemRepository.saveAll(medicineItems);
    }
}
