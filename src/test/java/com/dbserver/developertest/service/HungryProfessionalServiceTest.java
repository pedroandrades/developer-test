package com.dbserver.developertest.service;

import com.dbserver.developertest.dto.HungryProfessionalDTO;
import com.dbserver.developertest.dto.RestaurantDTO;
import com.dbserver.developertest.exception.ExistingHungryProfessionalException;
import com.dbserver.developertest.exception.ExistingRestaurantException;
import com.dbserver.developertest.model.HungryProfessional;
import com.dbserver.developertest.model.Restaurant;
import com.dbserver.developertest.repository.HungryProfessionalRepository;
import com.dbserver.developertest.repository.RestaurantRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class HungryProfessionalServiceTest {

    @Mock
    private HungryProfessionalRepository hungryProfessionalRepository;

    private HungryProfessionalService hungryProfessionalService;

    @Before
    public void setUp(){
        List<HungryProfessional> hungryProfessionalList = new ArrayList<>();
        hungryProfessionalList.add(HungryProfessional.builder().nickname("oldHungryProfessional").build());
        hungryProfessionalService = new HungryProfessionalService(hungryProfessionalRepository);
        when(hungryProfessionalRepository.findAll()).thenReturn(hungryProfessionalList);
    }

    @Test
    public void whenCreateHungryProfessionalItShouldReturnHungryProfessional(){
        HungryProfessionalDTO hungryProfessionalDTO =
                new HungryProfessionalDTO("hungryProfessional", "hungryProfessional", "hungryProfessional");
        HungryProfessional hungryProfessionalExpected = HungryProfessional.builder()
                                                .name("hungryProfessional")
                                                .password("hungryProfessional")
                                                .nickname("hungryProfessional")
                                                .build();

        when(hungryProfessionalRepository.save(any(HungryProfessional.class))).thenReturn(hungryProfessionalExpected);

        HungryProfessional hungryProfessionalCreated =
                hungryProfessionalService.createHungryProfessional(hungryProfessionalDTO);

        assertThat(hungryProfessionalCreated).isEqualToComparingFieldByField(hungryProfessionalExpected);
    }

    @Test(expected = ExistingHungryProfessionalException.class)
    public void whenCreateHungryProfessionalWithAExistingNicknameItShouldReturnException(){
        HungryProfessionalDTO hungryProfessionalDTO =
                new HungryProfessionalDTO("oldHungryProfessional",
                        "oldHungryProfessional", "oldHungryProfessional");

        hungryProfessionalService.createHungryProfessional(hungryProfessionalDTO);

    }
}
