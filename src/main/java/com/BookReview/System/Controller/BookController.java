package com.BookReview.System.Controller;

import com.BookReview.System.Model.Dto.AddBookDto;
import com.BookReview.System.Model.Dto.BookDto;
import com.BookReview.System.Model.Dto.BookResponse;
import com.BookReview.System.Model.Dto.UpdateBookDto;
import com.BookReview.System.Model.Entity.Book;
import com.BookReview.System.Service.Impl.BookServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/book")
public class BookController {

    @Autowired
    private BookServiceImpl bookService;

    @PostMapping("/")
    public ResponseEntity<BookDto> createBook(@RequestBody AddBookDto addBookDto){
        return new ResponseEntity<>(this.bookService.createBook(addBookDto), HttpStatus.CREATED);
    }
    @GetMapping("/{id}")
    public ResponseEntity<BookDto> getBookById(@PathVariable("id") Long id){
        return new ResponseEntity<>(this.bookService.getBookById(id), HttpStatus.OK);

    }
    @PutMapping("/{id}/update")
    public ResponseEntity<BookDto> UpdateBook(@RequestBody UpdateBookDto updateBookDto,@PathVariable("id") Long id){
        return new ResponseEntity<>(this.bookService.UpdateBook(updateBookDto,id), HttpStatus.OK);
    }
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<String> deleteById(@PathVariable("id") Long id){
        this.bookService.deleteById(id);
        return new ResponseEntity<>(String.format("this id %s is deleted",id), HttpStatus.NO_CONTENT);
    }
    @GetMapping("/")
    public ResponseEntity<BookResponse> getAllBook(@RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
                                                   @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize){
        return new ResponseEntity<>(this.bookService.getAllBook(pageNo,pageSize), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<BookDto>> getAllBooksFromDB(){
        return new ResponseEntity<>(this.bookService.getAllBooksFromDB(), HttpStatus.OK);

    }

}
