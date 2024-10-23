package com.emadsolutions.LMS.book;

import com.emadsolutions.LMS.DTOs.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class BookController {
    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/books")
    public ResponseEntity<?> getPaginatedListOfBooks
            (
                    @RequestParam(defaultValue = "0") int pageNumber,
                    @RequestParam(defaultValue = "10") int pageSize
            ) {

        PaginatedListDTO<BookToDTO> listOfBooks = bookService.getPaginatedListOfBooks(
                pageNumber
                , pageSize
        );
        return ResponseEntity.ok(new CustomResponse<>(CustomResponse.ResponseStatus.success, null, listOfBooks));

    }
    @GetMapping("/books/{id}")
    public ResponseEntity<?> getSingleBook(@PathVariable Long id) {
        Book foundBook = bookService.getSingleBook(id);
        BookToDTO bookDTO = BookDTOMapper.INSTANCE.toDTO(foundBook);
        return ResponseEntity.ok(new CustomResponse<>(CustomResponse.ResponseStatus.success, null, bookDTO));
    }
    @PostMapping("/books")
    public ResponseEntity<?> addNewBook(@Valid @RequestBody Book book) {
        BookToDTO theBook = bookService.addNewBook(book);
        var response = new CustomResponse<>(CustomResponse.ResponseStatus.success,null,theBook);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @PutMapping("/books/{id}")
    public ResponseEntity<?> updateBook(@PathVariable Long id, @Valid @RequestBody UpdateBookToEntity book)
    {
        BookToDTO theBook = bookService.updateBook(id, book);
        return ResponseEntity.ok(new CustomResponse<>(CustomResponse.ResponseStatus.success, null, theBook));
    }

    @DeleteMapping("/books/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable Long id)
    {
        bookService.deleteBook(id);
        return ResponseEntity.ok(new CustomResponse<>(CustomResponse.ResponseStatus.success,
                "Book with id: "+ id +" is deleted"));
    }
}
