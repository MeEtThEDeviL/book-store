package com.miniproject.bookstore;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.miniproject.bookstore.business.BookService;
import com.miniproject.bookstore.data.Book;
import com.miniproject.bookstore.error.ErrorMessage;
import com.miniproject.bookstore.error.exceptions.EntityNotFoundException;
import com.miniproject.bookstore.webservice.WebServiceController;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class WebServiceControllerTest {

    @Mock
    private BookService bookService;

    private Book book1;
    private Book book2;
    private Book book3;
    private ObjectMapper mapper;

    private ErrorMessage errorMessage;

    @InjectMocks
    private WebServiceController webServiceController;

    @Autowired
    private MockMvc mockMvc; // To send HTTP requests to the controller

    @BeforeEach
    public void setupBooks(){
        book1 = new Book();
        book2 = new Book();
        book3 = new Book();

        mapper = new ObjectMapper();

        mockMvc = MockMvcBuilders.standaloneSetup(webServiceController).build();

        errorMessage = new ErrorMessage();

        book1.setBookId(1);
        book1.setBookName("Watchdog: kraken");
        book1.setStream("Engineering");
        book1.setAuthor("DedSec");
        book1.setPublisher("Ubisoft");
        book1.setYearOfPublication(2020);

        book2.setBookId(2);
        book2.setBookName("Assassin's Creed: Mirage");
        book2.setStream("Philosophy");
        book2.setAuthor("Adam");
        book2.setPublisher("Cry-Engine");
        book2.setYearOfPublication(2020);

        book3.setBookId(3);
        book3.setBookName("Battle Field");
        book3.setStream("Engineering");
        book3.setAuthor("Jack Reacher");
        book3.setPublisher("Activision");
        book3.setYearOfPublication(2014);
    }

    @AfterEach
    public void tearDown(){
        book1 = book2 = book3 = null;
    }

    @Test
    @DisplayName("Test POST mapping saving book")
    public void testPostMappingSaveBook() throws Exception {
        String json = "{}";

        when(bookService.addBook(any())).thenReturn(book1);

        try{
            json = mapper.writeValueAsString(book1);
        } catch (JsonProcessingException e){
            e.printStackTrace();
        }
        mockMvc.perform(post("/api/book").
                contentType(MediaType.APPLICATION_JSON).
                content(json)).
                andExpect(status().isCreated()).
                andExpect(content().contentType(MediaType.APPLICATION_JSON)).
                andExpect(jsonPath("$.bookName").value(book1.getBookName())).
                andExpect(jsonPath("$.stream").value(book1.getStream())).
                andExpect(jsonPath("$.author").value(book1.getAuthor())).
                andExpect(jsonPath("$.publisher").value(book1.getPublisher())).
                andExpect(jsonPath("$.yearOfPublication").value(book1.getYearOfPublication())).
        andDo(MockMvcResultHandlers.print());

        verify(bookService, times(1)).addBook(any());

    }

    @Test
    @DisplayName("Test getting list of all books")
    public void testGetMappingFilterAllBooks() throws  Exception{
        List<Book> bookList = new ArrayList<>();
        bookList.add(book1);
        bookList.add(book2);
        bookList.add(book3);

        when(bookService.filterBooks(null, null, null,null, null)).
                thenReturn(bookList);

        mockMvc.perform(get("/api/book"))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$[0].bookName").value(book1.getBookName()))
                        .andExpect(jsonPath("$[0].author").value(book1.getAuthor()))
                        .andExpect(jsonPath("$[2].bookName").value(book3.getBookName()))
                        .andExpect(jsonPath("$[2].author").value(book3.getAuthor()));

        verify(bookService, times(1)).filterBooks(null,null,null,null,null);
    }

    @Test
    @DisplayName("Test getting list of books by stream:Engineering")
    public void testGetMappingFilterBookByStream() throws  Exception{
        List<Book> bookList = new ArrayList<>();
        bookList.add(book1);
        bookList.add(book3);

        String stream = "Engineering";

        when(bookService.filterBooks(stream, null, null,null, null)).
                thenReturn(bookList);

        mockMvc.perform(get("/api/book")
                        .param("stream", stream))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$[0].bookName").value(book1.getBookName()))
                    .andExpect(jsonPath("$[0].author").value(book1.getAuthor()))
                    .andExpect(jsonPath("$[1].bookName").value(book3.getBookName()))
                    .andExpect(jsonPath("$[1].author").value(book3.getAuthor()));

        verify(bookService, times(1)).filterBooks(stream,null,null,null,null);
    }

    @Test
    @DisplayName("Test PUT mapping to update book by ID")
    public void testPutMappingUpdateBookDetails() throws Exception{
        String author = "Assassins";
        Book bookDetails = new Book();
        bookDetails.setAuthor(author);

        book1.setAuthor(author);

        String jsonString = "{}";
        try{
            jsonString = mapper.writeValueAsString(bookDetails);
        }catch(JsonProcessingException e){
            e.printStackTrace();
        }

        when(bookService.updateBookDetails(anyLong(), any(Book.class))).thenReturn(book1);

        mockMvc.perform(put("/api/book/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonString))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.bookName").value(book1.getBookName()))
                .andExpect(jsonPath("$.publisher").value(book1.getPublisher()))
                .andExpect(jsonPath("$.author").value(author));
        verify(bookService, times(1)).updateBookDetails(anyLong(), any(Book.class));
    }

    @Test
    @DisplayName("Test DELETE mapping to delete book by present bookId")
    public void testDeleteMappingDeleteBookById() throws Exception{
        when(bookService.deleteBookById(anyLong())).thenReturn(book1);

        mockMvc.perform(delete("/api/book/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookName").value(book1.getBookName()))
                .andExpect(jsonPath("$.author").value(book1.getAuthor()))
                .andDo(MockMvcResultHandlers.print());

        verify(bookService, times(1)).deleteBookById(anyLong());
    }

    /*
    Write tests:
    -post mapping:
        - book name null
        - year of publication -ve
        - year of publication > current year
        - add same book again
        */

/*
     -put mapping:
        - invalid year of publication
        - absent book id

     -delete mapping:
        - absent book id
     */

}
