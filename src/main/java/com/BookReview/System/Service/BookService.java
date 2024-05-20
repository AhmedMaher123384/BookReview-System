package com.BookReview.System.Service;

import com.BookReview.System.Model.Dto.*;
import com.BookReview.System.Model.Entity.Book;

import java.util.List;

public interface BookService {
     BookDto createBook(AddBookDto addBookDto);
     BookDto getBookById(Long id);
     BookDto UpdateBook(UpdateBookDto updateBookDto,Long id);
     void deleteById(Long id);
    BookResponse getAllBook(int pageNo, int pageSize);

    List<BookDto> getAllBooksFromDB();

}
