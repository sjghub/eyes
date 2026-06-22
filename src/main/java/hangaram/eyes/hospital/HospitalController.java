package hangaram.eyes.hospital;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/hospitals")
public class HospitalController {

    private final HospitalRepository hospitalRepository;

    public HospitalController(HospitalRepository hospitalRepository) {
        this.hospitalRepository = hospitalRepository;
    }

    @GetMapping
    public List<Hospital> findAll() {
        return hospitalRepository.findAllByOrderByNoAsc();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Hospital> updateNote(@PathVariable Long id, @RequestBody NoteUpdateRequest request) {
        Hospital hospital = hospitalRepository.findById(id).orElseThrow();
        hospital.setNote(request.note());
        return ResponseEntity.ok(hospitalRepository.save(hospital));
    }

    public record NoteUpdateRequest(String note) {
    }
}
