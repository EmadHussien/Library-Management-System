package com.emadsolutions.LMS.borrowingRecord;

import com.emadsolutions.LMS.DTOs.BorrowingToDTO;
import com.emadsolutions.LMS.DTOs.CustomResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BorrowingRecordController {
    private final BorrowingRecordService borrowingRecordService;
    @Autowired
    public BorrowingRecordController(BorrowingRecordService borrowingRecordService) {
        this.borrowingRecordService = borrowingRecordService;
    }

    @PostMapping("/borrow/{bookId}/patron/{patronId}")
    public ResponseEntity<?> borrowBook(@PathVariable Long bookId ,@PathVariable Long patronId)
    {
        BorrowingToDTO borrowingToDTO = borrowingRecordService.borrowBook(bookId,patronId);
        return ResponseEntity.ok(new CustomResponse<>(CustomResponse.ResponseStatus.success, null, borrowingToDTO));
    }
    @PutMapping("/return/{bookId}/patron/{patronId}")
    public ResponseEntity<?> returnBook(@PathVariable Long bookId ,@PathVariable Long patronId)
    {
        BorrowingToDTO returnedToDTO = borrowingRecordService.returnBook(bookId,patronId);
        return ResponseEntity.ok(new CustomResponse<>(CustomResponse.ResponseStatus.success, null, returnedToDTO));
    }
}
