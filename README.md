# Book Review Application

## Overview
This application offers a platform for users to evaluate books through reviews and ratings before making a purchase. It helps users make informed decisions by providing insights from other readers.

## Technologies Used
- **Spring Boot**: Framework for creating stand-alone, production-grade Spring-based applications.
- **JUnit**: Framework for unit testing Java applications.
- **Spring Security**: Provides authentication and authorization support.
- **PostgreSQL**: Used as the relational database management system.

## Features

### Books
- **Create Book**: Add new books with names and types.
- **Retrieve Book**: Fetch book details by ID.
- **Update Book**: Modify existing book details.
- **Delete Book**: Remove a book by ID.
- **List All Books**: Paginated view of books.
- **Get Detailed Book List**: Comprehensive list of all books.

### Reviews
- **Create Review**: Post reviews for books with content and ratings.
- **Get Reviews by Book ID**: View all reviews for a specific book.
- **Retrieve Review**: Get details of a specific review.
- **Update Review**: Edit content, title, and ratings of a review.
- **Delete Review**: Remove a review ensuring it is associated with the correct book.

## Setup
1. Clone the repository.
2. Configure your database credentials in `enviroment in docker-compose`.
3. Set up Maven:
   ```bash
   mvn clean install
4. Set up Docker and run Docker Compose:
   ```bash
   docker-compose up -d 

## Usage
Use the provided endpoints to manage books and reviews through a RESTful API.

