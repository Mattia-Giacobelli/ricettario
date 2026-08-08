package com.example.ricettario.scheduler;

import java.time.LocalDate;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.ricettario.entities.WeeklyPoll;
import com.example.ricettario.repositories.IWeeklyPollRepository;
import com.example.ricettario.utilities.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class WeeklyPollScheduler {

    private static final Logger log = LoggerFactory.getLogger(WeeklyPollScheduler.class);

    private final IWeeklyPollRepository weeklyPollRepo;

    public WeeklyPollScheduler(IWeeklyPollRepository weeklyPollRepository) {

        this.weeklyPollRepo = weeklyPollRepository;

    }

    @Scheduled(cron = "0 0 0 * * MON")
    public void createWeeklyPoll() {

        LocalDate weekStart = LocalDate.now();
        LocalDate weekEnd = weekStart.plusDays(4);

        boolean exists = weeklyPollRepo.existsByWeekStartAndWeekEnd(weekStart, weekEnd);

        if (exists) {

            log.info("Poll per la settimana {} - {} già esistente, salto la creazione.",
                    weekStart, weekEnd);

            return;

        }

        WeeklyPoll poll = new WeeklyPoll();
        poll.setWeekStart(weekStart);
        poll.setWeekEnd(weekEnd);
        poll.setStatus(Status.OPEN);

        weeklyPollRepo.save(poll);

    }

    @Scheduled(cron = "0 59 23 * * FRI")
    public void closeWeeklyPoll() {

        WeeklyPoll poll = weeklyPollRepo.findByStatus(Status.OPEN)
                .orElseThrow(() -> new RuntimeException("Nessun poll aperto da chiudere"));

        poll.setStatus(Status.CLOSED);

        weeklyPollRepo.save(poll);

    }

}
