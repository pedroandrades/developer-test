package com.dbserver.developertest.controller;

import com.dbserver.developertest.dto.VoteDTO;
import com.dbserver.developertest.exception.MoreThanOneVotePerDayException;
import com.dbserver.developertest.exception.NotFoundException;
import com.dbserver.developertest.model.HungryProfessional;
import com.dbserver.developertest.model.Vote;
import com.dbserver.developertest.repository.HungryProfessionalRepository;
import com.dbserver.developertest.service.RestaurantService;
import com.dbserver.developertest.service.VoteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.Base64Utils;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration
@WebAppConfiguration
public class VoteControllerTest {

    @MockBean
    private VoteService voteService;

    @Autowired
    protected MockMvc mockMvc;

    private ObjectMapper mapper;

    @Before
    public void setUp() {
        this.mapper = new ObjectMapper();
    }

    @Test
    public void whenCreateVoteItShouldReturnSuccessMessage() throws Exception{
        VoteDTO voteDTO = new VoteDTO("restaurant", "hungry");

        JSONObject expected = new JSONObject();
        expected.put("message", "Your vote has been computed");

        when(voteService.createVote(voteDTO)).thenReturn(any(Vote.class));

        mockMvc.perform(post("/v1/vote")
                .content(mapper.writeValueAsString(voteDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(String.valueOf(expected)));

    }

    @Test
    public void whenCheckWinnerItShouldReturnWinner() throws Exception{
        String winner = "restaurant";

        JSONObject expected = new JSONObject();
        expected.put("message", "restaurant");

        when(voteService.winner()).thenReturn(winner);

        mockMvc.perform(get("/v1/vote")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(String.valueOf(expected)));

    }

    @Test
    public void whenCreateVoteWithANonExistingRestaurantOrHungryProfessionalItShouldReturnErrorMessage() throws Exception{
        VoteDTO voteDTO = new VoteDTO("restaurant", "hungry");

        when(voteService.createVote(voteDTO)).thenThrow(new NotFoundException("Restaurant or Hungry Professional not found"));

        JSONObject expected = new JSONObject();
        expected.put("status", 400);
        expected.put("message", "Restaurant or Hungry Professional not found");

        mockMvc.perform(post("/v1/vote")
                .content(mapper.writeValueAsString(voteDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(String.valueOf(expected)));

    }

    @Test
    public void whenCreateVoteWithAHungryProfessionalThatAlreadyVotedTodayItShouldReturnErrorMessage() throws Exception{
        VoteDTO voteDTO = new VoteDTO("restaurant", "hungry");

        when(voteService.createVote(voteDTO)).thenThrow(new MoreThanOneVotePerDayException("You already voted today"));

        JSONObject expected = new JSONObject();
        expected.put("status", 400);
        expected.put("message", "You already voted today");

        mockMvc.perform(post("/v1/vote")
                .content(mapper.writeValueAsString(voteDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(String.valueOf(expected)));
    }
}
