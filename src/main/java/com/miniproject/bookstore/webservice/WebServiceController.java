package com.miniproject.bookstore.webservice;

import com.miniproject.bookstore.business.BookService;
import com.miniproject.bookstore.data.Book;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class WebServiceController {
    // Inject service
    private final BookService bookService;

    public WebServiceController(BookService bookService){
        this.bookService = bookService;
    }

    // 1. POST - save a book with below details
    @PostMapping(
            path = "/book",
            consumes = "application/json"
    )
    @ResponseStatus(HttpStatus.CREATED)
    public Book saveBook(@RequestBody Book book){
        return this.bookService.addBook(book);
    }

    // 2. GET - retrieve list of books based on filter
    @GetMapping("/book")
    @ResponseStatus(HttpStatus.OK)
    public List<Book> filterBooks(
            @RequestParam(value = "stream", required = false)String stream,
            @RequestParam(value = "author", required = false)String author,
            @RequestParam(value = "publisher", required = false)String publisher,
            @RequestParam(value = "yearOfPublication", required = false)Long yearOfPublication,
            @RequestParam(value= "bookName", required = false)String bookName
    ){
        return this.bookService.filterBooks(stream, author, publisher, yearOfPublication, bookName);
    }

    // New Get Mapping test
   /* @GetMapping("/books")
    public List<Book> filterBooksNew(
            @RequestParam(value = "stream", required = false)String stream,
            @RequestParam(value = "author", required = false)String author,
            @RequestParam(value = "publisher", required = false)String publisher,
            @RequestParam(value = "yearOfPublication", required = false)Long yearOfPublication,
            @RequestParam(value= "bookName", required = false)String bookName
    ){
        return this.bookService.filterBooksNewService(stream, author, publisher, yearOfPublication, bookName);
    }*/

    // 3. PUT - Update book details by id.
    @PutMapping(path= "/book/{id}", consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Book updateBook(@PathVariable(value = "id")Long id, @RequestBody Book bookDetails){
        return this.bookService.updateBookDetails(id, bookDetails);
    }

    // 4. DELETE - Delete book details by id .
    @DeleteMapping("/book/{id}")
    public void deleteBook(@PathVariable Long id){
        this.bookService.deleteBookById(id);
    }


}
