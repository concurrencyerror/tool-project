package com.horace.toolbackend.service;

import com.horace.toolbackend.repository.RemindRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RemindServiceTest {

    private RemindService remindService;

    @Mock
    private RemindRepository remindRepository;

    @BeforeEach
    void setUp() {
        remindService = new RemindService(remindRepository);
    }

    @Test
    void should_query_records_using_a_single_time_point() {
        LocalDateTime queryTime = LocalDateTime.of(2026, 4, 4, 0, 0);
        when(remindRepository.findActiveAt(queryTime)).thenReturn(List.of());

        remindService.findRemindEntitiesByTime(queryTime);

        verify(remindRepository).findActiveAt(queryTime);
    }
}
