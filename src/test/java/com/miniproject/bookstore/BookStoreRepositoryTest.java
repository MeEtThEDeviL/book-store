package com.miniproject.bookstore;

import com.miniproject.bookstore.data.Book;
import com.miniproject.bookstore.data.BookRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumingThat;

// This integrates the Spring test context framework into the JUnit 5 Jupiter programming model
@ExtendWith(SpringExtension.class)
@DataJpaTest // only load the Spring Data JPA slice of the Spring context
public class BookStoreRepositoryTest {

    @Autowired
    private BookRepository bookRepository;
    private Book book1;
    private Book book2;
    private Book book3;



    @BeforeEach
    public void setupBooks(){
        book1 = new Book();
        book2 = new Book();
        book3 = new Book();

        //book1.setBookId(11);
        book1.setBookName("Watchdog: kraken");
        book1.setStream("Engineering");
        book1.setAuthor("DedSec");
        book1.setPublisher("Ubisoft");
        book1.setYearOfPublication(2020);

        //book2.setBookId(17);
        book2.setBookName("Assassin's Creed: Mirage");
        book2.setStream("Philosophy");
        book2.setAuthor("Adam");
        book2.setPublisher("Cry-Engine");
        book2.setYearOfPublication(2020);

        //book3.setBookId(24);
        book3.setBookName("Battle Field");
        book3.setStream("Engineering");
        book3.setAuthor("Jack Reacher");
        book3.setPublisher("Activision");
        book3.setYearOfPublication(2014);
    }

    @AfterEach
    public void tearDown(){
        bookRepository.deleteAll();
    }

    @Test
    @DisplayName("Saving book")
    public void testBookSave(){
        Book bookResult = bookRepository.save(book1);
        // As bookId is generated value
        book1.setBookId(bookResult.getBookId());
        assertSame(book1, bookResult);
        assertEquals(1, bookRepository.count());


    }

    @Test
    @DisplayName("Find book by present BookId")
    public void testFindByIdPresent(){
        Book bookUploaded  = bookRepository.save(book1);
        bookRepository.save(book2);
        bookRepository.save(book3);

        // Optional<T>.isPresent() - returns true if value is present
        // Optional<T>.get() - return value
        Optional<Book> bookResult = bookRepository.findById(bookUploaded.getBookId());
        assertTrue(bookResult.isPresent());
        assumingThat(bookResult.isPresent(), () -> {
            book1.setBookId(bookResult.get().getBookId());
            assertSame(bookUploaded, bookResult.get());
        });
    }

    @Test
    @DisplayName("Find book by Absent BookId")
    public void testFindByIdAbsent(){
        bookRepository.save(book1);
        bookRepository.save(book2);
        bookRepository.save(book3);

        // Optional<T>.isPresent() - returns true if value is present
        // Optional<T>.get() - return value
        Optional<Book> bookResult = bookRepository.findById(124L);
        assertFalse(bookResult.isPresent());
        assertThrows(NoSuchElementException.class, () -> bookResult.get());
    }

    @Test
    @DisplayName("Delete book by BookId")
    public void testDeleteById(){
        Book bookUploaded = bookRepository.save(book1);

        bookRepository.deleteById(bookUploaded.getBookId());
        Optional<Book> bookResult = bookRepository.findById(bookUploaded.getBookId());
        assertFalse(bookResult.isPresent());
        assertEquals(0, bookRepository.count());

    }

    @Test
    @DisplayName("Filter books by stream Engineering")
    public void testFilterBooksByStream(){
        Book bookResult1 = bookRepository.save(book1);
        bookRepository.save(book2);
        Book bookResult3 = bookRepository.save(book3);

        List<Book> expectedBooks = new ArrayList<>();
        expectedBooks.add(bookResult1);
        expectedBooks.add(bookResult3);

        Iterable<Book> actualBooks = bookRepository.filterBooksByStreamAuthorPublisherYearOfPublicationBookName(
                "Engineering", null,null,null,null
        );

        assertIterableEquals(expectedBooks, actualBooks);

    }

    @Test
    @DisplayName("Filter books by yearOfPublication 2020")
    public void testFilterBooksByYearOfPublication(){
        Book bookResult1 = bookRepository.save(book1);
        Book bookResult2 = bookRepository.save(book2);
        bookRepository.save(book3);

        List<Book> expectedBooks = new ArrayList<>();
        expectedBooks.add(bookResult1);
        expectedBooks.add(bookResult2);

        Iterable<Book> actualBooks = bookRepository.filterBooksByStreamAuthorPublisherYearOfPublicationBookName(
                null, null,null, Long.valueOf(2020),null
        );

        assertIterableEquals(expectedBooks, actualBooks);

    }
}
