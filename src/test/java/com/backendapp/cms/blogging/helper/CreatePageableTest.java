package com.backendapp.cms.blogging.helper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CreatePageableTest {

    @Autowired
    private final CreatePageable createPageable = new CreatePageable();

    @Test
    @DisplayName("Should return PageRequest when given pagination parameters")
    void get_shouldReturnPageRequestWhenGivenPaginationParameters() {
        int page = 1;
        int limit = 10;
        Sort sort = Sort.by(Sort.Direction.DESC, "PUBLISHED");
        PageRequest pageRequest = PageRequest.of(page, limit, sort);

        PageRequest actual = createPageable.get("PUBLISHED", Sort.Direction.DESC, page, limit);

        assertEquals(actual, pageRequest);
    }
}
