package com.example.ricettario.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ricettario.entities.WeeklyPoll;
import java.util.List;
import java.time.LocalDate;

public interface WeeklyPollRepository extends JpaRepository<WeeklyPoll, Integer> {

    boolean existsByWeekStartAndWeekEnd(LocalDate weekStart, LocalDate weekEnd);

}
