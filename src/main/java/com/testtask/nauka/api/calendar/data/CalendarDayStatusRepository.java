package com.testtask.nauka.api.calendar.data;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CalendarDayStatusRepository extends JpaRepository<CalendarDayStatus, Long> {
    @Modifying
    @Query("update CalendarDayStatus u set u.isDefault = ?1 where u.isDefault <> ?1")
    void setIsDefaultForAll(boolean value);
}
