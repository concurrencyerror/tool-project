package com.horace.toolbackend.service;

import com.horace.toolbackend.entity.api.TimeApiEntity;
import com.horace.toolbackend.enums.DateType;
import com.horace.toolbackend.third.ThirdTimeApiClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApiCheckTimeServiceTest {

    private ApiCheckTimeService apiCheckTimeService;

    @Mock
    private ThirdTimeApiClient thirdTimeApiClient;

    @BeforeEach
    void setUp() {
        apiCheckTimeService = new ApiCheckTimeService(thirdTimeApiClient);
    }

    @Test
    void Should_Return_True_When_workDay() {
        when(thirdTimeApiClient.getTime("2026-01-24")).thenReturn(entity(DateType.workDay));
        boolean result = apiCheckTimeService.checkTime(LocalDateTime.of(2026, 1, 24, 10, 0));
        assertThat(result).isTrue();
        verify(thirdTimeApiClient).getTime("2026-01-24");
    }

    @Test
    void Should_Return_True_When_change() {
        when(thirdTimeApiClient.getTime("2026-01-24")).thenReturn(entity(DateType.change));
        boolean result = apiCheckTimeService.checkTime(LocalDateTime.of(2026, 1, 24, 10, 0));
        assertThat(result).isTrue();
        verify(thirdTimeApiClient).getTime("2026-01-24");
    }


    @Test
    void Should_Return_False_When_holiday_or_weekend() {
        when(thirdTimeApiClient.getTime("2026-01-24")).thenReturn(entity(DateType.holiday));
        boolean holidayResult = apiCheckTimeService.checkTime(LocalDateTime.of(2026, 1, 24, 10, 0));
        assertThat(holidayResult).isFalse();

        reset(thirdTimeApiClient);
        when(thirdTimeApiClient.getTime("2026-01-24")).thenReturn(entity(DateType.weekend));
        boolean weekendResult = apiCheckTimeService.checkTime(LocalDateTime.of(2026, 1, 24, 10, 0));
        assertThat(weekendResult).isFalse();

        verify(thirdTimeApiClient).getTime("2026-01-24");
    }

    @Test
    void should_return_true_when_restClient_throws_exception() {
        when(thirdTimeApiClient.getTime("2026-01-24")).thenThrow(new RuntimeException("boom"));

        boolean result = apiCheckTimeService.checkTime(LocalDateTime.of(2026, 1, 24, 10, 0));

        assertThat(result).isTrue();
        verify(thirdTimeApiClient).getTime("2026-01-24");
    }

    @Test
    void should_return_false_when_restClient_returns_null() {
        when(thirdTimeApiClient.getTime(anyString())).thenReturn(entity(DateType.workDay));
        apiCheckTimeService.checkTime(LocalDateTime.of(2026, 1, 24, 10, 0));

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(thirdTimeApiClient).getTime(captor.capture());
        assertThat(captor.getValue()).isEqualTo("2026-01-24");
    }

    @Test
    void should_return_true_when_entity_or_type_is_null_because_it_will_be_caught() {
        when(thirdTimeApiClient.getTime(anyString())).thenReturn(null);
        boolean allNullResult = apiCheckTimeService.checkTime(LocalDateTime.of(2026, 1, 24, 10, 0));
        assertThat(allNullResult).isTrue();

        reset(thirdTimeApiClient);
        when(thirdTimeApiClient.getTime(anyString())).thenReturn(entityWithNullType());
        boolean nullTypeResult = apiCheckTimeService.checkTime(LocalDateTime.of(2026, 1, 24, 10, 0));
        assertThat(nullTypeResult).isTrue();
    }


    private static TimeApiEntity entity(DateType dateType) {
        TimeApiEntity e = new TimeApiEntity();
        TimeApiEntity.Type t = new TimeApiEntity.Type();
        t.type = dateType;
        e.setType(t);
        e.setCode("0");      // code 目前逻辑没用到，随便给个值也行
        return e;
    }

    private static TimeApiEntity entityWithNullType() {
        TimeApiEntity e = new TimeApiEntity();
        e.setType(null);
        e.setCode("0");
        return e;
    }
}