package com.example.team258.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBookRent is a Querydsl query type for BookRent
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBookRent extends EntityPathBase<BookRent> {

    private static final long serialVersionUID = 1269668742L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBookRent bookRent = new QBookRent("bookRent");

    public final QBook book;

    public final NumberPath<Long> bookRentId = createNumber("bookRentId", Long.class);

    public final DateTimePath<java.time.LocalDateTime> returnDate = createDateTime("returnDate", java.time.LocalDateTime.class);

    public QBookRent(String variable) {
        this(BookRent.class, forVariable(variable), INITS);
    }

    public QBookRent(Path<? extends BookRent> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBookRent(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBookRent(PathMetadata metadata, PathInits inits) {
        this(BookRent.class, metadata, inits);
    }

    public QBookRent(Class<? extends BookRent> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.book = inits.isInitialized("book") ? new QBook(forProperty("book"), inits.get("book")) : null;
    }

}

