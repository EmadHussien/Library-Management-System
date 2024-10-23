package com.emadsolutions.LMS.controllers;
import com.emadsolutions.LMS.DTOs.BookToDTO;
import com.emadsolutions.LMS.DTOs.CustomResponse;
import com.emadsolutions.LMS.DTOs.PaginatedListDTO;
import com.emadsolutions.LMS.DTOs.UpdateBookToEntity;
import com.emadsolutions.LMS.book.Book;
import com.emadsolutions.LMS.book.BookService;
import com.emadsolutions.LMS.security.JwtTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
public class BookControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private BookService bookService; // Mocking the service layer


    @Test
    public void testAddNewBook() throws Exception {
        // Arrange: Prepare the mock data
        Book book = new Book(1L, "Effective Java", "Joshua Bloch", 2008, "12345612345612345123",10,10,null);
        BookToDTO bookDTO = new BookToDTO(1L, "Effective Java", "Joshua Bloch", 2008,  "12345612345612345123",10,10); // Expected return object
        CustomResponse<BookToDTO> expectedResponse = new CustomResponse<>(
                CustomResponse.ResponseStatus.success, null, bookDTO
        );

        // Mock the service behavior (so we don’t actually call the real service)
        when(bookService.addNewBook(any(Book.class))).thenReturn(bookDTO);

        // Convert the Book object into JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String bookJson = objectMapper.writeValueAsString(book);

        // Act: Send the POST request and capture the response
        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson)) // Sending the JSON in the body
                .andExpect(status().isCreated()) // Assert that the response status is 201 CREATED
                .andExpect(jsonPath("$.status").value("success")) // Check if the custom response has success
                .andExpect(jsonPath("$.data.title").value("Effective Java")) // Check if the returned title is correct
                .andExpect(jsonPath("$.data.author").value("Joshua Bloch")); // Check if the returned author is correct
    }

    @Test
    public void testDeleteBook() throws Exception {
        // Mock the service behavior
        Long bookId = 123L;
        doNothing().when(bookService).deleteBook(bookId);

        // Act: Send the DELETE request and capture the response
        mockMvc.perform(delete("/books/{id}", bookId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Assert that the response status is 200 OK
                .andExpect(jsonPath("$.status").value("success")) // Check if the custom response has success
                .andExpect(jsonPath("$.message").value("Book with id: " + bookId + " is deleted"));
    }

    @Test
    public void testGetSingleBook() throws Exception {
        // Arrange: Mock the book object
        Book book = new Book(1L, "Effective Java", "Joshua Bloch", 2008, "12345612345612345123",10,10,null);
        BookToDTO bookDTO = new BookToDTO(1L, "Effective Java", "Joshua Bloch", 2008,  "12345612345612345123",10,10); // Expected return object
        when(bookService.getSingleBook(1L)).thenReturn(book);

        // Act: Send the GET request and capture the response
        mockMvc.perform(get("/books/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success")) // Check if the response has success
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.title").value("Effective Java"))
                .andExpect(jsonPath("$.data.author").value("Joshua Bloch"));
    }

    @Test
    public void tesGetAllBooks() throws Exception {
        // Arrange
        List<BookToDTO> books = Arrays.asList(
                new BookToDTO(1L, "Clean Code", "Robert Martin", 2008, "978-0132350884", 5, 3),
                new BookToDTO(2L, "Design Patterns", "Gang of Four", 1994, "978-0201633610", 4, 2)
        );

        PaginatedListDTO<BookToDTO> paginatedResponse = new PaginatedListDTO<>();
        paginatedResponse.setContent(books);
        paginatedResponse.setPageNumber(0);
        paginatedResponse.setPageSize(10);
        paginatedResponse.setTotalElements(25L); // Total 25 books in database
        paginatedResponse.setTotalPages(3);      // With page size 10, we get 3 pages
        paginatedResponse.setLast(false);        // Not the last page

        when(bookService.getPaginatedListOfBooks(0, 10)).thenReturn(paginatedResponse);

        // Act & Assert
        mockMvc.perform(get("/books")
                        .param("pageNumber", "0")
                        .param("pageSize", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.content.length()").value(2))
                .andExpect(jsonPath("$.data.pageNumber").value(0))
                .andExpect(jsonPath("$.data.pageSize").value(10))
                .andExpect(jsonPath("$.data.totalElements").value(25))
                .andExpect(jsonPath("$.data.totalPages").value(3))
                .andExpect(jsonPath("$.data.last").value(false))
                // Verify first book details
                .andExpect(jsonPath("$.data.content[0].id").value(1))
                .andExpect(jsonPath("$.data.content[0].title").value("Clean Code"))
                .andExpect(jsonPath("$.data.content[0].author").value("Robert Martin"))
                .andExpect(jsonPath("$.data.content[0].publicationYear").value(2008))
                .andExpect(jsonPath("$.data.content[0].isbn").value("978-0132350884"))
                .andExpect(jsonPath("$.data.content[0].totalCopies").value(5))
                .andExpect(jsonPath("$.data.content[0].availableCopies").value(3));
    }

    @Test
    public void testUpdateBook() throws Exception {
        // Arrange: Prepare the mock data for update
        UpdateBookToEntity bookUpdate = new UpdateBookToEntity("Effective Java Updated", "Joshua Bloch", 2008, "12345612345612345123",10);
        BookToDTO updatedBook = new BookToDTO(1L, "Effective Java Updated", "Joshua Bloch", 2008,  "12345612345612345123",10,10); // Expected return object

        // Mock the service behavior
        when(bookService.updateBook(eq(1L), any(UpdateBookToEntity.class))).thenReturn(updatedBook);

        // Convert the book update object into JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String bookUpdateJson = objectMapper.writeValueAsString(bookUpdate);

        // Act: Send the PUT request and capture the response
        mockMvc.perform(put("/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookUpdateJson))
                .andExpect(status().isOk()) // Assert that the response status is 200 OK
                .andExpect(jsonPath("$.status").value("success")) // Check if the response has success
                .andExpect(jsonPath("$.data.id").value(1L)) // Verify the updated book's ID
                .andExpect(jsonPath("$.data.title").value("Effective Java Updated")); // Check if the title is updated
    }

}

