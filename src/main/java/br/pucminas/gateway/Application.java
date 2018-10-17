package br.pucminas.gateway;

import reactor.core.publisher.Mono;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// tag::code[]
@SpringBootApplication
@EnableConfigurationProperties(UriConfiguration.class)
@RestController
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    // tag::route-locator[]
    @Bean
    public RouteLocator myRoutes(RouteLocatorBuilder builder, UriConfiguration uriConfiguration) {

        String httpUri = uriConfiguration.getHttpbin();

        String uriBiblioteca = "https://gestao-livro-aula3.herokuapp.com/api/v1/";
        String uriAutenticacao = "https://dry-mesa-35311.herokuapp.com/v1/public/";
        String uriPagamento = "https://credit-card-service-diegoapp.herokuapp.com/api";
        String uriAuditoria = "https://app-auditoria-aula3.herokuapp.com/";
        
        String uriBaseGatewayBiblioteca = "/v1/gateway/bilioteca/";
        
        return builder.routes() 
                
                // biblioteca
                .route(p -> p.path(uriBaseGatewayBiblioteca + "livros").uri(uriBiblioteca + "livros"))
                .route(p -> p.path(uriBaseGatewayBiblioteca + "livros/{idLivro}").uri(uriBiblioteca + "livros/{idLivro}"))
                .route(p -> p.path(uriBaseGatewayBiblioteca + "livros/{idLivro}/autores").uri(uriBiblioteca + "livros/{idLivro}/autores"))
                .route(p -> p.path(uriBaseGatewayBiblioteca + "livros/{idLivro}/editoras").uri(uriBiblioteca + "livros/{idLivro}/editoras"))
                .route(p -> p.path(uriBaseGatewayBiblioteca + "livros/{idLivro}/comentarios").uri(uriBiblioteca + "livros/{idLivro}/comentarios"))
                
                .route(p -> p.path(uriBaseGatewayBiblioteca + "autores").uri(uriBiblioteca + "bilioteca/autores"))
                .route(p -> p.path(uriBaseGatewayBiblioteca + "autores/{idAutor}").uri(uriBiblioteca + "bilioteca/autores/{idAutor}"))
                .route(p -> p.path(uriBaseGatewayBiblioteca + "autores/{idAutor}/livros").uri(uriBiblioteca + "bilioteca/autores/{idAutor}/livros"))
                .route(p -> p.path(uriBaseGatewayBiblioteca + "autores/{idAutor}/editoras").uri(uriBiblioteca + "bilioteca/autores/{idAutor}/editoras"))
                
                .route(p -> p.path(uriBaseGatewayBiblioteca + "editoras").uri(uriBiblioteca + "bilioteca/editoras"))
                .route(p -> p.path(uriBaseGatewayBiblioteca + "editoras/{idEditora}").uri(uriBiblioteca + "bilioteca/editoras/{idEditora}"))
                .route(p -> p.path(uriBaseGatewayBiblioteca + "editoras/{idEditora}/livros").uri(uriBiblioteca + "bilioteca/editoras/{idEditora}/livros"))
                .route(p -> p.path(uriBaseGatewayBiblioteca + "editoras/{idEditora}/autores").uri(uriBiblioteca + "bilioteca/editoras/{idEditora}/autores"))

                .route(p -> p.path(uriBaseGatewayBiblioteca + "clientes").uri(uriBiblioteca + "bilioteca/clientes"))
                .route(p -> p.path(uriBaseGatewayBiblioteca + "clientes/{idCliente}").uri(uriBiblioteca + "bilioteca/clientes/{idCliente}"))
                .route(p -> p.path(uriBaseGatewayBiblioteca + "clientes/{idCliente}/pedidos").uri(uriBiblioteca + "bilioteca/clientes/{idCliente}/pedidos"))
                
                .route(p -> p.path(uriBaseGatewayBiblioteca + "carrinhoCompra/{idLivro}").uri(uriBiblioteca + "bilioteca/carrinhoCompra/{idLivro}"))
                
                .route(p -> p.path(uriBaseGatewayBiblioteca + "pedido").uri(uriBiblioteca + "bilioteca/pedido"))
                .route(p -> p.path(uriBaseGatewayBiblioteca + "pedido/{idPedido}").uri(uriBiblioteca + "bilioteca/pedido/{idPedido}"))
                
                // autenticacao TODO
                .route(p -> p.path("/v1/gateway/bilioteca/livro").uri(uriAutenticacao + "/get"))
                
                // pagamento TODO
                .route(p -> p.path("/v1/gateway/bilioteca/livro").uri(uriPagamento + "/get"))
                
                // auditoria TODO
                .route(p -> p.path("/v1/gateway/bilioteca/livro").uri(uriAuditoria + "/get"))
                
                .route(p -> p.path("/v1/public/bilioteca/livro").uri(httpUri + "/get"))
                .route(p -> p.path("/v1/public/bilioteca/revista").uri(httpUri + "/get"))
                .route(p -> p.path("/get").filters(f -> f.addRequestHeader("Hello", "World")).uri(httpUri))
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

// tag::uri-configuration[]
@ConfigurationProperties
class UriConfiguration {

    private String httpbin = "http://httpbin.org:80";

    public String getHttpbin() {
        return httpbin;
    }

    public void setHttpbin(String httpbin) {
        this.httpbin = httpbin;
    }
}
// end::uri-configuration[]
// end::code[]
