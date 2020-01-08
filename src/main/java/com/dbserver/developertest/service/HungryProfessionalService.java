package com.dbserver.developertest.service;

import com.dbserver.developertest.dto.HungryProfessionalDTO;
import com.dbserver.developertest.exception.ExistingHungryProfessionalException;
import com.dbserver.developertest.model.HungryProfessional;
import com.dbserver.developertest.repository.HungryProfessionalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HungryProfessionalService {

    private HungryProfessionalRepository hungryProfessionalRepository;

    @Autowired
    public HungryProfessionalService(HungryProfessionalRepository hungryProfessionalRepository) {
        this.hungryProfessionalRepository = hungryProfessionalRepository;
    }

    public HungryProfessional createHungryProfessional(HungryProfessionalDTO hungryProfessionalDTO){
        if(hungryProfessionalRepository.findAll().stream().map(HungryProfessional::getNickname)
                .noneMatch(hungryProfessionalDTO.getNickname()::equals)){

            return hungryProfessionalRepository.save(HungryProfessional.builder()
                    .name(hungryProfessionalDTO.getName())
                    .nickname(hungryProfessionalDTO.getNickname())
                    .password(hungryProfessionalDTO.getPassword())
                    .build());

        }

        throw new ExistingHungryProfessionalException("Hungry Professional already exists");
    }
}
