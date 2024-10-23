package com.emadsolutions.LMS.borrowingRecord;

import com.emadsolutions.LMS.DTOs.BorrowingToDTO;
import com.emadsolutions.LMS.Exceptions.BookNotAvailableException;
import com.emadsolutions.LMS.Exceptions.BorrowLimitExceededException;
import com.emadsolutions.LMS.Exceptions.ConflictException;
import com.emadsolutions.LMS.book.Book;
import com.emadsolutions.LMS.book.BookService;
import com.emadsolutions.LMS.patron.Patron;
import com.emadsolutions.LMS.patron.PatronService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class BorrowingRecordService {
    private final BorrowingRecordRepository borrowingRecordRepository;
    private final BookService bookService;
    private final PatronService patronService;

    @Autowired
    public BorrowingRecordService(BorrowingRecordRepository borrowingRecordRepository, BookService bookService, PatronService patronService) {
        this.borrowingRecordRepository = borrowingRecordRepository;
        this.bookService = bookService;
        this.patronService = patronService;
    }

    public BorrowingRecord getByBookIdAndPatronId(Long bookId, Long patronId)
    {
        return borrowingRecordRepository.findByBook_IdAndPatron_Id(bookId,patronId)
                .orElseThrow(() -> new RuntimeException("Did not find borrow record with book id - "
                        +bookId+
                        " and patron id - "+patronId
                ));
    }
    public BorrowingToDTO borrowBook(Long bookId,Long patronId)
    {
        int MAX_BORROW_LIMIT = 5;

        Book theBook = bookService.getSingleBook(bookId);
        Patron thePatron = patronService.getSinglePatron(patronId);
        Optional<BorrowingRecord> theRecord = borrowingRecordRepository.findByBook_IdAndPatron_Id(bookId,patronId);
        if(theRecord.isPresent())
        {
            var record = theRecord.get();
            if(record.getStatus() == BorrowingRecord.Status.BORROWED)
            {
                throw new ConflictException("Patron already have borrowed this book and should return");
            }
        }

        if(theBook.getAvailableCopies() < 1 )
        {
            throw new BookNotAvailableException("The book is not available for borrowing.");

        }
        if(thePatron.getNumOfBorrowedBooks() >= MAX_BORROW_LIMIT)
        {
            throw new BorrowLimitExceededException("The patron has exceeded the borrowing limit.");
        }
        theBook.setAvailableCopies(theBook.getAvailableCopies() - 1);
        thePatron.setNumOfBorrowedBooks(thePatron.getNumOfBorrowedBooks() + 1);

        BorrowingRecord borrowingRecord = new BorrowingRecord();
        borrowingRecord.setBorrowingDate(LocalDate.now());
        borrowingRecord.setStatus(BorrowingRecord.Status.BORROWED);

        // manage the bidirectional relation
        theBook.addBorrowingRecord(borrowingRecord);
        thePatron.addBorrowingRecord(borrowingRecord);

        BorrowingRecord borrowedRecord = borrowingRecordRepository.save(borrowingRecord);
        return new BorrowingToDTO(borrowedRecord);
    }

    public BorrowingToDTO returnBook(Long bookId,Long patronId)
    {
        Book theBook = bookService.getSingleBook(bookId);
        Patron thePatron = patronService.getSinglePatron(patronId);
        BorrowingRecord theRecord = getByBookIdAndPatronId(bookId,patronId);

        theBook.setAvailableCopies(theBook.getAvailableCopies() + 1);
        thePatron.setNumOfBorrowedBooks(thePatron.getNumOfBorrowedBooks() - 1);
        theRecord.setStatus(BorrowingRecord.Status.RETURNED);
        theRecord.setReturnDate(LocalDate.now());

        BorrowingRecord returnedRecord = borrowingRecordRepository.save(theRecord);
        return new BorrowingToDTO(returnedRecord);
      }
}
