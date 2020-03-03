package com.acordier.dian.apimanagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

@RestController
@RequestMapping("/apis")
public class ApiController {
    private final ApiService apiService;

    @Autowired
    public ApiController(ApiService apiService) {
        this.apiService = apiService;
    }

    @PostMapping
    public void createApi(
            @RequestBody CreateApiDTO api
    ) {
        apiService.createApi(api);
    }
}
