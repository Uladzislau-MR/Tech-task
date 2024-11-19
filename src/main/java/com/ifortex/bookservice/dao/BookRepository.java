package com.ifortex.bookservice.dao;

import java.util.*;
import com.ifortex.bookservice.dto.SearchCriteria;
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

    public Map<String, Long> getBooksByTwoGenres() {
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

            statistic.put(genreKey, statistic.getOrDefault(genreKey, 0L) + 1);
        }

        List<Map.Entry<String, Long>> entryList = new ArrayList<>(statistic.entrySet());
        entryList.sort((entry1, entry2) -> Long.compare(entry2.getValue(), entry1.getValue()));

        Map<String, Long> sortedStatistic = new LinkedHashMap<>();
        for (Map.Entry<String, Long> entry : entryList) {
            sortedStatistic.put(entry.getKey(), entry.getValue());
        }
        return sortedStatistic;
    }

    public Map<String, Long> getBooks() {
        List<Book> bookList = getAllBooks();
        Map<String, Long> statistic = new HashMap<>();
        for (Book book : bookList) {
            Set<String> genres = book.getGenres();
            List<String> genresList = new ArrayList<>(genres);
            for (String genre : genresList) {
                statistic.put(genre, statistic.getOrDefault(genre, 0L) + 1);
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

    public List<Book> getAllByCriteria(SearchCriteria searchCriteria) {
        String title = searchCriteria.getTitle();
        String author = searchCriteria.getAuthor();
        String genre = searchCriteria.getGenre();
        String description = searchCriteria.getDescription();
        Integer year = searchCriteria.getYear();

        List<Book> bookList = getAllBooks();
        List<Book> resultList = new ArrayList<>();

        for (Book book : bookList) {
            if ((title != null && !title.trim().isEmpty() && book.getTitle().contains(title)) ||
                (author != null && !author.trim().isEmpty() && book.getAuthor().contains(author)) ||
                (description != null && !description.trim().isEmpty() && book.getDescription().contains(description)) ||
                (year != null && book.getPublicationDate().getYear() == year)) {
                resultList.add(book);
                continue;
            }

            if (genre != null && !genre.trim().isEmpty()) {
                String[] genresArray = genre.split(",");
                for (String g : genresArray) {
                    g = g.trim();
                    if (book.getGenres().contains(g)) {
                        resultList.add(book);
                        break;
                    }
                }
            }
        }
        if (resultList.isEmpty()) {
            return bookList;
        }
        return resultList;

    }
}
