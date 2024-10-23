package com.emadsolutions.LMS.patron;

import com.emadsolutions.LMS.DTOs.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class PatronController {
    private final PatronService patronService;

    @Autowired
    public PatronController(PatronService patronService) {
        this.patronService = patronService;
    }

    @GetMapping("/patrons")
    public ResponseEntity<?> getPaginatedListOfPatrons
            (
                    @RequestParam(defaultValue = "0") int pageNumber,
                    @RequestParam(defaultValue = "10") int pageSize
            ) {

        PaginatedListDTO<PatronToDTO> listOfPatrons = patronService.getPaginatedListOfPatrons(
                pageNumber
                , pageSize
        );
        return ResponseEntity.ok(new CustomResponse<>(CustomResponse.ResponseStatus.success, null, listOfPatrons));

    }

    @GetMapping("/patrons/{id}")
    public ResponseEntity<?> getSinglePatron(@PathVariable Long id) {
        Patron foundPatron = patronService.getSinglePatron(id);
        PatronToDTO patronDTO = PatronDTOMapper.INSTANCE.toDTO(foundPatron);
        return ResponseEntity.ok(new CustomResponse<>(CustomResponse.ResponseStatus.success, null, patronDTO));
    }

    @PostMapping("/patrons")
    public ResponseEntity<?> addNewPatron(@Valid @RequestBody Patron patron) {
        PatronToDTO thePatron = patronService.addNewPatron(patron);
        var response = new CustomResponse<>(CustomResponse.ResponseStatus.success, null, thePatron);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/patrons/{id}")
    public ResponseEntity<?> updatePatron(@PathVariable Long id, @Valid @RequestBody UpdatePatronToEntity patron)
    {
        PatronToDTO thePatron = patronService.updatePatron(id, patron);
        return ResponseEntity.ok(new CustomResponse<>(CustomResponse.ResponseStatus.success, null, thePatron));
    }
    @DeleteMapping("/patrons/{id}")
    public ResponseEntity<?> deletePatron(@PathVariable Long id)
    {
        patronService.deletePatron(id);
        return ResponseEntity.ok(new CustomResponse<>(CustomResponse.ResponseStatus.success,
                "Patron with id: "+ id +" is deleted"));
    }

}
