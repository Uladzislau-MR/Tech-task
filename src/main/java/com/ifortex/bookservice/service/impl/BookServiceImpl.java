package com.ifortex.bookservice.service.impl;

import com.ifortex.bookservice.dao.BookRepository;
import com.ifortex.bookservice.dto.SearchCriteria;
import com.ifortex.bookservice.model.Book;
import com.ifortex.bookservice.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class BookServiceImpl implements BookService {
    @Autowired
    BookRepository bookRepository;
    @Override
    public Map<String, Long> getBooks() {
        return bookRepository.getBooks() ;
    }

    @Override
    public List<Book> getAllByCriteria(SearchCriteria searchCriteria) {
        return null;
    }
}
