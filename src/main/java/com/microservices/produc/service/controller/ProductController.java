package com.microservices.produc.service.controller;

import com.microservices.produc.service.dto.ProductRequest;
import com.microservices.produc.service.dto.ProductResponse;
import com.microservices.produc.service.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("api/product")
public class ProductController {

    int count=0;
    @Autowired
    ProductService productService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public void createProduct(@RequestBody ProductRequest productRequest) {
        productService.createProduct(productRequest);
    }


    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> getAllProduct() {
        return productService.getAllProduct();
    }

    @CrossOrigin(
            origins = "http://localhost:4200",
            methods = {RequestMethod.GET},
            allowCredentials = "true"
    )
    @GetMapping(value = "/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter getEvents() {

        SseEmitter emitter = new SseEmitter();

        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(() -> {
            try {
                emitter.send(SseEmitter.event()
                                     .data("Event data this should be immited: "+ LocalDateTime.now())
                                     .comment("This is a comment"));
            } catch (IOException e) {
                // Handle the exception and complete the emitter with an error
                emitter.completeWithError(e);
            }
        }, 0, 1, TimeUnit.SECONDS);

        return emitter;
    }


    @CrossOrigin(
            origins = "http://localhost:4200",
            methods = {RequestMethod.GET},
            allowCredentials = "true"
    )
    @GetMapping(value = "/stream-sse")
    public Flux<ServerSentEvent<String>> streamEvents() {
        return Flux.interval(Duration.ofSeconds(1))
                       .map(sequence -> ServerSentEvent.<String>builder()
                                                .id(String.valueOf(sequence))
                                                .data("SSE - " + LocalTime.now().toString())
                                                .event("periodic-event")
                                                .build());
    }
}
