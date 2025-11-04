package tqs.lab6_2;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.time.LocalDate;

public class Library {
    private List<Book> store;

    public Library() {
        this.store = new ArrayList<>();
    }

    public void addBook(Book book) {
        store.add(book);
    }

    public List<Book> findBooksByAuthor(String author) {
        return store.stream()
                .filter(book -> book.getAuthor().equalsIgnoreCase(author))
                .collect(Collectors.toList());
    }

    public List<Book> findBooks(LocalDate from, LocalDate to) {
        return store.stream()
                .filter(book -> (book.getPublished().isAfter(from) || book.getPublished().isEqual(from)) &&
                                (book.getPublished().isBefore(to) || book.getPublished().isEqual(to)))
                .collect(Collectors.toList());
    }
}