package com.chatty.config;

import static java.util.stream.Collectors.groupingBy;

import com.chatty.dto.ErrorReason;
import com.chatty.dto.ErrorResponse;
import com.chatty.swagger.ApiErrorCodeExample;
import com.chatty.swagger.BaseErrorCode;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import io.swagger.v3.oas.models.servers.Server;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI(){

        String jwtSchemeName = "jwt";
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwtSchemeName);

        Server server = new Server();
        server.setUrl("https://dev.api.chattylab.org");

        Components components = new Components()
                .addSecuritySchemes(jwtSchemeName, new SecurityScheme()
                        .name(jwtSchemeName)
                        .type(Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                );

        return new OpenAPI()
                .info(apiInfo())
                .addSecurityItem(securityRequirement)
                .components(components)
                .servers(List.of(server));
    }

    @Bean
    public OperationCustomizer customize(){
        return (Operation operation, HandlerMethod handlerMethod) -> {
            ApiErrorCodeExample apiErrorCodeExample =
                    handlerMethod.getMethodAnnotation(ApiErrorCodeExample.class);

            if(apiErrorCodeExample != null){
                generateErrorCodeResponseExample(operation, apiErrorCodeExample.value());
            }

            return operation;
        };
    }

    private void generateErrorCodeResponseExample(Operation operation, Class<? extends ErrorResponse> type){
        ApiResponses responses = operation.getResponses();

        BaseErrorCode[] errorCodes = (BaseErrorCode[]) type.getEnumConstants();

        Map<Integer, List<ExampleHolder>> statusWithExampleHolders =
                Arrays.stream((errorCodes))
                        .map(
                                baseErrorCode -> {
                                    try {
                                        ErrorReason errorReason = baseErrorCode.getErrorReason();
                                        return ExampleHolder.builder()
                                                .holder(
                                                        getSwaggerExample(
                                                                baseErrorCode.getExplainError(),
                                                                errorReason
                                                        )
                                                )
                                                .code(errorReason.getStatus().value())
                                                .name(errorReason.getCode())
                                                .build();

                                    } catch (NoSuchFieldException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                        ).collect(groupingBy(ExampleHolder::getCode));
        addExamplesToResponses(responses, statusWithExampleHolders);
    }

    @Getter
    @Builder
    public static class ExampleHolder{
        private Example holder;
        private String name;
        private int code;
    }

    private Example getSwaggerExample(String value, ErrorReason errorReason){

        ErrorResponse errorResponse = ErrorResponse.of(errorReason.getReason());
        Example example = new Example();
        example.description(value);
        example.setValue(errorResponse);
        return example;
    }

    private void addExamplesToResponses(ApiResponses responses, Map<Integer, List<ExampleHolder>> statusWithExampleHolders) {
        statusWithExampleHolders.forEach(
                (status, v) -> {
                    Content content = new Content();
                    MediaType mediaType = new MediaType();
                    ApiResponse apiResponse = new ApiResponse();

                    v.forEach(
                            exampleHolder -> mediaType.addExamples(
                                    exampleHolder.getName(), exampleHolder.getHolder())
                    );

                    content.addMediaType("application/json", mediaType);
                    apiResponse.setContent(content);

                    responses.addApiResponse(status.toString(), apiResponse);
                }

        );
    }

    private Info apiInfo(){
        return new Info()
                .title("Chatty")
                .description("Chatty-api")
                .version("1.0.0");
    }
}
