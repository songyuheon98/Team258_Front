package com.example.team258.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = 1796119439L;

    public static final QUser user = new QUser("user");

    public final QTimestamped _super = new QTimestamped(this);

    public final ListPath<BookApplyDonation, QBookApplyDonation> bookApplyDonations = this.<BookApplyDonation, QBookApplyDonation>createList("bookApplyDonations", BookApplyDonation.class, QBookApplyDonation.class, PathInits.DIRECT2);

    public final ListPath<BookRent, QBookRent> bookRents = this.<BookRent, QBookRent>createList("bookRents", BookRent.class, QBookRent.class, PathInits.DIRECT2);

    public final ListPath<BookReservation, QBookReservation> bookReservations = this.<BookReservation, QBookReservation>createList("bookReservations", BookReservation.class, QBookReservation.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final StringPath password = createString("password");

    public final EnumPath<UserRoleEnum> role = createEnum("role", UserRoleEnum.class);

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public final StringPath username = createString("username");

    public QUser(String variable) {
        super(User.class, forVariable(variable));
    }

    public QUser(Path<? extends User> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUser(PathMetadata metadata) {
        super(User.class, metadata);
    }

}

