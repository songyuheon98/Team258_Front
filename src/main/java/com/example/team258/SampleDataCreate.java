//package com.example.team258;
//
//import com.example.team258.entity.*;
//import com.example.team258.repository.*;
//import jakarta.annotation.PostConstruct;
//import jakarta.persistence.*;
//import lombok.Builder;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//
//@Component
//@RequiredArgsConstructor
//public class SampleDataCreate {
//    private final InitService initService;
//
//    @PostConstruct
//    public void init() {
//        initService.userInit();
//        initService.bookInit();
//        initService.categoryInit();
//        initService.bookEventDonationInit();
//    }
//    @Component
//    @Transactional
//    @RequiredArgsConstructor
//    static class InitService {
//
//        private final UserRepository userRepository;
//        private final BookRepository bookRepository;
//        private final BookCategoryRepository bookCategoryRepository;
//        private final BookDonationEventRepository bookDonationEventRepository;
//        private final PasswordEncoder passwordEncoder;
//
//        public void userInit() {
//            User user1 = User.builder().username("bin0001").password(passwordEncoder.encode("Bin@12345")).role(UserRoleEnum.USER).build();
//            User user2 = User.builder().username("bin0002").password(passwordEncoder.encode("Bin@12345")).role(UserRoleEnum.USER).build();
//            User user3 = User.builder().username("bin0003").password(passwordEncoder.encode("Bin@12345")).role(UserRoleEnum.USER).build();
//            User user4 = User.builder().username("bin0004").password(passwordEncoder.encode("Bin@12345")).role(UserRoleEnum.ADMIN).build();
//            User user5 = User.builder().username("bin0005").password(passwordEncoder.encode("Bin@12345")).role(UserRoleEnum.ADMIN).build();
//            User user6 = User.builder().username("bin0006").password(passwordEncoder.encode("Bin@12345")).role(UserRoleEnum.ADMIN).build();
//
//            userRepository.save(user1);
//            userRepository.save(user2);
//            userRepository.save(user3);
//            userRepository.save(user4);
//            userRepository.save(user5);
//            userRepository.save(user6);
//        }
//        public void bookInit() {
//            Book book1 = Book.builder().bookName("book1").bookAuthor("author1").bookAuthor("author1").bookPublish("2011").bookStatus(BookStatusEnum.POSSIBLE).build();
//            Book book2 = Book.builder().bookName("book2").bookAuthor("author2").bookAuthor("author2").bookPublish("2011").bookStatus(BookStatusEnum.POSSIBLE).build();
//            Book book3 = Book.builder().bookName("book3").bookAuthor("author3").bookAuthor("author3").bookPublish("2011").bookStatus(BookStatusEnum.POSSIBLE).build();
//            Book book4 = Book.builder().bookName("book4").bookAuthor("author4").bookAuthor("author4").bookPublish("2011").bookStatus(BookStatusEnum.IMPOSSIBLE).build();
//            Book book5 = Book.builder().bookName("book5").bookAuthor("author5").bookAuthor("author5").bookPublish("2011").bookStatus(BookStatusEnum.IMPOSSIBLE).build();
//            Book book6 = Book.builder().bookName("book6").bookAuthor("author6").bookAuthor("author6").bookPublish("2011").bookStatus(BookStatusEnum.IMPOSSIBLE).build();
//            Book book7 = Book.builder().bookName("book7").bookAuthor("author7").bookAuthor("author7").bookPublish("2011").bookStatus(BookStatusEnum.DONATION).build();
//            Book book8 = Book.builder().bookName("book8").bookAuthor("author8").bookAuthor("author8").bookPublish("2011").bookStatus(BookStatusEnum.DONATION).build();
//            Book book9 = Book.builder().bookName("book9").bookAuthor("author9").bookAuthor("author9").bookPublish("2011").bookStatus(BookStatusEnum.DONATION).build();
//
//            bookRepository.save(book1);
//            bookRepository.save(book2);
//            bookRepository.save(book3);
//            bookRepository.save(book4);
//            bookRepository.save(book5);
//            bookRepository.save(book6);
//            bookRepository.save(book7);
//            bookRepository.save(book8);
//            bookRepository.save(book9);
//        }
//        public void categoryInit() {
//            BookCategory bookCategory1 = BookCategory.builder().bookCategoryName("category1").build();
//            BookCategory bookCategory2 = BookCategory.builder().bookCategoryName("category2").build();
//            BookCategory bookCategory3 = BookCategory.builder().bookCategoryName("category3").build();
//            BookCategory bookCategory4 = BookCategory.builder().bookCategoryName("category4").build();
//            BookCategory bookCategory5 = BookCategory.builder().bookCategoryName("category5").build();
//
//            bookCategoryRepository.save(bookCategory1);
//            bookCategoryRepository.save(bookCategory2);
//            bookCategoryRepository.save(bookCategory3);
//            bookCategoryRepository.save(bookCategory4);
//            bookCategoryRepository.save(bookCategory5);
//        }
//
//        public void bookEventDonationInit(){
//            BookDonationEvent bookDonationEvent1 = BookDonationEvent.builder().createdAt(LocalDateTime.now()).closedAt(LocalDateTime.parse("2023-10-12T19:16:59")).build();
//            BookDonationEvent bookDonationEvent2 = BookDonationEvent.builder().createdAt(LocalDateTime.now()).closedAt(LocalDateTime.parse("2023-10-12T19:16:59")).build();
//            BookDonationEvent bookDonationEvent3 = BookDonationEvent.builder().createdAt(LocalDateTime.now()).closedAt(LocalDateTime.parse("2023-10-12T19:16:59")).build();
//
//            bookDonationEventRepository.save(bookDonationEvent1);
//            bookDonationEventRepository.save(bookDonationEvent2);
//            bookDonationEventRepository.save(bookDonationEvent3);
//        }
//
//
//    }
//}