package io.github.melquimartins.todolist.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    private static final String API_DESCRIPTION = String.join(
            " ",
            "API RESTful desenvolvida para o gerenciamento de listas de tarefas.",
            "A aplicação permite criar, consultar, atualizar e remover tarefas,",
            "facilitando a organização e o acompanhamento das atividades dos usuários."
    );

    @Bean
    public OpenAPI configOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("Gerenciador de Lista Tarefas")
                .description(API_DESCRIPTION)
                .version("1.0.0")
        );
    }

}
