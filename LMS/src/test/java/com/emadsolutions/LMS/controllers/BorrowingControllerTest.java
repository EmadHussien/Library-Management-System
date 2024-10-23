package com.emadsolutions.LMS.controllers;

import com.emadsolutions.LMS.DTOs.BookToDTO;
import com.emadsolutions.LMS.DTOs.BorrowingToDTO;
import com.emadsolutions.LMS.DTOs.PatronToDTO;
import com.emadsolutions.LMS.Exceptions.BookNotAvailableException;
import com.emadsolutions.LMS.borrowingRecord.BorrowingRecord;
import com.emadsolutions.LMS.borrowingRecord.BorrowingRecordService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
public class BorrowingControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BorrowingRecordService borrowingRecordService;

    @Test
    public void borrowBook_ValidRequest_ShouldReturnSuccess() throws Exception {
        // Arrange
        BorrowingToDTO expectedResponse = new BorrowingToDTO();
        expectedResponse.setId(1L);
        expectedResponse.setBorrowingDate(LocalDate.now());
        expectedResponse.setReturnDate(LocalDate.now().plusDays(14));
        expectedResponse.setStatus(BorrowingRecord.Status.BORROWED);

        // Set book details
        BookToDTO book = new BookToDTO();
        book.setId(1L);
        expectedResponse.setBook(book);

        // Set patron details
        PatronToDTO patron = new PatronToDTO();
        patron.setId(1L);
        expectedResponse.setPatron(patron);

        when(borrowingRecordService.borrowBook(1L, 1L))
                .thenReturn(expectedResponse);

        // Act & Assert
        mockMvc.perform(post("/borrow/1/patron/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data.id").exists())
                .andExpect(jsonPath("$.data.borrowingDate").exists())
                .andExpect(jsonPath("$.data.status").value("BORROWED"));
    }

    @Test
    public void borrowBook_BookNotAvailable_ShouldReturnError() throws Exception {
        when(borrowingRecordService.borrowBook(1L, 1L))
                .thenThrow(new BookNotAvailableException("The book is not available for borrowing."));



        mockMvc.perform(post("/borrow/1/patron/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("The book is not available for borrowing."));
    }


    @Test
    public void returnBook_ValidRequest_ShouldReturnSuccess() throws Exception {
        // Arrange
        BorrowingToDTO expectedResponse = new BorrowingToDTO();
        expectedResponse.setId(1L);
        expectedResponse.setBorrowingDate(LocalDate.now().minusDays(7));
        expectedResponse.setReturnDate(LocalDate.now()); // Returned today
        expectedResponse.setStatus(BorrowingRecord.Status.RETURNED);

        BookToDTO book = new BookToDTO();
        book.setId(1L);
        expectedResponse.setBook(book);

        PatronToDTO patron = new PatronToDTO();
        patron.setId(1L);
        expectedResponse.setPatron(patron);

        when(borrowingRecordService.returnBook(1L, 1L))
                .thenReturn(expectedResponse);

        // Act & Assert
        mockMvc.perform(put("/return/1/patron/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data.id").exists())
                .andExpect(jsonPath("$.data.returnDate").exists())
                .andExpect(jsonPath("$.data.status").value("RETURNED"));
    }


    @Test
    public void returnBook_NoActiveBorrowing_ShouldReturnError() throws Exception {
        long bookId = 1L;
        long patronId = 1L;
        // Arrange
        when(borrowingRecordService.returnBook(1L, 1L))
                .thenThrow(new RuntimeException("Did not find borrow record with book id - "
                        +bookId+
                        " and patron id - "+patronId
                ));

        // Act & Assert
        mockMvc.perform(put("/return/1/patron/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Did not find borrow record with book id - "
                        +bookId+
                        " and patron id - "+patronId
                ));
    }

}