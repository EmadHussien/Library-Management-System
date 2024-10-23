package com.emadsolutions.LMS;

import com.emadsolutions.LMS.admin.Admin;
import com.emadsolutions.LMS.admin.AdminRepository;
import com.emadsolutions.LMS.book.Book;
import com.emadsolutions.LMS.book.BookRepository;
import com.emadsolutions.LMS.patron.Patron;
import com.emadsolutions.LMS.patron.PatronRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DataInit {

    private final PatronRepository patronRepository;
    private final BookRepository bookRepository;
    private final AdminRepository adminRepository;

    @Autowired
    public DataInit(PatronRepository patronRepository, BookRepository bookRepository, AdminRepository adminRepository) {
        this.patronRepository = patronRepository;
        this.bookRepository = bookRepository;
        this.adminRepository = adminRepository;
    }

    @PostConstruct
    public void initData() {

        // Initialize Patrons
        Patron patron1 = new Patron(1L, "Alice Johnson", "01012345678", "alice.johnson@example.com", "789 Oak St",0, null);
        Patron patron2 = new Patron(2L, "Bob Brown", "01523456789", "bob.brown@example.com", "321 Pine St",0, null);
        Patron patron3 = new Patron(3L, "Charlie Davis", "01234567890", "charlie.davis@example.com", "654 Maple St",0, null);
        Patron patron4 = new Patron(4L, "Diana White", "01145678901", "diana.white@example.com", "987 Cedar St",0, null);
        Patron patron5 = new Patron(5L, "Edward Green", "01556789012", "edward.green@example.com", "159 Birch St",0, null);

        // Save Patrons
        patronRepository.save(patron1);
        patronRepository.save(patron2);
        patronRepository.save(patron3);
        patronRepository.save(patron4);
        patronRepository.save(patron5);

        // Initialize Books
        Book book1 = new Book(1L,"The Great Gatsby","F. Scott Fitzgerald",1925,"9780743273565",10,10,null);
        Book book2 = new Book(2L, "1984", "George Orwell", 1949, "9780451524935", 15, 15, null);
        Book book3 = new Book(3L, "To Kill a Mockingbird", "Harper Lee", 1960, "9780061120084", 20, 20, null);
        Book book4 = new Book(4L, "Pride and Prejudice", "Jane Austen", 1813, "9780141439518", 12, 12, null);
        Book book5 = new Book(5L, "The Catcher in the Rye", "J.D. Salinger", 1951, "9780316769488", 8, 8, null);

        // Save Books
        bookRepository.save(book1);
        bookRepository.save(book2);
        bookRepository.save(book3);
        bookRepository.save(book4);
        bookRepository.save(book5);

        System.out.println("Dummy data initialized.");
    }
}