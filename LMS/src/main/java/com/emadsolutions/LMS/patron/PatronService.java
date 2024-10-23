package com.emadsolutions.LMS.patron;

import com.emadsolutions.LMS.DTOs.*;
import com.emadsolutions.LMS.Exceptions.ConflictException;
import com.emadsolutions.LMS.book.Book;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PatronService {
    private final PatronRepository patronRepository;

    @Autowired
    public PatronService(PatronRepository patronRepository) {
        this.patronRepository = patronRepository;
    }

    public PaginatedListDTO<PatronToDTO> getPaginatedListOfPatrons  (int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Patron> patrons = patronRepository.findAll(pageable);

        List<PatronToDTO> patronDTOs = patrons.getContent().stream()
                .map(PatronDTOMapper.INSTANCE::toDTO)
                .toList();

        Page<PatronToDTO> patronDTOPage = new PageImpl<>(patronDTOs, pageable, patrons.getTotalElements());
        return new PaginatedListDTO<>(patronDTOPage);
    }

    public Patron getSinglePatron(Long id)
    {
        return patronRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Did not find patron with id - " + id));
    }
    @Transactional
    public PatronToDTO addNewPatron(Patron patron)
    {
        Patron savedPatron = patronRepository.save(patron);
        return PatronDTOMapper.INSTANCE.toDTO(savedPatron);
    }
    @Transactional
    public PatronToDTO updatePatron(Long id, UpdatePatronToEntity patron)
    {
        System.out.println("reqPatron: "+patron);
        Patron existingPatron = getSinglePatron(id);
        System.out.println("fromDB: "+ existingPatron);
        Patron patronUpdates = UpdatePatronToEntityMapper.INSTANCE.toEntity(patron);
        System.out.println("Converted "+patronUpdates);
        BeanUtils.copyProperties(patronUpdates, existingPatron,"id","borrowingRecords");
        System.out.println(existingPatron);
        Patron updatedPatron = patronRepository.save(existingPatron);
        return PatronDTOMapper.INSTANCE.toDTO(updatedPatron);
    }

    @Transactional
    public void deletePatron(Long id)
    {
        Optional<Patron> patronToDelete = patronRepository.findById(id);
        if (patronToDelete.isPresent()) {
            Patron thePatron = patronToDelete.get();
            thePatron.removeBorrowingRecordsAssociation();
            patronRepository.delete(thePatron);
        } else {
            throw new EntityNotFoundException("Patron with ID " + id + " not found");
        }
    }
}
