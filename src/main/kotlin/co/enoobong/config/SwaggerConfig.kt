package co.enoobong.config

import co.enoobong.CrossRideApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.service.Contact
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

@Configuration
@EnableSwagger2
class SwaggerConfig {

    companion object {
        private fun generateApiInfo(): ApiInfo {
            return ApiInfo(
                "Cross Ride",
                "Cross-Ride is a ride-sharing application developed by a startup company. " + "Cross-Ride allows its users to register as drivers and/or riders.",
                "1",
                "urn:tos",
                Contact("Ibanga Enoobong", "https://www.linkedin.com/in/ienoobong/", "ibangaenoobong@yahoo.com"),
                "Apache 2.0",
                "http://www.apache.org/licenses/LICENSE-2.0",
                arrayListOf()
            )
        }
    }

    @Bean
    fun apiDoc(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
            .apiInfo(generateApiInfo())
            .select()
            .apis(RequestHandlerSelectors.basePackage(CrossRideApplication::class.java.`package`.name))
            .paths(PathSelectors.any())
            .build()
    }

}