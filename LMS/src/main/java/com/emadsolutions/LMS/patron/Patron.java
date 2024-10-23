    package com.emadsolutions.LMS.patron;

    import com.emadsolutions.LMS.borrowingRecord.BorrowingRecord;
    import jakarta.persistence.*;
    import jakarta.validation.constraints.Email;
    import jakarta.validation.constraints.NotBlank;
    import jakarta.validation.constraints.Pattern;
    import jakarta.validation.constraints.Size;
    import lombok.*;

    import java.util.ArrayList;
    import java.util.List;

    @Setter
    @Getter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @Entity
    public class Patron{
        @Id
        @GeneratedValue()
        private Long id;

        @NotBlank(message = "Name is mandatory")
        @Size(max = 50, message = "Name should not exceed 50 characters")
        private String name;

        @Pattern(regexp = "^(010|011|015|012)\\d{8}$", message = "Phone number must start with 010, 011, 015, or 012 and be 11 digits long")
        private String phoneNumber;

        @Email(message = "Email should be valid ex: example@example.com")
        @NotBlank(message = "Email is mandatory")
        private String email;

        private String address;

        private int numOfBorrowedBooks;

        @OneToMany(mappedBy = "patron", fetch = FetchType.LAZY , cascade = {
                CascadeType.DETACH , CascadeType.MERGE , CascadeType.PERSIST , CascadeType.REFRESH
        })
        private List<BorrowingRecord> borrowingRecords;

        public void addBorrowingRecord(BorrowingRecord borrowingRecord)
        {
            if(this.borrowingRecords == null)
            {
                this.borrowingRecords = new ArrayList<BorrowingRecord>();
            }
            borrowingRecords.add(borrowingRecord);
            borrowingRecord.setPatron(this);
        }

        public void removeBorrowingRecordsAssociation()
        {
            if (borrowingRecords.isEmpty())
                return;

            for (BorrowingRecord record : this.borrowingRecords) {
                record.setPatron(null);
            }
        }
    }
