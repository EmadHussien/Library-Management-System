package com.emadsolutions.LMS.DTOs;

import com.emadsolutions.LMS.book.Book;
import com.emadsolutions.LMS.borrowingRecord.BorrowingRecord;
import com.emadsolutions.LMS.patron.Patron;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class BorrowingToDTO{
    private long id;
    private LocalDate borrowingDate;
    private LocalDate returnDate;
    private BorrowingRecord.Status status;
    private BookToDTO book;
    private PatronToDTO patron;

    public BorrowingToDTO(BorrowingRecord borrowingRecord) {
        this.id = borrowingRecord.getId();
        this.borrowingDate = borrowingRecord.getBorrowingDate();
        this.returnDate = borrowingRecord.getReturnDate();
        this.status = borrowingRecord.getStatus();
        this.book = BookDTOMapper.INSTANCE.toDTO(borrowingRecord.getBook());
        this.patron = PatronDTOMapper.INSTANCE.toDTO(borrowingRecord.getPatron());
    }
}
