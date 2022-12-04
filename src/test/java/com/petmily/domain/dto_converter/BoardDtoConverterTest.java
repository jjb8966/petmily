package com.petmily.domain.dto_converter;

import com.petmily.domain.builder.MemberBuilder;
import com.petmily.domain.builder.board.FindBoardBuilder;
import com.petmily.domain.core.Member;
import com.petmily.domain.core.board.FindBoard;
import com.petmily.domain.dto.board.find_watch.FindWatchBoardDetailForm;
import com.petmily.domain.dto.board.find_watch.FindWatchBoardListForm;
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
class BoardDtoConverterTest {

    @Autowired
    EntityManager em;

    @Autowired
    BoardDtoConverter boardDtoConverter;

    @Test
    @DisplayName("찾아요 게시글 도메인을 리스트 폼으로 변환할 수 있다.")
    void findBoard_to_listForm() {
        Member member = new MemberBuilder("member", "123")
                .setName("memberA")
                .build();

        FindBoard findBoard = new FindBoardBuilder(member, BoardType.FIND)
                .setTitle("title")
                .setLostTime(LocalDateTime.now())
                .setSpecies(AnimalSpecies.CAT)
                .build();

        em.persist(member);

        FindWatchBoardListForm findWatchBoardListForm = boardDtoConverter.entityToDto(findBoard, FindWatchBoardListForm.class).orElseThrow();

        assertThat(findWatchBoardListForm.getId()).isEqualTo(findBoard.getId());
        assertThat(findWatchBoardListForm.getMemberId()).isEqualTo(member.getId());

        assertThat(findWatchBoardListForm.getWriterName()).isEqualTo(member.getName());
        assertThat(findWatchBoardListForm.getTitle()).isEqualTo(findBoard.getTitle());
        assertThat(findWatchBoardListForm.getLostOrWatchTime()).isEqualTo(findBoard.getLostTime());
        assertThat(findWatchBoardListForm.getSpecies()).isEqualTo(findBoard.getSpecies());
    }

    @Test
    @DisplayName("찾아요 게시글 도메인을 상세보기 폼으로 변환할 수 있다.")
    void findBoard_to_detailForm() {
        Member member = new MemberBuilder("member", "123")
                .setName("memberA")
                .build();

        FindBoard findBoard = new FindBoardBuilder(member, BoardType.FIND)
                .setTitle("title")
                .setLostTime(LocalDateTime.now())
                .setSpecies(AnimalSpecies.CAT)
                .build();

        em.persist(member);

        FindWatchBoardDetailForm findWatchBoardDetailForm = boardDtoConverter.entityToDto(findBoard, FindWatchBoardDetailForm.class).orElseThrow();

        assertThat(findWatchBoardDetailForm.getId()).isEqualTo(findBoard.getId());
        assertThat(findWatchBoardDetailForm.getMember().getId()).isEqualTo(member.getId());

        assertThat(findWatchBoardDetailForm.getMember().getName()).isEqualTo(member.getName());
        assertThat(findWatchBoardDetailForm.getTitle()).isEqualTo(findBoard.getTitle());
        assertThat(findWatchBoardDetailForm.getLostOrWatchTime()).isEqualTo(findBoard.getLostTime());
        assertThat(findWatchBoardDetailForm.getSpecies()).isEqualTo(findBoard.getSpecies());
    }

}