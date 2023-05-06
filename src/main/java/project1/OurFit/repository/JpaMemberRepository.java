package project1.OurFit.repository;

import jakarta.persistence.EntityManager;
import project1.OurFit.entity.Member;

import java.util.Optional;

public class JpaMemberRepository implements MemberRepository {

    private final EntityManager em;

    public JpaMemberRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        return em.createQuery("select m from Member m where m.email = :email", Member.class)
                .setParameter("email", email)
                .getResultStream().findAny();
    }
    @Override
    public Optional<Member> findById(Long id){
        return em.createQuery("select m from Member m where m.id=:id", Member.class)
                .setParameter("id",id)
                .getResultStream().findAny();
    }

    @Override
    public Optional<Member> save(Member member) {
        em.persist(member);
        em.flush();
        if (member.getId() != null)
            return Optional.of(member);
        return Optional.empty();
    }

    @Override
    public boolean existsByEmail(String email) {
        Long count = em.createQuery("select count(m) from Member m where m.email = :email", Long.class)
                .setParameter("email",email)
                .getSingleResult();
        return count>0;
    }

    @Override
    public boolean existsByNickname(String nickname) {
        Long count = em.createQuery("select count(m) from Member m where m.nickname = :nickname", Long.class)
                .setParameter("nickname",nickname)
                .getSingleResult();
        return count>0;

    }
}
