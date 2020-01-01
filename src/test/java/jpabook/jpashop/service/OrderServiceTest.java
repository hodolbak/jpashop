package jpabook.jpashop.service;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OrderServiceTest {

    @Autowired
    EntityManager em;
    @Autowired OrderService orderService;
    @Autowired
    OrderRepository orderRepository;

    @Test
    public void order() throws Exception {
        Member member = createMember();

        Book book = createBook("adddsaf", 10000, 10);

        int orderCount = 3;

        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        Order getOrder = orderRepository.findOne(orderId);

        Assert.assertEquals("상품 주문시 상태는 ORDER", OrderStatus.ORDER, getOrder.getStatus());;
        Assert.assertEquals("주문한 상품 수 일치", 1, getOrder.getOrderItems().size());
        Assert.assertEquals("주문 가격 일치", book.getPrice()*orderCount, getOrder.getTotalPrice());
        Assert.assertEquals("재고 일치", 10-orderCount, book.getStockQuantity());
    }

    private Book createBook(String name, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("kimjooho");
        member.setAddress(new Address("서울", "진관3로", "03300"));
        em.persist(member);
        return member;
    }

    @Test(expected = NotEnoughStockException.class)
    public void order_exception() throws Exception{
        Member member = createMember();
        Item item = createBook("book11", 1000, 10);

        int orderCount = 11;

        orderService.order(member.getId(), item.getId(), orderCount);

        fail("주문 재고 오류 발생해야 한다.");
    }

    @Test
    public void orderCancel() throws Exception{
        Member member = createMember();
        Book item = createBook("adf", 1000, 10);

        int orderCount = 3;

        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

        orderService.cancelOrder(orderId);

        Order getOrder = orderRepository.findOne(orderId);

        Assert.assertEquals("주문취소 상태 캔슬.", OrderStatus.CANCEL, getOrder.getStatus());
        Assert.assertEquals("취소 후 재공 증가", 10, item.getStockQuantity());

    }

}
