package com.example.ricettario.repositories;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.ricettario.entities.WeeklyPoll;
import com.example.ricettario.utilities.Status;

public interface IWeeklyPollRepository extends JpaRepository<WeeklyPoll, Integer> {

    boolean existsByWeekStartAndWeekEnd(LocalDate weekStart, LocalDate weekEnd);

    Optional<WeeklyPoll> findByWeekStartAndWeekEnd(LocalDate weekStart, LocalDate weekEnd);

    @Query("SELECT p FROM WeeklyPoll p WHERE :today BETWEEN p.weekStart AND p.weekEnd")
    Optional<WeeklyPoll> findPollContainingDate(@Param("today") LocalDate today);

    Optional<WeeklyPoll> findTopByStatusOrderByWeekEndDesc(Status status);

    Optional<WeeklyPoll> findByStatus(Status status);

}
