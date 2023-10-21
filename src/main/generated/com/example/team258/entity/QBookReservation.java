package com.example.team258.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBookReservation is a Querydsl query type for BookReservation
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBookReservation extends EntityPathBase<BookReservation> {

    private static final long serialVersionUID = -1974500641L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBookReservation bookReservation = new QBookReservation("bookReservation");

    public final QBook book;

    public final NumberPath<Long> bookReservationId = createNumber("bookReservationId", Long.class);

    public final QUser user;

    public QBookReservation(String variable) {
        this(BookReservation.class, forVariable(variable), INITS);
    }

    public QBookReservation(Path<? extends BookReservation> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBookReservation(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBookReservation(PathMetadata metadata, PathInits inits) {
        this(BookReservation.class, metadata, inits);
    }

    public QBookReservation(Class<? extends BookReservation> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.book = inits.isInitialized("book") ? new QBook(forProperty("book"), inits.get("book")) : null;
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

