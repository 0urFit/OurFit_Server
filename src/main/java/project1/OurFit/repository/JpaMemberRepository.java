package project1.OurFit.repository;

import jakarta.persistence.EntityManager;
import project1.OurFit.Entity.Member;

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
    public Optional<Member> findByNickname(String nickname) {
        return em.createQuery("select m from Member m where m.nickname = :nickname", Member.class)
                .setParameter("nickname", nickname)
                .getResultStream().findAny();
    }

    @Override
    public Optional<Member> save(Member member) {
        em.persist(member);
        em.flush();
        if (member.getId() != null)
            return Optional.of(member);
        else
            return Optional.empty();
    }
}
