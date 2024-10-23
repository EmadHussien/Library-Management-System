package com.emadsolutions.LMS.book;
import com.emadsolutions.LMS.DTOs.*;
import com.emadsolutions.LMS.Exceptions.ConflictException;
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
public class BookService {
    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public PaginatedListDTO<BookToDTO> getPaginatedListOfBooks  (int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Book> books = bookRepository.findAll(pageable);
        List<BookToDTO> bookDTOs = books.getContent().stream()
                .map(BookDTOMapper.INSTANCE::toDTO)
                .toList();

        Page<BookToDTO> bookDTOPage = new PageImpl<>(bookDTOs, pageable, books.getTotalElements());
        return new PaginatedListDTO<>(bookDTOPage);
    }
    public Book getSingleBook(Long id)
    {
        return bookRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Did not find book with id - " + id));

    }
    @Transactional
    public BookToDTO addNewBook(Book book)
    {
        Optional<Book> existingBook = bookRepository.findByIsbn(book.getIsbn());
        if (existingBook.isPresent())
        {
            throw new ConflictException("A book with the provided ISBN is already registered. Please check the ISBN or update the existing record.");
        }
        var savedBook = bookRepository.save(book);
        return BookDTOMapper.INSTANCE.toDTO(savedBook);
    }
    @Transactional
    public BookToDTO updateBook(Long id, UpdateBookToEntity book)
    {
        Book existingBook = getSingleBook(id);
        var bookUpdates = UpdateBookToEntityMapper.INSTANCE.toEntity(book);
        BeanUtils.copyProperties(bookUpdates, existingBook,"id","borrowingRecords","availableCopies");
        var updatedBook = bookRepository.save(existingBook);
        return BookDTOMapper.INSTANCE.toDTO(updatedBook);

    }
    @Transactional
    public void deleteBook(Long id)
    {
        Optional<Book> bookToDelete = bookRepository.findById(id);
        if (bookToDelete.isPresent()) {
            Book theBook = bookToDelete.get();
            theBook.removeBorrowingRecordsAssociation();
            bookRepository.delete(theBook);
        } else {
            throw new EntityNotFoundException("Book with ID " + id + " not found");
        }
    }

}
