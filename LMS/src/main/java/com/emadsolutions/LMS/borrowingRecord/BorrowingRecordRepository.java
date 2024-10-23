package com.emadsolutions.LMS.borrowingRecord;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BorrowingRecordRepository extends JpaRepository<BorrowingRecord,Long> {

    Optional<BorrowingRecord> findByBook_IdAndPatron_Id(Long bookId, Long patronId);
}
