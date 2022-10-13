package com.miniproject.bookstore.business;

import com.miniproject.bookstore.data.Book;
import com.miniproject.bookstore.data.BookRepository;
import com.miniproject.bookstore.data.Photo;
import com.miniproject.bookstore.data.PhotoRepository;
import com.miniproject.bookstore.error.exceptions.EntityAlreadyExistsException;
import com.miniproject.bookstore.error.exceptions.EntityNotFoundException;
import com.miniproject.bookstore.error.exceptions.InvalidEntityException;
import org.springframework.stereotype.Service;


import java.time.Year;
import java.util.List;
import java.util.Optional;


@Service
public class BookService {

    private final BookRepository bookRepository;
    private final PhotoRepository photoRepository;

    public BookService(BookRepository bookRepository, PhotoRepository photoRepository){
        this.bookRepository = bookRepository;
        this.photoRepository = photoRepository;
    }

    public Book addBook(Book book){

        System.out.println("Book to be added: " + book);

        if(book.getBookName() == null){
            throw new InvalidEntityException("Book name cannot be null!");
        }


        if(book.getYearOfPublication() < 0 || book.getYearOfPublication() > Year.now().getValue()){
            throw new InvalidEntityException("Invalid year of publication");
        }


        List<Book> filterBooks = filterBooks(
                book.getStream(), book.getAuthor(), book.getPublisher(), book.getYearOfPublication(), book.getBookName()
        );

        if(!filterBooks.isEmpty()){
            throw new EntityAlreadyExistsException("Book with given book details already exist!");
        }

        return this.bookRepository.save(book);

    }

    public List<Book> filterBooks( String stream, String author, String publisher, Long yearOfPublication, String bookName){

        List<Book> filteredBooks = this.bookRepository.filterBooksByParameters(
                stream, author, publisher, yearOfPublication, bookName
        );

        return filteredBooks;
    }



    public Book deleteBookById(Long id){
        Book book = this.bookRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Book with given ID: " + id + " not found"));
        this.bookRepository.deleteById(id);
        return book;

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

            if (bookDetails.getYearOfPublication() > 0 && bookDetails.getYearOfPublication() < Year.now().getValue()) {
                bookGot.setYearOfPublication(bookDetails.getYearOfPublication());
            } else if(bookDetails.getYearOfPublication() != 0){
                throw new InvalidEntityException("Invalid Year of publication");
            }

            return this.bookRepository.save(bookGot);
        } else {
            throw new EntityNotFoundException("Book with given ID: " + id + " not Found");
        }

    }

    public Book addPhotoToBook(Long bookId, Long photoId){
       Book book = bookRepository.findById(bookId).get();
       Photo photo = photoRepository.findById(photoId).get();

       book.setPhoto(photo);
       bookRepository.save(book);
       return book;
    }

    public Photo addPhoto(Photo photo){
        return this.photoRepository.save(photo);
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

}
