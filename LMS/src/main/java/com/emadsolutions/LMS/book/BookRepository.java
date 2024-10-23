package com.emadsolutions.LMS.book;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book,Long> {
    @NonNull
    Page<Book> findAll(@NonNull Pageable pageable);

    Optional<Book> findByIsbn(String isbn);
}
