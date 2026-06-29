package hangaram.eyes.hospital;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
    public ResponseEntity<Hospital> updateNote(@PathVariable Long id, @RequestBody NoteUpdateRequest request, HttpServletRequest httpRequest) {
        Hospital hospital = hospitalRepository.findById(id).orElseThrow();
        String oldNote = hospital.getNote();
        hospital.setNote(request.note());
        log.info("NOTE_UPDATE hospital={} ip={} [{}] -> [{}]", hospital.getName(), clientIp(httpRequest), oldNote, request.note());
        return ResponseEntity.ok(hospitalRepository.save(hospital));
    }

    private String clientIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        return (forwarded != null && !forwarded.isBlank()) ? forwarded.split(",")[0].trim() : request.getRemoteAddr();
    }

    public record NoteUpdateRequest(String note) {
    }
}
