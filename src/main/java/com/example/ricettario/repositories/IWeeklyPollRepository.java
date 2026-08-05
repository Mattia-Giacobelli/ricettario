package com.example.ricettario.repositories;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ricettario.entities.WeeklyPoll;

public interface IWeeklyPollRepository extends JpaRepository<WeeklyPoll, Integer> {

    boolean existsByWeekStartAndWeekEnd(LocalDate weekStart, LocalDate weekEnd);

    Optional<WeeklyPoll> findByWeekStartAndWeekEnd(LocalDate weekStart, LocalDate weekEnd);

}
