package com.petmily.repository;

import com.petmily.domain.builder.MemberBuilder;
import com.petmily.domain.builder.board.FindWatchBoardBuilder;
import com.petmily.domain.core.Member;
import com.petmily.domain.core.board.FindWatchBoard;
import com.petmily.domain.enum_type.AnimalSpecies;
import com.petmily.domain.enum_type.BoardType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class BoardRepositoryCustomImplTest {

    @Autowired
    EntityManager em;

    @Autowired
    BoardRepository boardRepository;

    Member member;
    Member otherMember;

    @BeforeEach
    void beforeEach() {
        member = new MemberBuilder("member", "123").build();
        otherMember = new MemberBuilder("otherMember", "123").build();
        em.persist(member);
        em.persist(otherMember);
    }

    @Test
    @DisplayName("찾아요 게시판의 종, 이름, 종류, 나이, 몸무게 중 일치하는 정보를 가진 봤어요 게시글의 id를 조회할 수 있다.")
    void matchWithFindWatchBoard() {
        //given
        FindWatchBoard findBoard = new FindWatchBoardBuilder(member, BoardType.FIND)
                .setSpecies(AnimalSpecies.CAT)
                .setAnimalName("cat")
                .setAnimalKind("kind")
                .setAnimalAge(3)
                .setAnimalWeight(2F)
                .build();

        em.persist(findBoard);

        //when
        FindWatchBoard matchBoard1 = new FindWatchBoardBuilder(otherMember, BoardType.WATCH)
                .setSpecies(findBoard.getSpecies())
                .build();

        FindWatchBoard matchBoard2 = new FindWatchBoardBuilder(otherMember, BoardType.WATCH)
                .setAnimalName(findBoard.getAnimalName())
                .build();

        FindWatchBoard matchBoard3 = new FindWatchBoardBuilder(otherMember, BoardType.WATCH)
                .setAnimalKind(findBoard.getAnimalKind())
                .build();

        FindWatchBoard mismatchBoard1 = new FindWatchBoardBuilder(otherMember, BoardType.WATCH)
                .setAnimalAge(4)
                .build();

        FindWatchBoard mismatchBoard2 = new FindWatchBoardBuilder(otherMember, BoardType.WATCH)
                .setAnimalWeight(3F)
                .build();

        FindWatchBoard mismatchBoard3 = new FindWatchBoardBuilder(member, BoardType.WATCH)
                .setAnimalWeight(findBoard.getAnimalWeight())
                .build();

        FindWatchBoard mismatchBoard4 = new FindWatchBoardBuilder(member, BoardType.WATCH)
                .build();

        List<Long> matchIds = boardRepository.matchWithFindWatchBoard(findBoard);

        //then
        assertThat(matchIds).hasSize(3);
        assertThat(matchIds).containsExactly(matchBoard1.getId(), matchBoard2.getId(), matchBoard3.getId());
    }

}