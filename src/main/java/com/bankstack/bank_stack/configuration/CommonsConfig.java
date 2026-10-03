package com.bankstack.bank_stack.configuration;

import com.commons.exception.GlobalExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(GlobalExceptionHandler.class)
public class CommonsConfig {
}
