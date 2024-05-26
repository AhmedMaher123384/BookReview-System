package com.BookReview.System.Controller;

import com.BookReview.System.Model.Dto.*;
import com.BookReview.System.Model.Entity.Book;
import com.BookReview.System.Model.Entity.Review;
import com.BookReview.System.Service.Impl.BookServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = BookController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class BookControllerTest {


    @MockBean
    private BookServiceImpl bookService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private BookDto bookDto;
    private ReviewDto reviewDto;
    private AddBookDto addBookDto;
    private UpdateBookDto updateBookDto;
    private Book book;
    private Review review;
    private BookResponse bookResponse;


    @BeforeEach
    public void init(){
         book =Book.builder().name("cracking code").type("programming type").build();
         review=Review.builder().title("review title").content("test content").stars(5).build();
         bookDto=BookDto.builder().name("cracking code").type("programming type").build();
         reviewDto=ReviewDto.builder().title("review title").content("test content").stars(5).build();
        addBookDto=AddBookDto.builder().name("cracking code").type("programming type").build();
        updateBookDto=UpdateBookDto.builder().name("cracking code").type("programming type").build();
    }


    @Test
    public void BookController_CreateBook_ReturnCreated() throws Exception{

        when(bookService.createBook(addBookDto)).thenReturn(bookDto);
        ResultActions response =mockMvc.perform(MockMvcRequestBuilders.post("/book/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookDto)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(bookDto.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.type",CoreMatchers.is(bookDto.getType())));
    }
    @Test
    public void BookController_GetBookById_ReturnResponseDto() throws Exception{
        Long id=1l;
        when(bookService.getBookById(id)).thenReturn(bookDto);
        ResultActions response =mockMvc.perform(MockMvcRequestBuilders.get("/book/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(bookDto.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.type",CoreMatchers.is(bookDto.getType())));
    }

    @Test
    public void BookController_UpdateBook_ReturnResponseDto() throws Exception{
        Long id=1l;
        when(bookService.UpdateBook(updateBookDto,id)).thenReturn(bookDto);
        ResultActions response =mockMvc.perform(MockMvcRequestBuilders.put("/book/1/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(updateBookDto.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.type",CoreMatchers.is(updateBookDto.getType())));
    }

    @Test
    public void BookController_deleteById_ReturnOk() throws Exception{
        Long id=1l;
        doNothing().when(bookService).deleteById(id);
        ResultActions response =mockMvc.perform(MockMvcRequestBuilders.delete("/book/1/delete")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

    }

    @Test
    public void BookController_getAllBook_ReturnBookResponse() throws Exception{
        bookResponse = BookResponse.builder().pageSize(10).pageNo(1).last(true).content(Arrays.asList(bookDto)).build();
        int pageNo= 1;
        int pageSize=10;
        when(bookService.getAllBook(pageNo,pageSize)).thenReturn(bookResponse);
        ResultActions response =mockMvc.perform(MockMvcRequestBuilders.get("/book/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookResponse)).param("pageNo","1")
                .param("pageSize", "10"))

                .andExpect(MockMvcResultMatchers.jsonPath("$.content.size()", CoreMatchers.is(bookResponse.getContent().size())))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }
    @Test
    public void BookController_getAllBookFromDB_ReturnListOfBookDto () throws Exception{
        List<BookDto> dtos =Arrays.asList(bookDto);
        when(bookService.getAllBooksFromDB()).thenReturn(dtos);
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/book/all")
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dtos)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()",CoreMatchers.is(dtos.size())));

    }

}

