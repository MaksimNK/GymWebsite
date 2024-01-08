package com.maksimnk.gym_website.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.Formatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Configuration
public class WebConfig {
    @Bean
    public Formatter<Date> dateFormatter() {
        return new Formatter<Date>() {
            @Override
            public Date parse(String text, Locale locale) throws ParseException {
                return new SimpleDateFormat("yyyy-MM-dd").parse(text);
            }

            @Override
            public String print(Date object, Locale locale) {
                return new SimpleDateFormat("yyyy-MM-dd").format(object);
            }
        };
    }
}
