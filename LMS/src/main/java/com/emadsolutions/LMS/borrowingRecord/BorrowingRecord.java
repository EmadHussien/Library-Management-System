package com.emadsolutions.LMS.borrowingRecord;
import com.emadsolutions.LMS.book.Book;
import com.emadsolutions.LMS.patron.Patron;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDate;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class BorrowingRecord {

    @Id
    @GeneratedValue()
    private long id;

    @NotNull(message = "Borrowing date is required")
    private LocalDate borrowingDate;

    private LocalDate returnDate;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne(fetch = FetchType.EAGER,cascade = {
            CascadeType.DETACH , CascadeType.MERGE , CascadeType.PERSIST , CascadeType.REFRESH
    })
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @ManyToOne(fetch = FetchType.EAGER,cascade = {
            CascadeType.DETACH , CascadeType.MERGE , CascadeType.PERSIST , CascadeType.REFRESH
    })
    @JoinColumn(name = "patron_id", nullable = false)
    private Patron patron;




    public enum Status {

        BORROWED,
        RETURNED
         }



}
