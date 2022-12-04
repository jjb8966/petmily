package com.petmily.domain.builder;

import com.petmily.domain.builder.board.FindBoardBuilder;
import com.petmily.domain.builder.board.WatchBoardBuilder;
import com.petmily.domain.core.Member;
import com.petmily.domain.core.board.Board;
import com.petmily.domain.core.board.FindBoard;
import com.petmily.domain.core.board.WatchBoard;
import com.petmily.domain.enum_type.AnimalSpecies;
import com.petmily.domain.enum_type.BoardType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class BoardBuilderTest {

    @Autowired
    EntityManager em;

    @Test
    @DisplayName("찾아요/봤어요 게시물을 만들 수 있다.")
    void findBoard() {
        //given
        Member member = new MemberBuilder("member", "123").build();

        //when
        FindBoard createFindBoard = new FindBoardBuilder(member, BoardType.FIND)
                .setTitle("title")
                .setContent("content")
                .setLostTime(LocalDateTime.now())
                .setSpecies(AnimalSpecies.CAT)
                .setAnimalName("야옹이")
                .setAnimalKind("페르시안")
                .setAnimalAge(10)
                .setAnimalWeight(5.0F)
                .build();

        WatchBoard createWatchBoard = new WatchBoardBuilder(member, BoardType.WATCH)
                .setTitle("title")
                .setContent("content")
                .setWatchTime(LocalDateTime.now())
                .setSpecies(AnimalSpecies.DOG)
                .build();

        em.persist(member);

        em.flush();
        em.clear();

        Board findFindBoard = em.find(Board.class, createFindBoard.getId());
        Board findWatchBoard = em.find(Board.class, createWatchBoard.getId());
        FindBoard findBoard = (FindBoard) findFindBoard;
        WatchBoard watchBoard = (WatchBoard) findWatchBoard;

        //then
        assertThat(findFindBoard).isInstanceOf(FindBoard.class);
        assertThat(findWatchBoard).isInstanceOf(WatchBoard.class);

        assertThat(findBoard.getLostTime()).isEqualTo(createFindBoard.getLostTime());
        assertThat(findBoard.getSpecies()).isEqualTo(createFindBoard.getSpecies());

        assertThat(watchBoard.getWatchTime()).isEqualTo(createWatchBoard.getWatchTime());
        assertThat(watchBoard.getSpecies()).isEqualTo(createWatchBoard.getSpecies());
    }

}