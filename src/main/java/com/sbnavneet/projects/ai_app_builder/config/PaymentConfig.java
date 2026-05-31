package com.sbnavneet.projects.ai_app_builder.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.stripe.Stripe;

import jakarta.annotation.PostConstruct;

@Configuration
public class PaymentConfig {

    @Value("${stripe.secret-key}")
    private String STRIPE_SECRET_KEY;

    @PostConstruct
    public void init(){
        Stripe.apiKey = STRIPE_SECRET_KEY;
    }


}
