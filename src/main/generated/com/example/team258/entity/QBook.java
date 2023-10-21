package com.example.team258.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBook is a Querydsl query type for Book
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBook extends EntityPathBase<Book> {

    private static final long serialVersionUID = 1795549869L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBook book = new QBook("book");

    public final QBookApplyDonation bookApplyDonation;

    public final StringPath bookAuthor = createString("bookAuthor");

    public final QBookCategory bookCategory;

    public final QBookDonationEvent bookDonationEvent;

    public final NumberPath<Long> bookId = createNumber("bookId", Long.class);

    public final StringPath bookName = createString("bookName");

    public final StringPath bookPublish = createString("bookPublish");

    public final QBookRent bookRent;

    public final ListPath<BookReservation, QBookReservation> bookReservations = this.<BookReservation, QBookReservation>createList("bookReservations", BookReservation.class, QBookReservation.class, PathInits.DIRECT2);

    public final EnumPath<BookStatusEnum> bookStatus = createEnum("bookStatus", BookStatusEnum.class);

    public QBook(String variable) {
        this(Book.class, forVariable(variable), INITS);
    }

    public QBook(Path<? extends Book> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBook(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBook(PathMetadata metadata, PathInits inits) {
        this(Book.class, metadata, inits);
    }

    public QBook(Class<? extends Book> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.bookApplyDonation = inits.isInitialized("bookApplyDonation") ? new QBookApplyDonation(forProperty("bookApplyDonation"), inits.get("bookApplyDonation")) : null;
        this.bookCategory = inits.isInitialized("bookCategory") ? new QBookCategory(forProperty("bookCategory"), inits.get("bookCategory")) : null;
        this.bookDonationEvent = inits.isInitialized("bookDonationEvent") ? new QBookDonationEvent(forProperty("bookDonationEvent")) : null;
        this.bookRent = inits.isInitialized("bookRent") ? new QBookRent(forProperty("bookRent"), inits.get("bookRent")) : null;
    }

}

