package com.dbserver.developertest.repository;

import com.dbserver.developertest.model.HungryProfessional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HungryProfessionalRepository extends JpaRepository<HungryProfessional, Long> {

    Optional<HungryProfessional> findByNicknameEquals(String nickname);
}
