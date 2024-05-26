package com.BookReview.System.Service.Impl;

import com.BookReview.System.Exception.BookNotFoundException;
import com.BookReview.System.Model.Dto.*;
import com.BookReview.System.Model.Entity.Book;
import com.BookReview.System.Repository.BookRepository;
import com.BookReview.System.Service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

    @Override
    public BookDto createBook(AddBookDto addBookDto) {
        Book book =mapAddDtoToEntity(addBookDto);
        Book entity = this.bookRepository.save(book);
        BookDto bookDto =mapToDto(entity);
        return bookDto;

    }

    @Override
    public BookDto getBookById(Long id) {
        Book book = this.bookRepository.findById(id).orElseThrow(()->new BookNotFoundException("Pokemon could not be delete"));
        return mapToDto(book);
    }

    @Override
    public BookDto UpdateBook(UpdateBookDto updateBookDto, Long id) {
        Book book = this.bookRepository.findById(id).orElseThrow(()->new BookNotFoundException("Pokemon could not be delete"));
        book.setName(updateBookDto.getName());
        book.setType(updateBookDto.getType());
        Book updated = this.bookRepository.save(book);
        BookDto bookDto =mapToDto(updated);
        return bookDto;
    }

    @Override
    public void deleteById(Long id) {
        Book book=this.bookRepository.findById(id).orElseThrow(()->new BookNotFoundException("Pokemon could not be delete"));
        this.bookRepository.delete(book);

    }

    @Override
    public BookResponse getAllBook(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo,pageSize);
        Page<Book> books = this.bookRepository.findAll(pageable);
        List<Book> bookList =books.getContent();
        List<BookDto> bookDtoList=maptoListDto(bookList);

        BookResponse bookResponse=BookResponse.builder()
                .content(bookDtoList)
                .last(books.isLast())
                .pageNo(books.getNumber())
                .totalElements(books.getTotalElements())
                .pageSize(books.getSize())
                .totalPages(books.getTotalPages())
                .build();

        return bookResponse;
    }

    @Override
    public List<BookDto> getAllBooksFromDB() {
        List<Book> bookentity = this.bookRepository.findAll();
        List<BookDto> bookDtos =maptoListDto(bookentity);
        return bookDtos;
    }



    BookDto mapToDto(Book book){
        return BookDto.builder().id(book.getId()).name(book.getName()).type(book.getType()).build();
    }

    Book mapAddDtoToEntity(AddBookDto addBookDto){
        return Book.builder().name(addBookDto.getName()).type(addBookDto.getType()).build();
    }
    List<BookDto> maptoListDto(List<Book> books){
        List<BookDto> dtos = books.stream().map(book -> {
            BookDto bookDto= BookDto
                    .builder()
                    .id(book.getId())
                    .name(book.getName())
                    .type(book.getType())
                    .build();
            return bookDto;
        }).collect(Collectors.toList());
    return dtos;
    }



}
