package com.petmily.repository;

import com.petmily.domain.core.board.Board;
import com.petmily.domain.core.board.FindWatchBoard;
import com.petmily.domain.dto.board.find_watch.SearchCondition;
import com.petmily.domain.enum_type.AnimalSpecies;
import com.petmily.domain.enum_type.BoardType;
import com.petmily.domain.enum_type.FindWatchBoardStatus;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.petmily.domain.core.board.QFindWatchBoard.findWatchBoard;

@Slf4j
public class BoardRepositoryCustomImpl implements BoardRepositoryCustom {

    private final JPAQueryFactory query;

    public BoardRepositoryCustomImpl(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }

    @Override
    public Page<Board> findAllByBoardType(BoardType boardType, SearchCondition searchCondition, Pageable pageable) {
        JPAQuery<FindWatchBoard> selectQuery = query
                .selectFrom(findWatchBoard)
                .where(findWatchBoard.boardType.eq(boardType),
                        speciesEq(searchCondition.getSpecies()),
                        boardStatusEq(searchCondition.getBoardStatus()),
                        keywordContain(searchCondition.getKeyword()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(getOrderSpecifiers(pageable));

        List<Board> content = selectQuery.fetch().stream()
                .map(findWatchBoard -> (Board) findWatchBoard)
                .collect(Collectors.toList());

        return PageableExecutionUtils.getPage(content, pageable, selectQuery::fetchCount);
    }

    private BooleanExpression speciesEq(AnimalSpecies species) {
        return species == null ? null : findWatchBoard.species.eq(species);
    }

    private BooleanExpression boardStatusEq(FindWatchBoardStatus boardStatus) {
        return boardStatus == null ? null : findWatchBoard.boardStatus.eq(boardStatus);
    }

    private BooleanExpression keywordContain(String keyword) {
        if (keyword == null) {
            return null;
        }

        return titleContain(keyword)
                .or(contentContain(keyword))
                .or(animalNameContain(keyword))
                .or(animalKindContain(keyword))
                .or(animalSpeciesContain(keyword));
    }

    private static BooleanExpression animalSpeciesContain(String keyword) {
        return findWatchBoard.species.stringValue().containsIgnoreCase(keyword);
    }

    private static BooleanExpression animalKindContain(String keyword) {
        return findWatchBoard.animalKind.containsIgnoreCase(keyword);
    }

    private static BooleanExpression contentContain(String keyword) {
        return findWatchBoard.content.containsIgnoreCase(keyword);
    }

    private static BooleanExpression titleContain(String keyword) {
        return findWatchBoard.title.containsIgnoreCase(keyword);
    }

    private static BooleanExpression animalNameContain(String keyword) {
        return findWatchBoard.animalName.containsIgnoreCase(keyword);
    }

    private OrderSpecifier[] getOrderSpecifiers(Pageable pageable) {
        return getOrders(pageable)
                .toArray(value -> new OrderSpecifier[value]);
    }

    private List<OrderSpecifier> getOrders(Pageable pageable) {
        List<OrderSpecifier> result = new ArrayList<>();
        Sort sort = pageable.getSort();

        for (Sort.Order order : sort) {
            Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
            String property = order.getProperty();

            Path<Object> path = Expressions.path(Object.class, findWatchBoard, property);
            OrderSpecifier<?> orderSpecifier = new OrderSpecifier(direction, path);

            result.add(orderSpecifier);
        }

        return result;
    }

    @Override
    public List<Long> matchWithFindWatchBoard(FindWatchBoard board) {
        List<Long> result = query.select(findWatchBoard.id)
                .from(findWatchBoard)
                .where(
                        findWatchBoard.member.ne(board.getMember()),
                        againstBoardType(board.getBoardType()),
                        matchInformation(board)
                )
                .fetch();

        return result;
    }

    private BooleanExpression againstBoardType(BoardType boardType) {
        if (boardType.equals(BoardType.FIND)) {
            return findWatchBoard.boardType.eq(BoardType.WATCH);
        } else {
            return findWatchBoard.boardType.eq(BoardType.FIND);
        }
    }

    private BooleanBuilder matchInformation(FindWatchBoard board) {
        BooleanBuilder builder = new BooleanBuilder();
        boolean hasCondition = false;

        if (board.getSpecies() != null) {
            builder.or(findWatchBoard.species.eq(board.getSpecies()));
            hasCondition = true;
        }

        if (board.getAnimalName() != null) {
            builder.or(findWatchBoard.animalName.eq(board.getAnimalName()));
            hasCondition = true;
        }

        if (board.getAnimalKind() != null) {
            builder.or(findWatchBoard.animalKind.eq(board.getAnimalKind()));
            hasCondition = true;
        }

        if (board.getAnimalAge() != null) {
            builder.or(findWatchBoard.animalAge.eq(board.getAnimalAge()));
            hasCondition = true;
        }

        if (board.getAnimalWeight() != null) {
            builder.or(findWatchBoard.animalWeight.eq(board.getAnimalWeight()));
            hasCondition = true;
        }

        return hasCondition ? builder : builder.and(findWatchBoard.id.eq(-1L));
    }

    @Override
    public Long updateBoardStatusMatch(ArrayList<Long> needUpdateIds) {
        long countUpdatedBoard = query.update(findWatchBoard)
                .set(findWatchBoard.boardStatus, FindWatchBoardStatus.MATCH)
                .where(findWatchBoard.id.in(needUpdateIds))
                .execute();

        return countUpdatedBoard;
    }
}
