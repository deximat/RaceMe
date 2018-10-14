package com.codlex.raceme.api;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("login-service")
public interface LoginService {
}
