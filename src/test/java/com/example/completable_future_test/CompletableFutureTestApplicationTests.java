package com.example.completable_future_test;

import com.example.completable_future_test.service.AsyncService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootTest
class CompletableFutureTestApplicationTests {

    @Autowired
    private AsyncService asyncService;

    @Test
    void contextLoads() {
        List<Long> seqList = Stream.iterate(1L, n -> n + 1L).limit(100).collect(Collectors.toList());

        List<CompletableFuture<Long>> completableFutureList = seqList.stream()
                .map(seq -> asyncService.doSomething(seq)
                        .exceptionally(throwable -> {
                            System.out.println("throwable = " + throwable);
                            return 1000L;
                        }))
                .collect(Collectors.toList());

        CompletableFuture<List<Long>> listCompletableFuture = CompletableFuture
                .allOf(completableFutureList.toArray(new CompletableFuture[0]))
                .thenApply(unused -> completableFutureList.stream().map(CompletableFuture::join).collect(Collectors.toList()));

        List<Long> join = listCompletableFuture.join();
        System.out.println("join = " + join);
    }

}
