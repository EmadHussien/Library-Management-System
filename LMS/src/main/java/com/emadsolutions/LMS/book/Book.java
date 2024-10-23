package com.emadsolutions.LMS.book;
import com.emadsolutions.LMS.borrowingRecord.BorrowingRecord;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Book {

    @Id
    @GeneratedValue()
    private Long id;

    @NotBlank(message = "Title is mandatory")
    @Size(max = 100, message = "Title should not exceed 100 characters")
    private String title;

    @NotBlank(message = "Author is mandatory")
    @Size(max = 50, message = "Author's name should not exceed 50 characters")
    private String author;

    @NotNull(message = "Publication year is required")
    private Integer publicationYear;

    @NotEmpty(message = "ISBN is mandatory")
    @Size(max = 20, message = "ISBN should not exceed 20 characters")
    private String isbn;

    @NotNull(message = "Total number of copies is required")
    @Min(value = 1, message = "Total number of copies must be greater than 0")
    private Integer totalCopies;

    private int availableCopies;

    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY , cascade = {
            CascadeType.DETACH , CascadeType.MERGE , CascadeType.PERSIST , CascadeType.REFRESH
    })
    private List<BorrowingRecord> borrowingRecords;

    // Custom setter for totalCopies
    public void setTotalCopies(Integer totalCopies) {
        this.totalCopies = totalCopies;
        this.availableCopies = totalCopies;
    }

    public void addBorrowingRecord(BorrowingRecord borrowingRecord)
    {
        if(this.borrowingRecords == null)
        {
            this.borrowingRecords = new ArrayList<BorrowingRecord>();
        }
        borrowingRecords.add(borrowingRecord);
        borrowingRecord.setBook(this);
    }
    public void removeBorrowingRecordsAssociation()
    {
        if (borrowingRecords.isEmpty())
            return;

        for (BorrowingRecord record : this.borrowingRecords) {
            record.setBook(null);
        }
    }
}
