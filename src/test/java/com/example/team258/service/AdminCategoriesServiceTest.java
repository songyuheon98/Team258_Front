package com.example.team258.service;

import com.example.team258.dto.AdminCategoriesRequestDto;
import com.example.team258.dto.AdminCategoriesResponseDto;
import com.example.team258.dto.MessageDto;
import com.example.team258.entity.Book;
import com.example.team258.entity.BookCategory;
import com.example.team258.entity.User;
import com.example.team258.entity.UserRoleEnum;
import com.example.team258.repository.AdminBooksRepository;
import com.example.team258.repository.BookCategoryRepository;
import com.example.team258.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminCategoriesServiceTest {

    @Mock
    private AdminBooksRepository adminBooksRepository;

    @Mock
    private BookCategoryRepository bookCategoryRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    AdminBooksService adminBooksService;

    @InjectMocks
    AdminCategoriesService adminCategoriesService;

    @Nested
    @DisplayName("카테고리 생성")
    class Create {

        @Test
        @DisplayName("카테고리 생성 성공")
        void createCategory() {
            // Given
            AdminCategoriesRequestDto adminCategoriesRequestDto = new AdminCategoriesRequestDto();
            adminCategoriesRequestDto.setBookCategoryName("TestCategory");  // 카테고리 생성에 필요한 데이터 설정

            // 모의 유저(관리자)생성
            User adminUser = mock(User.class);
            when(adminUser.getRole()).thenReturn(UserRoleEnum.ADMIN);

            // When
            ResponseEntity<MessageDto> response = adminCategoriesService.createBookCategory(adminCategoriesRequestDto, adminUser);

            // Then
            assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
            assertNotNull(response.getBody());
            assertEquals("카테고리 추가가 완료되었습니다.", response.getBody().getMsg());
        }
    }
    @Nested
    @DisplayName("하위 카테고리 생성")
    class CreateSubCategory {

        @Test
        @DisplayName("하위 카테고리 생성 성공")
        void createSubCategory() {
            // Given
            AdminCategoriesRequestDto adminCategoriesRequestDto = new AdminCategoriesRequestDto();
            adminCategoriesRequestDto.setBookCategoryName("TestSubCategory");  // 카테고리 생성에 필요한 데이터 설정

            Long parentCategoryId = 1L;

            // 모의 부모 카테고리 생성
            BookCategory parentCategory = mock(BookCategory.class);
            when(bookCategoryRepository.findById(1L)).thenReturn(Optional.of(parentCategory));

            // 모의 유저(관리자) 생성
            User adminUser = mock(User.class);
            when(adminUser.getRole()).thenReturn(UserRoleEnum.ADMIN);

            // When
            ResponseEntity<MessageDto> response = adminCategoriesService.createSubBookCategory(parentCategoryId, adminCategoriesRequestDto, adminUser);

            // Then
            assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
            assertNotNull(response.getBody());
            assertEquals("하위 카테고리 추가가 완료되었습니다.", response.getBody().getMsg());
        }

        @Test
        @DisplayName("부모 카테고리가 없을 때 예외 발생")
        void createSubCategoryWithoutParent() {
            // Given
            AdminCategoriesRequestDto adminCategoriesRequestDto = new AdminCategoriesRequestDto();
            adminCategoriesRequestDto.setBookCategoryName("TestSubCategory");  // 하위 카테고리 생성에 필요한 데이터 설정

            // 모의 유저(관리자) 생성
            User adminUser = mock(User.class);
            when(adminUser.getRole()).thenReturn(UserRoleEnum.ADMIN);

            // When, Then
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                adminCategoriesService.createSubBookCategory(null, adminCategoriesRequestDto, adminUser);
            });

            assertEquals("부모 카테고리를 찾을 수 없습니다.", exception.getMessage());
        }
    }
    @Nested
    @DisplayName("카테고리 조회")
    class ReadCategories {

        @Test
        @DisplayName("모든 카테고리 조회 성공")
        void getAllCategories() {
            // Given
            // 모의 유저(관리자) 생성
            User adminUser = mock(User.class);
            when(adminUser.getRole()).thenReturn(UserRoleEnum.ADMIN);

            BookCategory category1 = mock(BookCategory.class);
            BookCategory category2 = mock(BookCategory.class);
            BookCategory category3 = mock(BookCategory.class);

            // 모의 카테고리 목록 생성
            List<BookCategory> mockCategories = Arrays.asList(
                    category1, category2, category3
            );

            when(bookCategoryRepository.findAll()).thenReturn(mockCategories);

            // When
            List<AdminCategoriesResponseDto> response = adminCategoriesService.getAllCategories(adminUser);

            // Then
            assertNotNull(response);

            // 카테고리 목록을 AdminCategoriesResponseDto 목록으로 변환
            List<AdminCategoriesResponseDto> expectedResponse = mockCategories.stream()
                    .map(AdminCategoriesResponseDto::new)
                    .collect(Collectors.toList());

            assertEquals(expectedResponse.size(), response.size()); // 추가된 부분
        }

        @Test
        @DisplayName("관리자가 아닌 사용자가 조회할 때 예외 발생")
        void getAllCategoriesNonAdminUser() {
            // Given
            // 모의 유저(관리자가 아닌 경우) 생성
            User nonAdminUser = mock(User.class);
            when(nonAdminUser.getRole()).thenReturn(UserRoleEnum.USER);

            // When, Then
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                adminCategoriesService.getAllCategories(nonAdminUser);
            });

            assertEquals("해당 작업을 수행할 권한이 없습니다.", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("카테고리 업데이트")
    class UpdateCategory {

        @Test
        @DisplayName("카테고리 이름 업데이트 성공")
        void updateCategoryName() {
            // Given
            Long categoryId = 1L;
            AdminCategoriesRequestDto adminCategoriesRequestDto = new AdminCategoriesRequestDto();
            adminCategoriesRequestDto.setBookCategoryName("UpdatedCategoryName");

            // 모의 유저(관리자) 생성
            User adminUser = mock(User.class);
            when(adminUser.getRole()).thenReturn(UserRoleEnum.ADMIN);

            // 모의 카테고리 생성
            BookCategory category = mock(BookCategory.class);
            when(bookCategoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

            // When
            ResponseEntity<MessageDto> response = adminCategoriesService.updateBookCategoryName(categoryId, adminCategoriesRequestDto, adminUser);

            // Then
            assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
            assertNotNull(response.getBody());
            assertEquals("카테고리 이름이 수정되었습니다.", response.getBody().getMsg());

            // 부모 카테고리에도 올바른 메서드가 호출되었는지 확인
            verify(category, times(1)).getParentCategory();
            verify(bookCategoryRepository, times(1)).save(category);
        }

        @Test
        @DisplayName("존재하지 않는 카테고리 업데이트할 때 예외 발생")
        void updateNonExistingCategory() {
            // Given
            Long categoryId = 1L;
            AdminCategoriesRequestDto adminCategoriesRequestDto = new AdminCategoriesRequestDto();
            adminCategoriesRequestDto.setBookCategoryName("UpdatedCategoryName");

            // 모의 유저(관리자) 생성
            User adminUser = mock(User.class);
            when(adminUser.getRole()).thenReturn(UserRoleEnum.ADMIN);

            // 존재하지 않는 카테고리를 반환
            when(bookCategoryRepository.findById(categoryId)).thenReturn(Optional.empty());

            // When, Then
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                adminCategoriesService.updateBookCategoryName(categoryId, adminCategoriesRequestDto, adminUser);
            });

            assertEquals("해당 카테고리를 찾을 수 없습니다.", exception.getMessage());
        }

        @Test
        @DisplayName("관리자가 아닌 사용자가 업데이트할 때 예외 발생")
        void updateCategoryNonAdminUser() {
            // Given
            Long categoryId = 1L;
            AdminCategoriesRequestDto adminCategoriesRequestDto = new AdminCategoriesRequestDto();
            adminCategoriesRequestDto.setBookCategoryName("UpdatedCategoryName");

            // 모의 유저(관리자가 아닌 경우) 생성
            User nonAdminUser = mock(User.class);
            when(nonAdminUser.getRole()).thenReturn(UserRoleEnum.USER);

            // When, Then
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                adminCategoriesService.updateBookCategoryName(categoryId, adminCategoriesRequestDto, nonAdminUser);
            });

            assertEquals("해당 작업을 수행할 권한이 없습니다.", exception.getMessage());
        }
    }
    @Nested
    @DisplayName("카테고리 삭제")
    class DeleteCategory {

        @Test
        @DisplayName("카테고리 삭제 성공")
        void deleteCategory() {
            // Given
            Long categoryId = 1L;

            // 모의 유저(관리자) 생성
            User adminUser = mock(User.class);
            when(adminUser.getRole()).thenReturn(UserRoleEnum.ADMIN);

            // 모의 카테고리 생성
            BookCategory category = mock(BookCategory.class);
            when(bookCategoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

            // When
            ResponseEntity<MessageDto> response = adminCategoriesService.deleteBookCategory(categoryId, adminUser);

            // Then
            assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
            assertNotNull(response.getBody());
            assertEquals("카테고리가 삭제되었습니다.", response.getBody().getMsg());

            // 부모 카테고리에서 올바른 메서드가 호출되었는지 확인
            verify(category, times(1)).getParentCategory();
            verify(bookCategoryRepository, times(1)).delete(category);
        }

        @Test
        @DisplayName("존재하지 않는 카테고리 삭제할 때 예외 발생")
        void deleteNonExistingCategory() {
            // Given
            Long categoryId = 1L;

            // 모의 유저(관리자) 생성
            User adminUser = mock(User.class);
            when(adminUser.getRole()).thenReturn(UserRoleEnum.ADMIN);

            // 존재하지 않는 카테고리를 반환
            when(bookCategoryRepository.findById(categoryId)).thenReturn(Optional.empty());

            // When, Then
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                adminCategoriesService.deleteBookCategory(categoryId, adminUser);
            });

            assertEquals("해당 카테고리를 찾을 수 없습니다.", exception.getMessage());
        }

        @Test
        @DisplayName("관리자가 아닌 사용자가 삭제할 때 예외 발생")
        void deleteCategoryNonAdminUser() {
            // Given
            Long categoryId = 1L;

            // 모의 유저(관리자가 아닌 경우) 생성
            User nonAdminUser = mock(User.class);
            when(nonAdminUser.getRole()).thenReturn(UserRoleEnum.USER);

            // When, Then
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                adminCategoriesService.deleteBookCategory(categoryId, nonAdminUser);
            });

            assertEquals("해당 작업을 수행할 권한이 없습니다.", exception.getMessage());
        }
    }
    @Nested
    @DisplayName("도서 카테고리 업데이트")
    class UpdateBookCategory {

        @Test
        @DisplayName("도서 카테고리 업데이트 성공")
        void updateBookCategory() {
            // Given
            Long bookId = 1L;
            Long bookCategoryId = 2L;

            // 모의 유저(관리자) 생성
            User adminUser = mock(User.class);
            when(adminUser.getRole()).thenReturn(UserRoleEnum.ADMIN);

            // 모의 도서 생성
            Book book = mock(Book.class);
            when(adminBooksRepository.findById(bookId)).thenReturn(Optional.of(book));

            // 모의 카테고리 생성
            BookCategory category = mock(BookCategory.class);
            when(bookCategoryRepository.findById(bookCategoryId)).thenReturn(Optional.of(category));

            // When
            ResponseEntity<MessageDto> response = adminCategoriesService.updateBookCategory(bookId, bookCategoryId, adminUser);

            // Then
            assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
            assertNotNull(response.getBody());
            assertEquals("도서의 카테고리가 업데이트되었습니다.", response.getBody().getMsg());

            // 도서의 카테고리 업데이트 메서드가 호출되었는지 확인
            verify(book, times(1)).updateBookCategory(category);
        }

        @Test
        @DisplayName("존재하지 않는 도서 업데이트할 때 예외 발생")
        void updateNonExistingBook() {
            // Given
            Long bookId = 1L;
            Long bookCategoryId = 2L;

            // 모의 유저(관리자) 생성
            User adminUser = mock(User.class);
            when(adminUser.getRole()).thenReturn(UserRoleEnum.ADMIN);

            // 존재하지 않는 도서를 반환
            when(adminBooksRepository.findById(bookId)).thenReturn(Optional.empty());

            // When, Then
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                adminCategoriesService.updateBookCategory(bookId, bookCategoryId, adminUser);
            });

            assertEquals("도서를 찾을 수 없습니다.", exception.getMessage());
        }

        @Test
        @DisplayName("존재하지 않는 카테고리 업데이트할 때 예외 발생")
        void updateNonExistingCategory() {
            // Given
            Long bookId = 1L;
            Long bookCategoryId = 2L;

            // 모의 유저(관리자) 생성
            User adminUser = mock(User.class);
            when(adminUser.getRole()).thenReturn(UserRoleEnum.ADMIN);

            // 모의 도서 생성
            Book book = mock(Book.class);
            when(adminBooksRepository.findById(bookId)).thenReturn(Optional.of(book));

            // 존재하지 않는 카테고리를 반환
            when(bookCategoryRepository.findById(bookCategoryId)).thenReturn(Optional.empty());

            // When, Then
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                adminCategoriesService.updateBookCategory(bookId, bookCategoryId, adminUser);
            });

            assertEquals("해당 카테고리를 찾을 수 없습니다.", exception.getMessage());
        }

        @Test
        @DisplayName("관리자가 아닌 사용자가 업데이트할 때 예외 발생")
        void updateBookCategoryNonAdminUser() {
            // Given
            Long bookId = 1L;
            Long bookCategoryId = 2L;

            // 모의 유저(관리자가 아닌 경우) 생성
            User nonAdminUser = mock(User.class);
            when(nonAdminUser.getRole()).thenReturn(UserRoleEnum.USER);

            // When, Then
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                adminCategoriesService.updateBookCategory(bookId, bookCategoryId, nonAdminUser);
            });

            assertEquals("해당 작업을 수행할 권한이 없습니다.", exception.getMessage());
        }
    }
    @Nested
    @DisplayName("부모 카테고리에서 자식 카테고리 제거")
    class RemoveFromParentCategory {

        @Test
        @DisplayName("부모 카테고리에서 자식 카테고리 제거 성공")
        void removeFromParentCategory() {
            // Given
            BookCategory subCategory = mock(BookCategory.class); // 서브카테고리
            BookCategory parentCategory = mock(BookCategory.class); //상위카테고리
            when(subCategory.getParentCategory()).thenReturn(parentCategory);

            // When
            adminCategoriesService.removeFromParentCategory(subCategory);

            // Then
            verify(parentCategory, times(1)).getChildCategories();
        }

        @Test
        @DisplayName("부모 카테고리가 null인 경우 아무 동작도 하지 않음")
        void removeFromParentCategoryWithNullParent() {
            // Given
            BookCategory category = mock(BookCategory.class);
            when(category.getParentCategory()).thenReturn(null);

            // When
            adminCategoriesService.removeFromParentCategory(category);

            // Then
            // 특별히 어떤 동작도 수행되지 않아야 합니다.
            verify(bookCategoryRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("부모 카테고리 이름 업데이트")
    class UpdateParentCategoryName {

        @Test
        @DisplayName("부모 카테고리 이름 업데이트 성공")
        void updateParentCategoryName() {
            // Given
            BookCategory parentCategory = mock(BookCategory.class);

            // When
            adminCategoriesService.updateParentCategoryName(parentCategory, "NewCategoryName");

            // Then
            verify(parentCategory, times(1)).changeBookCategoryName("NewCategoryName");
            verify(bookCategoryRepository, times(1)).save(parentCategory);
        }

        @Test
        @DisplayName("부모 카테고리가 null인 경우 아무 동작도 하지 않음")
        void updateParentCategoryNameWithNullParent() {
            // Given
            BookCategory parentCategory = null;

            // When
            adminCategoriesService.updateParentCategoryName(parentCategory, "NewCategoryName");

            // Then
            // 특별히 어떤 동작도 수행되지 않아야 합니다.
            verify(bookCategoryRepository, never()).save(any());
        }
    }

}