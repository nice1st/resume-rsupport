package com.bahwa.repository;

import java.util.List;

import com.bahwa.entity.Notice;

public interface NoticeRepositoryDsl {
    
    List<Notice> findAllByBetweenPeriodDateTime();
}
