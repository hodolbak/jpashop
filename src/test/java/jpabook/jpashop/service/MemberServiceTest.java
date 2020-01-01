package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
//    @Autowired
//    EntityManager em;

    // Test 케이스에 @Transactional 이 있으면 기본적으로 rollback 을 한다. 쿼리를 보고 싶은면 rollback false 로..
    // 쿼리를 보고 rollback 을 하고 싶으면
    // EntityManager 를 이용 flush 사용.
    @Test
    @Rollback(false)
    public void join() throws Exception {
        Member member = new Member();
        member.setName("kim");

        Long saveId = memberService.join(member);

//        em.flush();
        assertEquals(member, memberRepository.findOne(saveId));
    }

    @Test(expected = IllegalStateException.class)
    public void dupException() throws Exception {
        Member member1 = new Member();
        member1.setName("kim");

        Member member2 = new Member();
        member2.setName("kim");

        memberService.join(member1);
        memberService.join(member2);

        fail("예외가 발생해야 한다.");
    }
}