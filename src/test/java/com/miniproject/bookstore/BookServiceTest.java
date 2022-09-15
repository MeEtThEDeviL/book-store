package com.miniproject.bookstore;

import com.miniproject.bookstore.business.BookService;
import com.miniproject.bookstore.data.Book;
import com.miniproject.bookstore.data.BookRepository;
import org.apache.catalina.LifecycleState;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    @DisplayName("Given null book to throws RuntimeException")
    public void testAddNullBook(){
        assertThrows(RuntimeException.class, () -> bookService.addBook(null));
        verify(bookRepository, times(0)).save(any());
    }

    @Test
    @DisplayName("Return list of books with stream:Engineering")
    public void testFilterBooks(){
        List<Book> ExpectedBookList = new ArrayList<>();
        ExpectedBookList.add(book1);
        ExpectedBookList.add(book2);

        when(bookRepository.filterBooksByStreamAuthorPublisherYearOfPublicationBookName(
                "Engineering", null, null, null, null)
        ).thenReturn(ExpectedBookList);

        List<Book> actualBookList = bookService.filterBooks("Engineering", null, null, null, null);

        assertIterableEquals(ExpectedBookList, actualBookList);
        verify(bookRepository, times(1)).filterBooksByStreamAuthorPublisherYearOfPublicationBookName(
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

        assertSame(book1, actualBook);

        verify(bookRepository, times(1)).findById(1L);
        verify(bookRepository, times(1)).save(book1);

    }


    @Test
    @DisplayName("Delete Book With Given BookId")
    public void testDeleteBookById(){


    }

}
