package com.petmily.service;

import com.petmily.domain.builder.AbandonedAnimalBuilder;
import com.petmily.domain.builder.BoardBuilder;
import com.petmily.domain.builder.MemberBuilder;
import com.petmily.domain.builder.ReplyBuilder;
import com.petmily.domain.builder.application.AdoptBuilder;
import com.petmily.domain.core.Board;
import com.petmily.domain.core.Member;
import com.petmily.domain.core.Reply;
import com.petmily.domain.core.application.Adopt;
import com.petmily.domain.core.embeded_type.Email;
import com.petmily.domain.core.embeded_type.PhoneNumber;
import com.petmily.domain.core.enum_type.BoardType;
import com.petmily.domain.dto.member.LoginForm;
import com.petmily.domain.dto.member.MemberJoinForm;
import com.petmily.domain.dto.member.ModifyMemberForm;
import com.petmily.domain.dto.member.WithdrawMemberForm;
import com.petmily.exception.DuplicateLoginIdException;
import com.petmily.exception.PasswordIncorrectException;
import com.petmily.exception.PasswordMismatchException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
@Slf4j
class MemberServiceTest {

    @Autowired
    EntityManager em;

    @Autowired
    MemberService memberService;

    @Autowired
    AbandonedAnimalService animalService;

    @Autowired
    BoardService boardService;

    @Autowired
    ReplyService replyService;

    @Autowired
    ApplicationService applicationService;

    @Test
    @DisplayName("회원 가입 및 id를 통한 조회를 할 수 있다.")
    void join_findMember() {
        //given
        MemberJoinForm form = new MemberJoinForm();
        form.setLoginId("m1");
        form.setPassword("123");
        form.setName("memberA");
        form.setBirth(LocalDate.parse("2000-10-10"));
        form.setEmail(new Email("abc", "naver", "com"));
        form.setPhoneNumber(new PhoneNumber("010", "1234", "1234"));

        //when
        Long joinId = memberService.join(form);
        Member findMember = memberService.findOne(joinId).get();

        //then
        assertThat(findMember.getLoginId()).isEqualTo(form.getLoginId());
        assertThat(findMember.getPassword()).isEqualTo(form.getPassword());
        assertThat(findMember.getName()).isEqualTo(form.getName());
        assertThat(findMember.getBirth()).isEqualTo(form.getBirth());
        assertThat(findMember.getEmail()).isEqualTo(form.getEmail());
        assertThat(findMember.getPhoneNumber()).isEqualTo(form.getPhoneNumber());
    }

    @Test
    @DisplayName("이미 존재하는 로그인 아이디로 회원 가입 시 예외가 발생해 중복 회원 가입을 방지할 수 있다.")
    void duplicateCheck() {
        //given
        MemberJoinForm form1 = new MemberJoinForm();
        MemberJoinForm form2 = new MemberJoinForm();
        form1.setLoginId("memberA");
        form2.setLoginId("memberA");

        //when
        memberService.join(form1);

        //then
        assertThatThrownBy(() -> memberService.join(form2))
                .isInstanceOf(DuplicateLoginIdException.class)
                .hasMessage("중복된 아이디입니다.");
    }

    @Test
    @DisplayName("로그인 시 비밀번호 검증을 할 수 있다.")
    void login() {
        Member member = new MemberBuilder("m", "123").build();
        em.persist(member);

        //given
        LoginForm rightForm = new LoginForm();
        rightForm.setLoginId("m");
        rightForm.setPassword("123");

        LoginForm wrongForm = new LoginForm();
        wrongForm.setLoginId("m");
        wrongForm.setPassword("1234");

        //when
        Optional<Member> rightMember = memberService.login(rightForm);
        Optional<Member> wrongMember = memberService.login(wrongForm);

        //then
        assertThat(rightMember.isPresent()).isTrue();
        assertThat(wrongMember.isPresent()).isFalse();
    }

    @Test
    @DisplayName("모든 회원을 조회할 수 있다.")
    void findAllMember() {
        //given
        Member m1 = new MemberBuilder("m1", "123").build();
        Member m2 = new MemberBuilder("m2", "123").build();
        Member m3 = new MemberBuilder("m3", "123").build();
        em.persist(m1);
        em.persist(m2);
        em.persist(m3);

        //when
        List<Member> allMember = memberService.findAll();

        //then
        assertThat(allMember).hasSize(3);
        assertThat(allMember).contains(m1, m2, m3);
    }

    @Test
    @DisplayName("회원의 로그인 아이디, 비밀번호, 이름, 이메일, 전화번호를 변경할 수 있다.")
    void changeInfo() {
        //given
        Member member = new MemberBuilder("m", "123")
                .setName("member")
                .setEmail(new Email("aaa", "naver", "com"))
                .setPhoneNumber(new PhoneNumber("010", "1234", "1234"))
                .build();

        em.persist(member);

        ModifyMemberForm modifyMemberForm = new ModifyMemberForm();
        modifyMemberForm.setPassword("abc");
        modifyMemberForm.setName("modify member");
        modifyMemberForm.setEmail(new Email("bbb", "google", "com"));
        modifyMemberForm.setPhoneNumber(new PhoneNumber("010", "2222", "3333"));

        //when
        Long modifyId = memberService.modify(member.getId(), modifyMemberForm);
        Member modifyMember = memberService.findOne(modifyId).orElseThrow();

        //then
        assertThat(modifyMember.getPassword()).isEqualTo(modifyMemberForm.getPassword());
        assertThat(modifyMember.getName()).isEqualTo(modifyMemberForm.getName());
        assertThat(modifyMember.getEmail()).isEqualTo(modifyMemberForm.getEmail());
        assertThat(modifyMember.getPhoneNumber()).isEqualTo(modifyMemberForm.getPhoneNumber());
    }

    @Test
    @DisplayName("회원 탈퇴를 할 수 있다.")
    void withdraw() {
        //given
        Member member = new MemberBuilder("m", "123").build();
        em.persist(member);

        WithdrawMemberForm withdrawMemberForm = new WithdrawMemberForm();
        withdrawMemberForm.setPassword("123");
        withdrawMemberForm.setPasswordCheck("123");

        //when
        memberService.withdrawMember(member.getId(), "123", withdrawMemberForm);

        //then
        Optional<Member> findMember = memberService.findOne(member.getId());
        assertThat(findMember.isPresent()).isFalse();
    }

    @Test
    @DisplayName("회원 탈퇴 시 비밀번호와 비밀번호 확인이 다르면 회원 탈퇴를 할 수 없다.")
    void withdraw_mismatch_fail() {
        //given
        Member member = new MemberBuilder("m", "123").build();
        em.persist(member);

        //when
        WithdrawMemberForm withdrawMemberForm = new WithdrawMemberForm();
        withdrawMemberForm.setPassword("123");
        withdrawMemberForm.setPasswordCheck("1234");

        //then
        assertThatThrownBy(() -> memberService.withdrawMember(member.getId(), "123", withdrawMemberForm))
                .isInstanceOf(PasswordMismatchException.class)
                .hasMessage("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
    }

    @Test
    @DisplayName("회원 탈퇴 시 비밀번호가 틀리면 회원 탈퇴를 할 수 없다.")
    void withdraw_incorrect_fail() {
        //given
        Member member = new MemberBuilder("m", "123").build();
        em.persist(member);

        //when
        WithdrawMemberForm withdrawMemberForm = new WithdrawMemberForm();
        withdrawMemberForm.setPassword("1234");
        withdrawMemberForm.setPasswordCheck("1234");

        //then
        assertThatThrownBy(() -> memberService.withdrawMember(member.getId(), "123", withdrawMemberForm))
                .isInstanceOf(PasswordIncorrectException.class)
                .hasMessage("비밀번호가 틀렸습니다.");
    }

    @Test
    @DisplayName("회원 탈퇴 시 작성한 게시글, 댓글, 지원서는 모두 삭제된다.")
    void withdraw2() {
        //given
        Member member = new MemberBuilder("m", "123").build();
        Board board = new BoardBuilder(member, BoardType.FREE).build();
        Reply reply = new ReplyBuilder(member, board).build();
        Adopt adopt = new AdoptBuilder(member, new AbandonedAnimalBuilder().build()).build();

        em.persist(member);

        Optional<Member> findMember = memberService.findOne(member.getId());
        Optional<Board> findBoard = boardService.findOne(board.getId());
        Optional<Reply> findReply = replyService.findOne(reply.getId());
        Optional<Adopt> findApplication = applicationService.findOne(adopt.getId(), Adopt.class);

        assertThat(findMember.isPresent()).isTrue();
        assertThat(findBoard.isPresent()).isTrue();
        assertThat(findReply.isPresent()).isTrue();
        assertThat(findApplication.isPresent()).isTrue();

        //when
        WithdrawMemberForm withdrawMemberForm = new WithdrawMemberForm();
        withdrawMemberForm.setPassword("123");
        withdrawMemberForm.setPasswordCheck("123");

        memberService.withdrawMember(member.getId(), "123", withdrawMemberForm);

        //then
        findMember = memberService.findOne(member.getId());
        findBoard = boardService.findOne(board.getId());
        findReply = replyService.findOne(reply.getId());
        findApplication = applicationService.findOne(adopt.getId(), Adopt.class);

        assertThat(findMember.isPresent()).isFalse();
        assertThat(findBoard.isPresent()).isFalse();
        assertThat(findReply.isPresent()).isFalse();
        assertThat(findApplication.isPresent()).isFalse();
    }
}