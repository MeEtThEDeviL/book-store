package com.miniproject.bookstore.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    @Query("SELECT book FROM Book book WHERE (:stream is null or book.stream = :stream) "
    + "and (:author is null or book.author = :author) "
    + "and (:publisher is null or book.publisher = :publisher) "
    + "and (:yearOfPublication is null or book.yearOfPublication = :yearOfPublication) "
    + "and (:bookName is null or book.bookName = :bookName) ")
    List<Book> filterBooksByParameters(
            @Param("stream")String stream,
            @Param("author")String author,
            @Param("publisher")String publisher,
            @Param("yearOfPublication")Long yearOfPublication,
            @Param("bookName")String bookName
    );



   /*@Query("SELECT book FROM Book book WHERE ((:bookDetails).stream is null or book.stream = (:bookDetails).stream) "
            + "and ((:bookDetails).author is null or book.author = (:bookDetails).author) "
            + "and ((:bookDetails).publisher is null or book.publisher = (:bookDetails).publisher) "
            + "and ((:bookDetails).yearOfPublication = -1L or book.yearOfPublication = (:bookDetails).yearOfPublication) "
            + "and ((:bookDetails).bookName is null or book.bookName = (:bookDetails).bookName) ")
    List<Book> filterBooksByBookDetails(
            @Param("bookDetails")Book bookDetails
    );*/

}
