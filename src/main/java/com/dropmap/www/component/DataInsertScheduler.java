package com.dropmap.www.component;

import com.dropmap.www.service.DataProcessorService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@RequiredArgsConstructor
@Component
public class DataInsertScheduler {
    private final DataProcessorService dataProcessorService;

    //@Scheduled(cron = "0 0 3 15 * *") // 매달 15일 03:00
    @PostConstruct//for test init onetime
    private void run() throws JsonProcessingException, InterruptedException, SQLException {
        dataProcessorService.init();
    }
}
