package com.dbserver.developertest.controller;

import com.dbserver.developertest.dto.RestaurantDTO;
import com.dbserver.developertest.dto.VoteDTO;
import com.dbserver.developertest.exception.ExistingHungryProfessionalException;
import com.dbserver.developertest.exception.MoreThanOneVotePerDayException;
import com.dbserver.developertest.exception.NotFoundException;
import com.dbserver.developertest.response.Response;
import com.dbserver.developertest.service.VoteService;
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

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/vote")
public class VoteController {

    private VoteService voteService;

    @Autowired
    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    @ApiOperation(value = "Insert a vote")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Your vote has been computed"),
            @ApiResponse(code = 400, message = "You already voted today."),
            @ApiResponse(code = 400, message = "Restaurant or Hungry Professional not found."),
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> create(@Valid @RequestBody VoteDTO voteDTO){
        voteService.createVote(voteDTO);
        return ResponseEntity.ok(new Response("Your vote has been computed"));
    }

    @ApiOperation(value = "Check who is the winner today")
    @GetMapping()
    public ResponseEntity<Response> winner(){
        return ResponseEntity.ok(new Response(voteService.winner()));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<JsonNode> handleException(NotFoundException e) {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        ObjectNode jsonNode = new ObjectMapper().createObjectNode();
        jsonNode.put("status", badRequest.value());
        jsonNode.put("message", e.getMessage());
        return ResponseEntity.status(badRequest).body(jsonNode);
    }

    @ExceptionHandler(MoreThanOneVotePerDayException.class)
    public ResponseEntity<JsonNode> handleException(MoreThanOneVotePerDayException e) {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        ObjectNode jsonNode = new ObjectMapper().createObjectNode();
        jsonNode.put("status", badRequest.value());
        jsonNode.put("message", e.getMessage());
        return ResponseEntity.status(badRequest).body(jsonNode);
    }
}
