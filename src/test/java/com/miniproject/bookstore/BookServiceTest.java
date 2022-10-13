package com.miniproject.bookstore;

import com.miniproject.bookstore.business.BookService;
import com.miniproject.bookstore.data.Book;
import com.miniproject.bookstore.data.BookRepository;
import com.miniproject.bookstore.error.exceptions.EntityAlreadyExistsException;
import com.miniproject.bookstore.error.exceptions.EntityNotFoundException;
import com.miniproject.bookstore.error.exceptions.InvalidEntityException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Initializes mocks in test classes
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Autowired
    @InjectMocks
    private BookService bookService;
    private Book book1;
    private Book book2;
    private Book book3;

    @BeforeEach
    public void setupBooks(){
        book1 = new Book();
        book2 = new Book();
        book3 = new Book();

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
    @DisplayName("Given book to add returns added book")
    public void testAddBook(){
        when(bookRepository.save(book1)).thenReturn(book1);
        Book bookResult = bookService.addBook(book1);
        assertEquals(book1, bookResult);
        verify(bookRepository, times(1)).save(any());
    }



    @Test
    @DisplayName("Inserting same book again throws EntityAlreadyExistsException")
    public void testAddSameBook(){
        List<Book> filteredBooks = new ArrayList<>();
        filteredBooks.add(book1);
        when(bookRepository.filterBooksByParameters(
                book1.getStream(), book1.getAuthor(), book1.getPublisher(), book1.getYearOfPublication(), book1.getBookName())
        ).thenReturn(filteredBooks);


        assertThrows(EntityAlreadyExistsException.class, () -> bookService.addBook(book1));
    }

    @Test
    @DisplayName("Given book without bookName throws InvalidEntityException")
    public void testAddBookWithNullBookName(){
        book1.setBookName(null);
        assertThrows(InvalidEntityException.class, () -> bookService.addBook(book1));
    }

    @Test
    @DisplayName("Adding book with -Ve yearOfPublication throws InvalidEntityException")
    public void testAddBookWithNegativeYearOfPublication(){
        book1.setYearOfPublication(-1);
        assertThrows(InvalidEntityException.class, () -> bookService.addBook(book1));
    }

    @Test
    @DisplayName("Adding book with greater than current year yearOfPublication throws InvalidEntityException")
    public void testAddBookWithGreaterThanCurrentYearYearOfPublication(){
        book1.setYearOfPublication(3000);
        assertThrows(InvalidEntityException.class, () -> bookService.addBook(book1));
    }

    @Test
    @DisplayName("Return list of books with stream:Engineering")
    public void testFilterBooks(){
        List<Book> ExpectedBookList = new ArrayList<>();
        ExpectedBookList.add(book1);
        ExpectedBookList.add(book2);

        when(bookRepository.filterBooksByParameters(
                "Engineering", null, null, null, null)
        ).thenReturn(ExpectedBookList);

        List<Book> actualBookList = bookService.filterBooks("Engineering", null, null, null, null);

        assertIterableEquals(ExpectedBookList, actualBookList);
        verify(bookRepository, times(1)).filterBooksByParameters(
                "Engineering", null, null, null, null
        );
    }

    @Test
    @DisplayName("Given bookId and updateParamBook update the book")
    public void testUpdateBookDetails(){
        Optional<Book> bookReturned = Optional.of(book1);
        when(bookRepository.findById(1L)).thenReturn(bookReturned);

        String author = "Assassins";
        Book updateParamBook = new Book();
        updateParamBook.setAuthor(author);

        book1.setAuthor(author);
        when(bookRepository.save(book1)).thenReturn(book1);

        Book actualBook = bookService.updateBookDetails(1L, updateParamBook);

        assertEquals(book1, actualBook);

        verify(bookRepository, times(1)).findById(1L);
        verify(bookRepository, times(1)).save(book1);

    }

    @Test
    @DisplayName("Delete book by present bookId returns deleted book")
    public void testDeleteBookById(){
        Optional<Book> bookReturned = Optional.of(book1);

        when(bookRepository.findById(book1.getBookId())).thenReturn(bookReturned);

        Book actualBook = bookService.deleteBookById(book1.getBookId());

        assertEquals(actualBook, book1);

        verify(bookRepository, times(1)).findById(book1.getBookId());
    }

    @Test
    @DisplayName("Delete book by absent bookId throws EntityNotFoundException")
    public void testDeleteBookByAbsentBookId(){
        Optional<Book> emptyOptional = Optional.empty();

        when(bookRepository.findById(anyLong())).thenReturn(emptyOptional);

        assertThrows(EntityNotFoundException.class, () -> bookService.deleteBookById(127L));

        verify(bookRepository, times(1)).findById(anyLong());
    }


}
