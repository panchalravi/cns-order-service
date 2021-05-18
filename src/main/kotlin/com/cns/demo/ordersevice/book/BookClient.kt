package com.cns.demo.ordersevice.book

import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientResponseException
import org.springframework.web.reactive.function.client.bodyToMono
import org.springframework.web.util.UriBuilder
import org.springframework.web.util.UriComponentsBuilder
import reactor.core.publisher.Mono
import reactor.util.retry.Retry
import java.time.Duration

@Service
class BookClient(
    private final val bookClientProperties: BookClientProperties,
    private final val webClientBuilder: WebClient.Builder
) {

    var webClient: WebClient = webClientBuilder.baseUrl(bookClientProperties.catalogServiceUrl.toString())
        .build()

    fun getBookByIsbn(isbn: String): Mono<Book>  {
        return webClient.get().uri { it.path("/books/{isbn}").build(isbn) }
            .retrieve()
            .bodyToMono<Book>()
            .timeout(Duration.ofSeconds(1), Mono.empty())
            .onErrorResume(WebClientResponseException.NotFound::class.java) { Mono.empty()  } // don't retry for 404 error
            .retryWhen(Retry.backoff(3, Duration.ofMillis(100)))
    }
}
