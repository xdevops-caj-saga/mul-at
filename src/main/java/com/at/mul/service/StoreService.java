package com.at.mul.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.at.mul.domain.customer.Customer;
import com.at.mul.domain.order.Order;
import com.at.mul.exception.NoRollbackException;
import com.at.mul.exception.StoreException;
import com.at.mul.repository.customer.CustomerRepository;
import com.at.mul.repository.order.OrderRepository;

@Service
public class StoreService {
	
	private final CustomerRepository customerRepository;
	private final OrderRepository orderRepository;

	public StoreService(CustomerRepository customerRepository, OrderRepository orderRepository) {
		this.customerRepository = customerRepository;
		this.orderRepository = orderRepository;
	}

	@Transactional
	public void store(Customer customer, Order order) {
		customerRepository.save(customer);
		orderRepository.save(order);
	}

	@Transactional(rollbackFor = StoreException.class)
	public void storeWithStoreException(Customer customer, Order order) throws StoreException {
		customerRepository.save(customer);
		orderRepository.save(order);
		throw new StoreException();
	}

	@Transactional(noRollbackFor = NoRollbackException.class, rollbackFor = StoreException.class)
	public void storeWithNoRollbackException(Customer customer, Order order) throws NoRollbackException {
		customerRepository.save(customer);
		orderRepository.save(order);
		throw new NoRollbackException();
	}

}
