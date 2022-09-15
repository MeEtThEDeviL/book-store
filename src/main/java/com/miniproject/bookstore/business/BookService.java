package com.miniproject.bookstore.business;

import com.miniproject.bookstore.data.Book;
import com.miniproject.bookstore.data.BookRepository;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;


@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository){
        this.bookRepository = bookRepository;
    }

    public Book addBook(Book book){
        if(null == book){
            throw new RuntimeException("Book cannot be null");
        }
        return this.bookRepository.save(book);

    }

    public List<Book> filterBooks( String stream, String author, String publisher, Long yearOfPublication, String bookName){

        List<Book> filteredBooks = this.bookRepository.filterBooksByStreamAuthorPublisherYearOfPublicationBookName(
                stream, author, publisher, yearOfPublication, bookName
        );

        return filteredBooks;
    }

    // New Filter books
   /*public List<Book> filterBooksNewService( String stream, String author, String publisher, Long yearOfPublication, String bookName){
        long yearOfPublicationPass;

        if(yearOfPublication == null){
            yearOfPublicationPass = -1L;
        } else{
            yearOfPublicationPass = yearOfPublication.longValue();
        }

        Book bookDetails = new Book();

        bookDetails.setBookName(bookName);
        bookDetails.setAuthor(author);
        bookDetails.setStream(stream);
        bookDetails.setYearOfPublication(yearOfPublicationPass);
        bookDetails.setPublisher(publisher);

        List<Book> filteredBooks = this.bookRepository.filterBooksByBookDetails(bookDetails);

        return filteredBooks;
    }*/

    public void deleteBookById(Long id){
        this.bookRepository.deleteById(id);

    }

    public Book updateBookDetails(Long id, Book bookDetails) {
        //Book book = this.bookRepository.findById(id).orElseThrow(() -> new RuntimeException("Book with given book id is not found"));
        Optional<Book> book = this.bookRepository.findById(id);

        // update only not null parameters
        if (book.isPresent()) {

            Book bookGot = book.get();

            if (bookDetails.getStream() != null) {
                bookGot.setStream(bookDetails.getStream());
            }

            if (bookDetails.getAuthor() != null) {
                bookGot.setAuthor(bookDetails.getAuthor());
            }

            if (bookDetails.getPublisher() != null) {
                bookGot.setPublisher(bookDetails.getPublisher());
            }

            if (bookDetails.getYearOfPublication() != 0) {
                bookGot.setYearOfPublication(bookDetails.getYearOfPublication());
            }

            return this.bookRepository.save(bookGot);
        }
        return null;

    }

}
