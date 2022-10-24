package com.example.completable_future_test.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class AsyncService {

    @Async("getAsyncExecutor")
    public CompletableFuture<Long> doSomething(Long seq){
        log.info("working.. {}", seq);
        if(seq % 10 == 0) throw new IllegalArgumentException("10의 배수는 통과할수 없다. 들어온 값 : " + seq);
        return CompletableFuture.completedFuture(seq);
    }
}
