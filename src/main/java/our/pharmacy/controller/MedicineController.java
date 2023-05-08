package our.pharmacy.controller;

import org.springframework.web.bind.annotation.*;
import our.pharmacy.dto.MedicineDto;
import our.pharmacy.dto.MedicineFilter;
import our.pharmacy.dto.MedicineGuide;
import our.pharmacy.model.MedicineDerived;
import our.pharmacy.service.MedicineService;

@RestController
public class MedicineController {

    private final MedicineService medicineService;

    public MedicineController(MedicineService medicineService) {
        this.medicineService = medicineService;
    }

    @PostMapping("/medicine/guide")
    public MedicineGuide medicineGuide(@RequestBody MedicineFilter filter){
        MedicineGuide medicineGuide = medicineService.getMedicineGuide(filter);
        return medicineGuide;
    }

    @GetMapping("/medicine")
    public MedicineDto getMedicine(@RequestParam Long id){
        return medicineService.getMedicineDto(id);
    }

    @PostMapping("/medicine")
    public void createMedicine(@RequestBody MedicineDto medicineDto){
        medicineService.createMedicine(medicineDto);
    }

    @PutMapping("/medicine")
    public void updateMedicine(@RequestBody MedicineDto medicineDto) {
        medicineService.updateMedicine(medicineDto);
    }

    @DeleteMapping("/medicine")
    public void deleteMedicine(@RequestParam Long id){
        medicineService.deleteMedicine(id);
    }

}
