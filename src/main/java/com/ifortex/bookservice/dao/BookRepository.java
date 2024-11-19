package com.ifortex.bookservice.dao;

import java.util.*;

import com.ifortex.bookservice.model.Book;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;



@Repository
@RequiredArgsConstructor
public class BookRepository {

    private final EntityManager entityManager;


    public List<Book> getAllBooks() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Book> criteriaQuery = criteriaBuilder.createQuery(Book.class);
        Root<Book> root = criteriaQuery.from(Book.class);
        criteriaQuery.select(root);

        return entityManager.createQuery(criteriaQuery).getResultList();
    }
    public Map<String, Long> getBooks() {
        List<Book> bookList = getAllBooks();

        Map<String, Long> statistic = new LinkedHashMap<>();

        for (Book book : bookList) {
            Set<String> genres = book.getGenres();
            List<String> genreList = new ArrayList<>(genres);
            StringBuilder genreKeyBuilder = new StringBuilder();
            for (String genre : genreList) {
                if (genreKeyBuilder.length() > 0) {
                    genreKeyBuilder.append(",");
                }
                genreKeyBuilder.append(genre);
            }
            String genreKey = genreKeyBuilder.toString();

            if (statistic.containsKey(genreKey)) {
                statistic.put(genreKey, statistic.get(genreKey) + 1);
            } else {
                statistic.put(genreKey, 1L);
            }
        }

        List<Map.Entry<String, Long>> entryList = new ArrayList<>(statistic.entrySet());
        entryList.sort((entry1, entry2) -> Long.compare(entry2.getValue(), entry1.getValue()));

        Map<String, Long> sortedStatistic = new LinkedHashMap<>();
        for (Map.Entry<String, Long> entry : entryList) {
            sortedStatistic.put(entry.getKey(), entry.getValue());
        }
        return sortedStatistic;
    }








}





