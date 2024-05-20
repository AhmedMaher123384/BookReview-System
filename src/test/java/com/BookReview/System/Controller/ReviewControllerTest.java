package com.BookReview.System.Controller;

import com.BookReview.System.Model.Dto.*;
import com.BookReview.System.Model.Entity.Book;
import com.BookReview.System.Model.Entity.Review;
import com.BookReview.System.Service.Impl.BookServiceImpl;
import com.BookReview.System.Service.Impl.ReviewServiceImpl;
import com.BookReview.System.Service.ReviewService;
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
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = ReviewController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ReviewControllerTest {

    @MockBean
    private ReviewServiceImpl reviewService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private BookDto bookDto;
    private ReviewDto reviewDto;
    private Book book;
    private AddReviewDto addReviewDto;
    private Review review;
    @BeforeEach
    public void init(){
        book =Book.builder().name("cracking code").type("programming type").build();
        review=Review.builder().title("review title").content("test content").stars(5).build();
        bookDto=BookDto.builder().name("cracking code").type("programming type").build();
        reviewDto=ReviewDto.builder().title("review title").content("test content").stars(5).build();
        addReviewDto=AddReviewDto.builder().title("review title").content("test content").stars(5).build();

    }

    @Test
    public void ReviewController_createReview_ReturnCreated() throws Exception{
        Long id = 1l;
        when(reviewService.createReview(id,addReviewDto)).thenReturn(reviewDto);
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post("/review/1/")
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(reviewDto)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", CoreMatchers.is(reviewDto.getTitle())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.stars",CoreMatchers.is(reviewDto.getStars())));

    }
    @Test
    public void ReviewController_getReviewsByPokemonId_ReturnReviewDto() throws Exception{
        Long id = 1l;
        List<ReviewDto> dtos = Arrays.asList(reviewDto);
        when(reviewService.getReviewsByBookId(id)).thenReturn(dtos);
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/review/1/")
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(reviewDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title", CoreMatchers.is(reviewDto.getTitle())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()",CoreMatchers.is(dtos.size())));
    }

    @Test
    public void ReviewController_getReviewById_ReturnReviewDto() throws Exception{
        Long reviewId = 1l;
        Long BookId=1l;
        when(reviewService.getReviewById(reviewId,BookId)).thenReturn(reviewDto);
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/review/1/1")
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(reviewDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", CoreMatchers.is(reviewDto.getTitle())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.stars",CoreMatchers.is(reviewDto.getStars())));
    }
    @Test
    public void ReviewController_updateReview_ReturnReviewDto() throws Exception{
        Long reviewId = 1l;
        Long BookId=1l;
        when(reviewService.updateReview(reviewId,BookId,reviewDto)).thenReturn(reviewDto);
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.put("/review/1/1")
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(reviewDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", CoreMatchers.is(reviewDto.getTitle())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.stars",CoreMatchers.is(reviewDto.getStars())));
    }
    @Test
    public void ReviewController_deleteReview_ReturnOk() throws Exception{
        Long reviewId = 1l;
        Long BookId=1l;
        doNothing().when(reviewService).deleteReview(reviewId,BookId);
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.delete("/review/1/1")
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(reviewDto)))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }



}
