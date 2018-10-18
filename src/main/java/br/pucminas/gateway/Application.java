package br.pucminas.gateway;

import reactor.core.publisher.Mono;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// tag::code[]
@SpringBootApplication
@RestController
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    // tag::route-locator[]
    @Bean
    public RouteLocator myRoutes(RouteLocatorBuilder builder) {

        
        String uriBiblioteca = "https://gestao-livro-aula3.herokuapp.com";
        String uriAutenticacao = "https://dry-mesa-35311.herokuapp.com";
        String uriPagamento = "https://credit-card-service-diegoapp.herokuapp.com";
        String uriAuditoria = "https://app-auditoria-aula3.herokuapp.com";
        
        String uriBaseGatewayBiblioteca = "/v1/gateway/bilioteca/";
        String uriBaseGatewayAutenticacao = "/v1/gateway/autenticacao/";
        String uriBaseGatewayPagamento = "/v1/gateway/pagamento/";
        String uriBaseGatewayAuditoria = "/v1/gateway/auditoria/";
        
        String uriPrefixoBiblioteca = "/api/v1/";
        String uriPrefixoAutenticacao = "/v1/public/";
        String uriPrefixoPagamento = "/api/v1/";
        String uriPrefixoAuditoria = "/api/v1/";
        
        return builder.routes() 
                
                // biblioteca
                .route("rota para API biblioteca", p -> p
                        .path(uriBaseGatewayBiblioteca + "**")
                        .filters(f->f.rewritePath(uriBaseGatewayBiblioteca + "(?<path>.*)",uriPrefixoBiblioteca + "${path}"))
                        .uri(uriBiblioteca))
                
                // autenticacao
                .route("rota autenticacao", p -> p
                        .path(uriBaseGatewayAutenticacao + "**")
                        .filters(f->f.rewritePath(uriBaseGatewayAutenticacao + "(?<path>.*)",uriPrefixoAutenticacao + "${path}"))
                        .uri(uriAutenticacao))
                
                // pagamento
                .route("rota pagamento", p -> p
                        .path(uriBaseGatewayPagamento + "**")
                        .filters(f->f.rewritePath(uriBaseGatewayPagamento + "(?<path>.*)",uriPrefixoPagamento + "${path}"))
                        .uri(uriPagamento))
                
                // auditoria
                .route("rota auditoria", p -> p
                        .path(uriBaseGatewayAuditoria + "**")
                        .filters(f->f.rewritePath(uriBaseGatewayAuditoria + "(?<path>.*)",uriPrefixoAuditoria + "${path}"))
                        .uri(uriAuditoria))
                
                .route("circuit breakers", p -> p
                    .host("*.hystrix.com")
                    .filters(f -> f.hystrix(config -> config
                    .setName("mycmd")
                    .setFallbackUri("forward:/fallback")))                
                    .uri(uriBiblioteca))
                .build();
    }
    // end::route-locator[]

    // tag::fallback[]
    @RequestMapping("/fallback")
    public Mono<String> fallback() {
        return Mono.just("fallback");
    }
    // end::fallback[]
}
 
// end::uri-configuration[]
// end::code[]
