package com.horace.toolbackend.api;

import java.time.LocalDateTime;

public interface CheckTimeService {
    boolean checkTime(LocalDateTime time);
}
