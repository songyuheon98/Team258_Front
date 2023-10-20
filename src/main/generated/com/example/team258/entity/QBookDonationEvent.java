package com.example.team258.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBookDonationEvent is a Querydsl query type for BookDonationEvent
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBookDonationEvent extends EntityPathBase<BookDonationEvent> {

    private static final long serialVersionUID = 1998498939L;

    public static final QBookDonationEvent bookDonationEvent = new QBookDonationEvent("bookDonationEvent");

    public final ListPath<BookApplyDonation, QBookApplyDonation> bookApplyDonations = this.<BookApplyDonation, QBookApplyDonation>createList("bookApplyDonations", BookApplyDonation.class, QBookApplyDonation.class, PathInits.DIRECT2);

    public final ListPath<Book, QBook> books = this.<Book, QBook>createList("books", Book.class, QBook.class, PathInits.DIRECT2);

    public final DateTimePath<java.time.LocalDateTime> closedAt = createDateTime("closedAt", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> donationId = createNumber("donationId", Long.class);

    public QBookDonationEvent(String variable) {
        super(BookDonationEvent.class, forVariable(variable));
    }

    public QBookDonationEvent(Path<? extends BookDonationEvent> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBookDonationEvent(PathMetadata metadata) {
        super(BookDonationEvent.class, metadata);
    }

}

