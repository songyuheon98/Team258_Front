package com.example.team258.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBookApplyDonation is a Querydsl query type for BookApplyDonation
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBookApplyDonation extends EntityPathBase<BookApplyDonation> {

    private static final long serialVersionUID = 1066198899L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBookApplyDonation bookApplyDonation = new QBookApplyDonation("bookApplyDonation");

    public final DateTimePath<java.time.LocalDateTime> applyDate = createDateTime("applyDate", java.time.LocalDateTime.class);

    public final NumberPath<Long> applyId = createNumber("applyId", Long.class);

    public final QBook book;

    public QBookApplyDonation(String variable) {
        this(BookApplyDonation.class, forVariable(variable), INITS);
    }

    public QBookApplyDonation(Path<? extends BookApplyDonation> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBookApplyDonation(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBookApplyDonation(PathMetadata metadata, PathInits inits) {
        this(BookApplyDonation.class, metadata, inits);
    }

    public QBookApplyDonation(Class<? extends BookApplyDonation> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.book = inits.isInitialized("book") ? new QBook(forProperty("book"), inits.get("book")) : null;
    }

}

