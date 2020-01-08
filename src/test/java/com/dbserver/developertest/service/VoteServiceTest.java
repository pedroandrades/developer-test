package com.dbserver.developertest.service;

import com.dbserver.developertest.dto.VoteDTO;
import com.dbserver.developertest.exception.NotFoundException;
import com.dbserver.developertest.model.HungryProfessional;
import com.dbserver.developertest.model.Restaurant;
import com.dbserver.developertest.model.Vote;
import com.dbserver.developertest.repository.HungryProfessionalRepository;
import com.dbserver.developertest.repository.RestaurantRepository;
import com.dbserver.developertest.repository.VoteRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class VoteServiceTest {

    @Mock
    private VoteRepository voteRepository;

    @Mock
    private HungryProfessionalRepository hungryProfessionalRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    private VoteService voteService;

    @Before
    public void setUp() {
        voteService = new VoteService(voteRepository, hungryProfessionalRepository, restaurantRepository);
    }

    @Test
    public void whenCreateVoteItShouldReturnVote(){
        VoteDTO voteDTO = new VoteDTO("restaurant", "professional");
        Optional<HungryProfessional> hungryProfessionalExpected = Optional.ofNullable(HungryProfessional.builder().nickname("professional").build());
        Optional<Restaurant> restaurantExpected = Optional.ofNullable(Restaurant.builder().name("restaurant").build());

        Vote voteExpected = Vote.builder().
                            hungryProfessional(hungryProfessionalExpected.get())
                           .restaurant(restaurantExpected.get())
                           .voteDate(LocalDate.now()).build();

        when(voteRepository.save(any(Vote.class))).thenReturn(voteExpected);
        when(hungryProfessionalRepository.findByNicknameEquals(any(String.class))).thenReturn(hungryProfessionalExpected);
        when(restaurantRepository.findByNameEquals(any(String.class))).thenReturn(restaurantExpected);

        Vote voteCreated = voteService.createVote(voteDTO);

        assertThat(voteCreated).isEqualToComparingFieldByField(voteExpected);

    }

    @Test(expected = NotFoundException.class)
    public void whenCreateVoteWithoutExistingRestaurantOrHungryProfessionalItShouldReturnException(){
        VoteDTO voteDTO = new VoteDTO("restaurant", "professional");

        voteService.createVote(voteDTO);

    }
}
