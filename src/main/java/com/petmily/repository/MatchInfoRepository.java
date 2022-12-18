package com.petmily.repository;

import com.petmily.domain.core.board.MatchInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchInfoRepository extends JpaRepository<MatchInfo, Long> {
}
