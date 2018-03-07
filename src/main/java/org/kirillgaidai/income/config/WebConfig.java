package org.kirillgaidai.income.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@EnableWebMvc
@Configuration
@ComponentScan(value =
        {"org.kirillgaidai.income.web","org.kirillgaidai.income.rest", "org.kirillgaidai.income.exception"})
public class WebConfig extends WebMvcConfigurerAdapter {

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        registry.viewResolver(new InternalResourceViewResolver("/WEB-INF/pages/", ".jsp"));
    }

    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler(
                "/index.html", "/favicon.ico",
                "/styles.*.bundle.css",
                "/inline.*.bundle.js", "/main.*.bundle.js", "/polyfills.*.bundle.js",
                "/glyphicons-halflings-regular.*.eot", "/glyphicons-halflings-regular.*.woff",
                "/glyphicons-halflings-regular.*.woff2", "/glyphicons-halflings-regular.*.svg",
                "/glyphicons-halflings-regular.*.ttf"
        ).addResourceLocations("/resources/");
    }

}
