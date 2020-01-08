package com.dbserver.developertest.controller;

import com.dbserver.developertest.dto.HungryProfessionalDTO;
import com.dbserver.developertest.exception.ExistingHungryProfessionalException;
import com.dbserver.developertest.model.HungryProfessional;
import com.dbserver.developertest.service.HungryProfessionalService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class HungryProfessionalControllerTest {

    @MockBean
    private HungryProfessionalService hungryProfessionalService;

    @Autowired
    protected MockMvc mockMvc;

    private ObjectMapper mapper;

    @Before
    public void setUp() {
        this.mapper = new ObjectMapper();
    }

    @Test
    public void whenCreateHungryProfessionalItShouldReturnSuccessMessage() throws Exception {
        HungryProfessionalDTO hungryProfessionalDTO =
                new HungryProfessionalDTO("hungryProfessional", "hungryProfessional", "hungryProfession");

        HungryProfessional hungryProfessionalExpected = HungryProfessional.builder()
                .id(1L)
                .name("hungryProfessional")
                .password("hungryProfessional")
                .nickname("hungryProfessional")
                .build();

        when(hungryProfessionalService.createHungryProfessional(any(HungryProfessionalDTO.class))).thenReturn(hungryProfessionalExpected);

        JSONObject expected = new JSONObject();
        expected.put("message", "Hungry Professional Created");

        mockMvc.perform(post("/v1/hungry-professional")
                .content(mapper.writeValueAsString(hungryProfessionalDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(String.valueOf(expected)));
    }

    @Test
    public void whenCreateHungryProfessionalWithAExistingNicknameItShouldReturnErrorMessage() throws Exception {
        HungryProfessionalDTO hungryProfessionalDTO =
                new HungryProfessionalDTO("hungryProfessional", "hungryProfessional", "hungryProfession");

        when(hungryProfessionalService.createHungryProfessional(any(HungryProfessionalDTO.class)))
                .thenThrow(new ExistingHungryProfessionalException("Hungry Professional already exists"));

        JSONObject expected = new JSONObject();
        expected.put("status", 400);
        expected.put("message", "Hungry Professional already exists");

        mockMvc.perform(post("/v1/hungry-professional")
                .content(mapper.writeValueAsString(hungryProfessionalDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(String.valueOf(expected)));

    }
}
