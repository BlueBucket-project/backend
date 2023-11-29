package com.example.shopping.config.swagger;

import org.springframework.boot.actuate.autoconfigure.endpoint.web.CorsEndpointProperties;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.boot.actuate.autoconfigure.web.server.ManagementPortType;
import org.springframework.boot.actuate.endpoint.ExposableEndpoint;
import org.springframework.boot.actuate.endpoint.web.*;
import org.springframework.boot.actuate.endpoint.web.annotation.ControllerEndpointsSupplier;
import org.springframework.boot.actuate.endpoint.web.annotation.ServletEndpointsSupplier;
import org.springframework.boot.actuate.endpoint.web.servlet.WebMvcEndpointHandlerMapping;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.OAS_30)
                // ApiSelectorBuilder 생성
                .select()
                // api 문서화를 할 패키지 경로
                .apis(RequestHandlerSelectors.basePackage("com.example.shopping.controller"))
                // 어떤 API URL에 대해서 문서화할지
                // PathSelectors.any() : 모든 API에 대한 URL을 문서화
                // PathSelectors.ant("/member/**") : 특정 URL을 지정해서 문서화
                .paths(PathSelectors.any())
                .build()
                // API 문서에 대한 정보 추가
                .apiInfo(apiInfo())
                // swagger에서 제공하는 기본 응답 코드 설명 제거
                .useDefaultResponseMessages(false);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("API 문서")
                .description("구현한 사이트의 기능을 보여주는 곳입니다.")
                .version("1.0")
                .build();
    }

    @Bean
    public WebMvcEndpointHandlerMapping webEndpointServletHandlerMapping(
            // 웹 엔드포인트
            WebEndpointsSupplier webEndpointsSupplier,
            // 서블릿 엔드포인트
            ServletEndpointsSupplier servletEndpointsSupplier,
            // 컨트롤러 엔드포인트
            ControllerEndpointsSupplier controllerEndpointsSupplier,
            // 엔드포인트의 미디어 타입
            EndpointMediaTypes endpointMediaTypes,
            // CORS 설정
            CorsEndpointProperties corsProperties,
            // 웹 엔드포인트 설정
            WebEndpointProperties webEndpointProperties,
            // 환경과 관련된 빈이나 속성
            Environment environment) {
        List<ExposableEndpoint<?>> allEndpoints = new ArrayList();
        // 액추에이터(Actuator)에서 사용자가 정의한 웹 엔드포인트를 가져오는 메서드
        // Actuator는 기본적으로 다양한 관리 엔드포인트를 제공하지만,
        // 사용자가 필요에 따라 커스텀한 엔드포인트를 추가할 수 있습니다.
        Collection<ExposableWebEndpoint> webEndpoints = webEndpointsSupplier.getEndpoints();
        allEndpoints.addAll(webEndpoints);
        // 액추에이터(Actuator)에서 제공하는 서블릿 엔드포인트 목록을 가져오는 메서드
        // 액추에이터는 기본적으로 서블릿 엔드포인트를 제공하여 애플리케이션의 상태, 건강, 정보 등을 노출할 수 있게 합니다.
        // 예를 들어, /actuator/health 엔드포인트는 애플리케이션의 건강 상태를 제공하고,
        // /actuator/info 엔드포인트는 추가적인 사용자 지정 정보를 제공
        allEndpoints.addAll(servletEndpointsSupplier.getEndpoints());
        // 액추에이터(Actuator)에서 제공하는 컨트롤러 엔드포인트 목록을 가져오는 메서드
        allEndpoints.addAll(controllerEndpointsSupplier.getEndpoints());
        // 액추에이터(Actuator) 엔드포인트의 기본 경로를 가져오는 메서드
        // 예를 들어, /actuator가 기본 경로인 경우, 이 메서드는 /actuator를 반환
        String basePath = webEndpointProperties.getBasePath();
        EndpointMapping endpointMapping = new EndpointMapping(basePath);
        boolean shouldRegisterLinksMapping = this.shouldRegisterLinksMapping(webEndpointProperties, environment, basePath);
        return new WebMvcEndpointHandlerMapping(endpointMapping, webEndpoints, endpointMediaTypes, corsProperties.toCorsConfiguration(), new EndpointLinksResolver(allEndpoints, basePath), shouldRegisterLinksMapping, null);
    }


    // 주어진 조건에 따라 액추에이터의 링크 매핑을 등록해야 하는지 여부를 결정
    private boolean shouldRegisterLinksMapping(WebEndpointProperties webEndpointProperties,
                                               Environment environment,
                                               String basePath) {
        // WebEndpointProperties에서 가져온 discovery 속성이 활성화되어 있는지 확인합니다.
        // 이 속성이 true인 경우에만 링크 매핑이 등록됩니다.
        // 그리고 다음 조건은 basePath가 비어 있지 않은 경우입니다. basePath는 액추에이터의 기본 경로입니다.
        //  ManagementPortType이 DIFFERENT인 경우입니다.
        return webEndpointProperties
                .getDiscovery()
                .isEnabled() &&
                (StringUtils.hasText(basePath)
                        || ManagementPortType.get(environment).equals(ManagementPortType.DIFFERENT));
    }
}
