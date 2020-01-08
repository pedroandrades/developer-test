package com.dbserver.developertest.controller;

import com.dbserver.developertest.dto.HungryProfessionalDTO;
import com.dbserver.developertest.exception.ExistingHungryProfessionalException;
import com.dbserver.developertest.service.HungryProfessionalService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.dbserver.developertest.response.Response;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/hungry-professional")
public class HungryProfessionalController {

    private HungryProfessionalService hungryProfessionalService;

    @Autowired
    public HungryProfessionalController(HungryProfessionalService hungryProfessionalService) {
        this.hungryProfessionalService = hungryProfessionalService;
    }

    @ApiOperation(value = "Create a Hungry Professional")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Hungry Professional Created"),
            @ApiResponse(code = 400, message = "Hungry Professional already exists"),
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> create(@Valid @RequestBody HungryProfessionalDTO hungryProfessionalDTO){
        hungryProfessionalService.createHungryProfessional(hungryProfessionalDTO);
        return ResponseEntity.ok(new Response("Hungry Professional Created"));
    }


    @ExceptionHandler(ExistingHungryProfessionalException.class)
    public ResponseEntity<JsonNode> handleException(ExistingHungryProfessionalException e) {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        ObjectNode jsonNode = new ObjectMapper().createObjectNode();
        jsonNode.put("status", badRequest.value());
        jsonNode.put("message", e.getMessage());
        return ResponseEntity.status(badRequest).body(jsonNode);
    }
}
