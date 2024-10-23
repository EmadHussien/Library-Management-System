package com.emadsolutions.LMS.patron;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

public interface PatronRepository extends JpaRepository<Patron,Long> {
    @NonNull
    Page<Patron> findAll(@NonNull Pageable pageable);
}
