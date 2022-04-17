package com.bahwa.repository;

import java.time.LocalDateTime;
import java.util.List;

import com.bahwa.entity.Notice;
import com.bahwa.entity.QNotice;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.DateExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class NoticeRepositoryDslImpl implements NoticeRepositoryDsl {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Notice> findAllByBetweenPeriodDateTime() {
        
        QNotice notice = QNotice.notice;

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        // 공지 시작일시가 현재보다 작음
        booleanBuilder.and(DateExpression.currentDate(LocalDateTime.class).goe(notice.periodStart));
        // 공지 종료일시가 없거나 현재보다 큼
        booleanBuilder.and(notice.periodEnd.isNull().or(DateExpression.currentDate(LocalDateTime.class).loe(notice.periodEnd)));

        return jpaQueryFactory.selectFrom(notice)
            .where(booleanBuilder)
            .fetch();
    }
}
