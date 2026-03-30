package com.horace.toolbackend.service;

import com.horace.toolbackend.entity.api.TimeApiEntity;
import com.horace.toolbackend.enums.DateType;
import com.horace.toolbackend.exception.ThirdApiException;
import com.horace.toolbackend.third.ThirdTimeApiClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

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
        boolean result = apiCheckTimeService.checkTime(LocalDate.of(2026, 1, 24));
        assertThat(result).isTrue();
        verify(thirdTimeApiClient).getTime("2026-01-24");
    }

    @Test
    void Should_Return_True_When_change() {
        when(thirdTimeApiClient.getTime("2026-01-24")).thenReturn(entity(DateType.change));
        boolean result = apiCheckTimeService.checkTime(LocalDate.of(2026, 1, 24));
        assertThat(result).isTrue();
        verify(thirdTimeApiClient).getTime("2026-01-24");
    }

    @Test
    void Should_Return_False_When_holiday_or_weekend() {
        when(thirdTimeApiClient.getTime("2026-01-24")).thenReturn(entity(DateType.holiday));
        boolean holidayResult = apiCheckTimeService.checkTime(LocalDate.of(2026, 1, 24));
        assertThat(holidayResult).isFalse();

        reset(thirdTimeApiClient);
        when(thirdTimeApiClient.getTime("2026-01-24")).thenReturn(entity(DateType.weekend));
        boolean weekendResult = apiCheckTimeService.checkTime(LocalDate.of(2026, 1, 24));
        assertThat(weekendResult).isFalse();

        verify(thirdTimeApiClient).getTime("2026-01-24");
    }

    @Test
    void should_return_true_when_restClient_throws_thirdApiException() {
        when(thirdTimeApiClient.getTime("2026-01-24")).thenThrow(new ThirdApiException("boom"));

        boolean result = apiCheckTimeService.checkTime(LocalDate.of(2026, 1, 24));

        assertThat(result).isTrue();
        verify(thirdTimeApiClient).getTime("2026-01-24");
    }

    @Test
    void should_return_false_when_restClient_formats_date() {
        when(thirdTimeApiClient.getTime(anyString())).thenReturn(entity(DateType.workDay));
        apiCheckTimeService.checkTime(LocalDate.of(2026, 1, 24));

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(thirdTimeApiClient).getTime(captor.capture());
        assertThat(captor.getValue()).isEqualTo("2026-01-24");
    }

    @Test
    void should_return_true_when_entity_or_type_is_null() {
        when(thirdTimeApiClient.getTime(anyString())).thenReturn(null);
        boolean allNullResult = apiCheckTimeService.checkTime(LocalDate.of(2026, 1, 24));
        assertThat(allNullResult).isTrue();

        reset(thirdTimeApiClient);
        when(thirdTimeApiClient.getTime(anyString())).thenReturn(entityWithNullType());
        boolean nullTypeResult = apiCheckTimeService.checkTime(LocalDate.of(2026, 1, 24));
        assertThat(nullTypeResult).isTrue();
    }

    private static TimeApiEntity entity(DateType dateType) {
        TimeApiEntity entity = new TimeApiEntity();
        TimeApiEntity.Type type = new TimeApiEntity.Type();
        type.type = dateType;
        entity.setType(type);
        entity.setCode("0");
        return entity;
    }

    private static TimeApiEntity entityWithNullType() {
        TimeApiEntity entity = new TimeApiEntity();
        entity.setType(null);
        entity.setCode("0");
        return entity;
    }
}
