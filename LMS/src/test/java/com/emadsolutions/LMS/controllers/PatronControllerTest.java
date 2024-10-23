package com.emadsolutions.LMS.controllers;

import com.emadsolutions.LMS.DTOs.*;
import com.emadsolutions.LMS.patron.Patron;
import com.emadsolutions.LMS.patron.PatronService;
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
public class PatronControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PatronService patronService; // Mocking the service layer


    @Test
    public void testAddNewPatron() throws Exception {
        // Arrange: Prepare the mock data
        Patron patron = new Patron(1L,"John Doe","01116446214","emad@gmail.com","cairo, Egypt",0,null); // Example patron
        PatronToDTO patronDTO = PatronDTOMapper.INSTANCE.toDTO(patron); // Expected return object
        CustomResponse<PatronToDTO> expectedResponse = new CustomResponse<>(
                CustomResponse.ResponseStatus.success, null, patronDTO
        );

        when(patronService.addNewPatron(any(Patron.class))).thenReturn(patronDTO);

        ObjectMapper objectMapper = new ObjectMapper();
        String patronJson = objectMapper.writeValueAsString(patron);

        mockMvc.perform(post("/patrons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patronJson)) // Sending the JSON in the body
                .andExpect(status().isCreated()) // Assert that the response status is 201 CREATED
                .andExpect(jsonPath("$.status").value("success")) // Check if the custom response has success
                .andExpect(jsonPath("$.data.name").value("John Doe")) // Check if the returned name is correct
                .andExpect(jsonPath("$.data.email").value("emad@gmail.com")); // Check if the returned age is correct
    }

    @Test
    public void testDeletePatron()throws Exception
    {
        // Mock the service behavior (so we don’t actually call the real service)
        Long patronId = 123L;

        doNothing().when(patronService).deletePatron(patronId);


        // Act: Send the POST request and capture the response
        mockMvc.perform(delete("/patrons/{patronId}",patronId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Assert that the response status is 201 CREATED
                .andExpect(jsonPath("$.status").value("success")) // Check if the custom response has success
                .andExpect(jsonPath("$.message").value("Patron with id: "+ patronId +" is deleted")); // Check if the returned name is correct
    }

    @Test
    public void testGetSinglePatron()throws Exception
    {
        var patron = new Patron();
        patron.setId(1L);
        patron.setName("Emad");

        when(patronService.getSinglePatron(1L)).thenReturn(patron);

        mockMvc.perform(get("/patrons/{patronId}",1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success")) // Check if the custom response has success
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.name").value("Emad"));
    }


    @Test
    public void testGetAllPatrons() throws Exception {
        // Arrange

        List<PatronToDTO> patrons = Arrays.asList(
                new PatronToDTO(1L, "Robert Martin", "01116446214", "em@gmail.com", "uae", 3),
                new PatronToDTO(2L, "Gang of Four",  "01556951342", "martin@gmail.com","usa", 2)
        );

        PaginatedListDTO<PatronToDTO> paginatedResponse = new PaginatedListDTO<>();
        paginatedResponse.setContent(patrons);
        paginatedResponse.setPageNumber(0);
        paginatedResponse.setPageSize(10);
        paginatedResponse.setTotalElements(25L); // Total 25 books in database
        paginatedResponse.setTotalPages(3);      // With page size 10, we get 3 pages
        paginatedResponse.setLast(false);        // Not the last page

        when(patronService.getPaginatedListOfPatrons(0, 10)).thenReturn(paginatedResponse);

        // Act & Assert
        mockMvc.perform(get("/patrons")
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
                // Verify first patron details
                .andExpect(jsonPath("$.data.content[0].id").value(1))
                .andExpect(jsonPath("$.data.content[0].name").value("Robert Martin"));
    }

    @Test
    public void testUpdatePatron() throws Exception {
        // Arrange: Prepare the mock data
        UpdatePatronToEntity patron = new UpdatePatronToEntity("John Doe", "01116446214", "emad@gmail.com", "cairo, Egypt");
        PatronToDTO thePatron = new PatronToDTO(1L, "John Doe", "01116446214", "emad@gmail.com", "cairo, Egypt", 0);

        // Mock the service behavior
        when(patronService.updatePatron(eq(1L), any(UpdatePatronToEntity.class))).thenReturn(thePatron);

        // Convert the Patron object into JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String patronJson = objectMapper.writeValueAsString(patron);

        // Act: Send the PUT request and capture the response
        mockMvc.perform(put("/patrons/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patronJson))
                .andExpect(status().isOk()) // Assert that the response status is 200
                .andExpect(jsonPath("$.status").value("success")) // Check if the response has success
                .andExpect(jsonPath("$.data.id").value(1L)) // Check if the data id matches
                .andExpect(jsonPath("$.data.name").value("John Doe")); // Additional checks as needed
    }

}
