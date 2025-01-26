package ru.javaops.cloudjava.aggregateservice.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.javaops.cloudjava.aggregateservice.service.AggregateService;

@Tag(name = "MenuAggregateController", description = "REST API для работы с блюдами.")
@Slf4j
@RestController
@RequestMapping("/v1/menu-aggregate")
@RequiredArgsConstructor
public class MenuAggregateController {

    private final AggregateService aggregateService;
}
