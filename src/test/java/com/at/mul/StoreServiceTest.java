package com.at.mul;

import com.at.mul.domain.customer.Customer;
import com.at.mul.domain.order.Order;
import com.at.mul.exception.NoRollbackException;
import com.at.mul.exception.StoreException;
import com.at.mul.repository.customer.CustomerRepository;
import com.at.mul.repository.order.OrderRepository;
import com.at.mul.service.StoreService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class StoreServiceTest {

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private StoreService storeService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    @Transactional
    public void testStore() throws Exception {
        Customer c = new Customer();
        c.setName("test");
        c.setAge(30);

        Order o = new Order();
        o.setCode(1);
        o.setQuantity(7);

        storeService.store(c, o);

        Assertions.assertNotNull(c.getId());
        Assertions.assertNotNull(o.getId());

        Assertions.assertEquals(1, customerRepository.findAll().size());
        Assertions.assertEquals(1, orderRepository.findAll().size());
    }

    @Test
    public void testStoreWithStoreException() throws StoreException {
        Customer c = new Customer();
        c.setName("test");
        c.setAge(30);

        Order o = new Order();
        o.setCode(1);
        o.setQuantity(7);

        Assertions.assertEquals(0, customerRepository.findAll().size());
        Assertions.assertEquals(0, orderRepository.findAll().size());

        Assertions.assertThrows(StoreException.class, () -> storeService.storeWithStoreException(c, o));
    }

    @Test
    @Transactional
    public void testStoreWithNoRollbackException() throws NoRollbackException {
        Customer c = new Customer();
        c.setName("test");
        c.setAge(30);

        Order o = new Order();
        o.setCode(1);
        o.setQuantity(7);

        Assertions.assertEquals(0, customerRepository.findAll().size());
        Assertions.assertEquals(0, orderRepository.findAll().size());

        Assertions.assertThrows(NoRollbackException.class, () -> {
            storeService.storeWithNoRollbackException(c, o);
        });
        Assertions.assertEquals(1, customerRepository.findAll().size());
        Assertions.assertEquals(1, orderRepository.findAll().size());
    }

}
