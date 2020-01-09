package com.dbserver.developertest.service;

import com.dbserver.developertest.dto.VoteDTO;
import com.dbserver.developertest.exception.MoreThanOneVotePerDayException;
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
import java.util.ArrayList;
import java.util.List;
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

    @Mock
    private RestaurantService restaurantService;

    private VoteService voteService;

    @Before
    public void setUp() {
        List<Vote> voteList = new ArrayList<>();
        voteList.add(Vote.builder()
                .hungryProfessional(HungryProfessional.builder().nickname("old").build())
                .voteDate(LocalDate.now())
                .restaurant(Restaurant.builder().name("restaurant1").build())
                .build());
        voteList.add(Vote.builder()
                .hungryProfessional(HungryProfessional.builder().nickname("new").build())
                .voteDate(LocalDate.now())
                .restaurant(Restaurant.builder().name("restaurant1").build())
                .build());
        voteList.add(Vote.builder()
                .hungryProfessional(HungryProfessional.builder().nickname("prof").build())
                .voteDate(LocalDate.now())
                .restaurant(Restaurant.builder().name("restaurant2").build())
                .build());
        when(voteRepository.findAll()).thenReturn(voteList);
        voteService = new VoteService(voteRepository, hungryProfessionalRepository, restaurantRepository, restaurantService);
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

    @Test
    public void whenCheckWinnerItShouldReturnWinner(){
        String winner = voteService.winner();

        assertThat(winner).isEqualTo("restaurant1");
    }

    @Test(expected = NotFoundException.class)
    public void whenCreateVoteWithoutExistingRestaurantOrHungryProfessionalItShouldReturnException(){
        VoteDTO voteDTO = new VoteDTO("restaurant", "professional");

        voteService.createVote(voteDTO);

    }

    @Test(expected = MoreThanOneVotePerDayException.class)
    public void whenCreateVoteWithAHungryProfessionalThatAlreadyVotedItShouldReturnException(){
        VoteDTO voteDTO = new VoteDTO("restaurant", "old");
        Optional<HungryProfessional> hungryProfessionalExpected = Optional.ofNullable(HungryProfessional.builder().nickname("old").build());
        Optional<Restaurant> restaurantExpected = Optional.ofNullable(Restaurant.builder().name("restaurant").build());

        Vote voteExpected = Vote.builder().
                hungryProfessional(hungryProfessionalExpected.get())
                .restaurant(restaurantExpected.get())
                .voteDate(LocalDate.now()).build();

        //when(voteRepository.save(any(Vote.class))).thenReturn(voteExpected);
        when(hungryProfessionalRepository.findByNicknameEquals(any(String.class))).thenReturn(hungryProfessionalExpected);
        when(restaurantRepository.findByNameEquals(any(String.class))).thenReturn(restaurantExpected);

        voteService.createVote(voteDTO);

    }
}
