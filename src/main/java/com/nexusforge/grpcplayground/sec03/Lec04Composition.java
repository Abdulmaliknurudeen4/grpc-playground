package com.nexusforge.grpcplayground.sec03;

import com.nexusforge.grpcplayground.models.sec03.Book;
import com.nexusforge.grpcplayground.models.sec03.Library;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Lec04Composition {
    public static final Logger log = LoggerFactory.getLogger(Lec04.class);

    public static void main(String[] args) {
        // create books
        var book1 = Book.newBuilder()
                .setTitle("Harry Potter - part 1")
                .setAuthor("JK rowling")
                .setPublicationYear(1997)
                .build();

        var book2 = book1.toBuilder().setTitle("Harry Porter 2")
                .setPublicationYear(1998).build();
        var book3 = book1.toBuilder().setTitle("Harry Porter 3")
                .setPublicationYear(2020).build();

        var library = Library.newBuilder()
                .setName("Fantasy Library")
//                .addBooks(book1)
//                .addBooks(book2)
//                .addBooks(book3)
                .addAllBooks(List.of(book1, book2, book3))
                .build();

        log.info("{}", library);

    }
}
