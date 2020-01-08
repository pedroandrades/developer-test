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
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class VoteService {

    private VoteRepository voteRepository;
    private HungryProfessionalRepository hungryProfessionalRepository;
    private RestaurantRepository restaurantRepository;
    private RestaurantService restaurantService;

    public VoteService(VoteRepository voteRepository, HungryProfessionalRepository hungryProfessionalRepository, RestaurantRepository restaurantRepository, RestaurantService restaurantService) {
        this.voteRepository = voteRepository;
        this.hungryProfessionalRepository = hungryProfessionalRepository;
        this.restaurantRepository = restaurantRepository;
        this.restaurantService = restaurantService;
    }

    public Vote createVote(VoteDTO voteDTO){
        Optional<Restaurant> restaurantOptional = restaurantRepository.findByNameEquals(voteDTO.getRestaurantName());
        Optional<HungryProfessional> hungryProfessionalOptional = hungryProfessionalRepository.findByNicknameEquals(voteDTO.getHungryProfessionalNickname());

        if(restaurantOptional.isPresent() && hungryProfessionalOptional.isPresent()){
            HungryProfessional hungryProfessional = hungryProfessionalOptional.get();
            if(checkHungryProfessionalVote(hungryProfessional)){
                throw new MoreThanOneVotePerDayException("You already voted today.");
            }
            Restaurant restaurant = restaurantOptional.get();
            return voteRepository.save(Vote.builder().hungryProfessional(hungryProfessional).
                            restaurant(restaurant).voteDate(LocalDate.now()).build());
        }

        throw new NotFoundException("Restaurant or Hungry Professional not found.");
    }

    private boolean checkHungryProfessionalVote(HungryProfessional hungryProfessional){
        List<Vote> voteList = voteRepository.findAll().stream()
                .filter(p -> p.getVoteDate().getDayOfMonth() == LocalDate.now().getDayOfMonth())
                .collect(Collectors.toList());
        return voteList.stream().anyMatch(vote -> hungryProfessional.equals(vote.getHungryProfessional()));
    }

    public String winner(){
        List<Vote> voteList = voteRepository.findAll().stream()
                .filter(p -> p.getVoteDate().getDayOfMonth() == LocalDate.now().getDayOfMonth())
                .collect(Collectors.toList());

        List<Vote> voteListX = new ArrayList<>();

        //check if a week has passed from all old winners
        for (Vote vote: voteList) {
            if(vote.getRestaurant().getChosenDate() == null
                    || vote.getRestaurant().getChosenDate().getDayOfYear() - LocalDate.now().getDayOfYear() >= 7){
                voteListX.add(vote);
            }
        }

        Map<String, Long> counters = voteListX.stream()
                .collect(Collectors.groupingBy(p -> p.getRestaurant().getName(),
                        Collectors.counting()));
        Map.Entry<String, Long> maxEntry = null;

        for (Map.Entry<String, Long> entry : counters.entrySet())
        {
            if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0)
            {
                maxEntry = entry;
            }
        }

        String winner = maxEntry.getKey();
        restaurantService.winnerRestaurant(winner);
        return winner;
    }
}
