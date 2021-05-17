package com.jongsul.fabinetgradle.Repository;

import com.jongsul.fabinetgradle.Domain.Board;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class BoardRepositoryImpl implements BoardRepository{

    private final EntityManager em;

    @Override
    public void save(Board board) {
        em.persist(board);
        log.info("psersist 완료");
    }
    @Override
    public List<Board> findAll(){
        log.info("게시물 전체 불러오기");
        return em.createQuery("select b from Board b",Board.class).getResultList();
    }

    @Override
    public Board findOne(long id) {
        log.info(id+"번 게시물 불러오기");
        return em.find(Board.class, id);
    }
}
