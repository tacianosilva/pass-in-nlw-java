package com.rocketseat.passin.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rocketseat.passin.domain.checkin.CheckIn;

public interface CheckinRepository extends JpaRepository<CheckIn, Integer> {

}
