package com.BookReview.System.Service;

import com.BookReview.System.Model.Dto.AddBookDto;
import com.BookReview.System.Model.Dto.BookDto;
import com.BookReview.System.Model.Dto.BookResponse;
import com.BookReview.System.Model.Dto.UpdateBookDto;
import com.BookReview.System.Model.Entity.Book;
import com.BookReview.System.Model.Entity.Review;
import com.BookReview.System.Repository.BookRepository;
import com.BookReview.System.Service.Impl.BookServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;
    @InjectMocks
    private BookServiceImpl bookService;
    private Book book;
    private BookDto bookDto;
    private Review review;
    private AddBookDto addBookDto;
    private UpdateBookDto updateBookDto;
    private BookResponse bookResponse;


    @BeforeEach
    public void init(){
        book =Book.builder().name("cracking code").type("programming type").build();
        review= Review.builder().title("review title").content("test content").stars(5).build();
        bookDto=BookDto.builder().name("cracking code").type("programming type").build();
        addBookDto= AddBookDto.builder().name("cracking code").type("programming type").build();
        updateBookDto= UpdateBookDto.builder().name("cracking code").type("programming type").build();
    }

        @Test
        public void BookService_createBook_ReturnBookDto(){
        when(bookRepository.save(book)).thenReturn(book);
        Assertions.assertThat(bookService.createBook(addBookDto)).isEqualTo(bookDto);
        }


    @Test
    public void BookService_getBookById_ReturnBookDto(){
        Long id =1l;
        when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        Assertions.assertThat(bookService.getBookById(id)).isEqualTo(bookDto);
    }

    @Test
    public void BookService_UpdateBook_ReturnBookDto(){
        Long id =1l;
        when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        when(bookRepository.save(book)).thenReturn(book);
        Assertions.assertThat(bookService.UpdateBook(updateBookDto,id)).isNotNull();
        Assertions.assertThat(bookService.UpdateBook(updateBookDto,id)).isEqualTo(bookDto);
    }
    @Test
    public void BookService_deleteById_Return_void(){
        Long id =1l;
        when(bookRepository.findById(id)).thenReturn(Optional.ofNullable(book));
        doNothing().when(bookRepository).delete(book);
        assertAll(() -> bookService.deleteById(id));
    }

    @Test
    public void BookService_getAllBook_ReturnBookResponse(){
        bookResponse = BookResponse.builder().pageSize(10).pageNo(1).last(true).content(Arrays.asList(bookDto)).build();
        Page<Book> books= Mockito.mock(Page.class);
        when(bookRepository.findAll(Mockito.any(Pageable.class))).thenReturn(books);
        Assertions.assertThat(bookService.getAllBook(1,10)).isNotNull();
    }

    @Test
    public void BookService_getAll_ReturnBookResponse(){
        List<Book> books= Arrays.asList(book);
        List<BookDto> dtos= Arrays.asList(bookDto);
        when(bookRepository.findAll()).thenReturn(books);
        Assertions.assertThat(bookService.getAllBooksFromDB()).isNotNull();
        Assertions.assertThat(bookService.getAllBooksFromDB()).isEqualTo(dtos);

    }

}
