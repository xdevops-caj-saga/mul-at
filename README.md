# Spring Boot, Spring Data JPA and Atomikos 

This fork updated the very helpful example of Fabio Maffioletti to the most recent Spring Boot version:

Please read his article for a detailed description: https://www.fabiomaffioletti.me/blog/2014/04/15/distributed-transactions-multiple-databases-spring-boot-spring-data-jpa-atomikos/

## Test

See `StoreServiceTest` test class:
- Run `testStore` for success case
- Run `testStoreWithStoreException` for failed case

## Explain

Components:

- JTA Platform: `AtomikosJtaPlatform`
- Transaction Manager definition: `MainConfig`
- Data sources
  - `xads1`: `customer.datasource`
  - `xads2`: `order.datasource`
- JPA Repository configuration:
  - `CustomerConfig`
  - `OrderConfig`
  
References:  
- [Distributed Transactions with JTA](https://docs.spring.io/spring-boot/docs/2.0.x/reference/html/boot-features-jta.html)