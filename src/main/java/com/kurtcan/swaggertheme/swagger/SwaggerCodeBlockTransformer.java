package com.kurtcan.swaggertheme.swagger;

import jakarta.servlet.http.HttpServletRequest;
import org.jspecify.annotations.NonNull;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springdoc.core.properties.SwaggerUiOAuthProperties;
import org.springdoc.core.providers.ObjectMapperProvider;
import org.springdoc.webmvc.ui.SwaggerIndexPageTransformer;
import org.springdoc.webmvc.ui.SwaggerWelcomeCommon;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.resource.ResourceTransformerChain;
import org.springframework.web.servlet.resource.TransformedResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;


public class SwaggerCodeBlockTransformer extends SwaggerIndexPageTransformer {

    /**
     * Instantiates a new Swagger index transformer.
     *
     * @param swaggerUiConfig          the swagger ui config
     * @param swaggerUiOAuthProperties the swagger ui o auth properties
     * @param swaggerWelcomeCommon     the swagger welcome common
     * @param objectMapperProvider     the object mapper provider
     */
    public SwaggerCodeBlockTransformer(
        SwaggerUiConfigProperties swaggerUiConfig,
        SwaggerUiOAuthProperties swaggerUiOAuthProperties,
        SwaggerWelcomeCommon swaggerWelcomeCommon,
        ObjectMapperProvider objectMapperProvider
    ) {
        super(swaggerUiConfig, swaggerUiOAuthProperties, swaggerWelcomeCommon, objectMapperProvider);
    }

    @Override
    public @NonNull Resource transform(
        HttpServletRequest request,
        Resource resource,
        ResourceTransformerChain transformer
    ) throws IOException {
        if (resource.toString().contains("swagger-ui.css")) {
            try (
                InputStream originalIs = resource.getInputStream();
                BufferedReader originalBr = new BufferedReader(new InputStreamReader(originalIs));
                InputStream customIs = getClass().getClassLoader().getResourceAsStream("static/swagger/one-dark.css");
                BufferedReader customBr = new BufferedReader(new InputStreamReader(customIs))
            ) {
                String originalCss = originalBr.lines().collect(Collectors.joining());
                String customCss = customBr.lines().collect(Collectors.joining());
                byte[] transformedContent = (originalCss + customCss).getBytes();
                return new TransformedResource(resource, transformedContent);
            }
        }
        return super.transform(request, resource, transformer);
    }
}