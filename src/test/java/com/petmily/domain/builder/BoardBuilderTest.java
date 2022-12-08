package com.petmily.domain.builder;

import com.petmily.domain.builder.board.FindWatchBoardBuilder;
import com.petmily.domain.core.Member;
import com.petmily.domain.core.board.Board;
import com.petmily.domain.core.board.FindWatchBoard;
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
        FindWatchBoard createFindWatchBoard = new FindWatchBoardBuilder(member, BoardType.FIND)
                .setTitle("title")
                .setContent("content")
                .setLostOrWatchTime(LocalDateTime.now())
                .setSpecies(AnimalSpecies.CAT)
                .setAnimalName("야옹이")
                .setAnimalKind("페르시안")
                .setAnimalAge(10)
                .setAnimalWeight(5.0F)
                .build();

        FindWatchBoard createWatchBoard = new FindWatchBoardBuilder(member, BoardType.WATCH)
                .setTitle("title")
                .setContent("content")
                .setLostOrWatchTime(LocalDateTime.now())
                .setSpecies(AnimalSpecies.DOG)
                .build();

        em.persist(member);

        em.flush();
        em.clear();

        Board findFindBoard = em.find(Board.class, createFindWatchBoard.getId());
        Board findWatchBoard = em.find(Board.class, createWatchBoard.getId());
        FindWatchBoard findBoard = (FindWatchBoard) findFindBoard;
        FindWatchBoard watchBoard = (FindWatchBoard) findWatchBoard;

        //then
        assertThat(findFindBoard).isInstanceOf(FindWatchBoard.class);
        assertThat(findWatchBoard).isInstanceOf(FindWatchBoard.class);

        assertThat(findBoard.getLostOrWatchTime()).isEqualTo(createFindWatchBoard.getLostOrWatchTime());
        assertThat(findBoard.getSpecies()).isEqualTo(createFindWatchBoard.getSpecies());

        assertThat(watchBoard.getLostOrWatchTime()).isEqualTo(createWatchBoard.getLostOrWatchTime());
        assertThat(watchBoard.getSpecies()).isEqualTo(createWatchBoard.getSpecies());
    }

}