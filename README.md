# OrderFlow
OrderFlow is project to gain hands-on experience working with microservices architecture while creating an e-commerce platform

### ✅ The following features are currently implemented: 
- [x] Kafka configuration (topics, async/sync events)
- [x] Cart and additon to cart
- [x] SAGA Orchestration + Compensating transactions

### 📝 Planned to be implemented:
- [ ] Validating of DTOs
- [ ] More custom exceptions
- [ ] Delete CartItems from Cart
- [ ] CRUD for products
- [ ] LogisticsMicroservice with the shiping/delivery logic
- [ ] Email notifications via NotificationMicroservice
- [ ] User logic: Authorization, Authentication, Security (JPA tokens)
- [ ] Metrics and observability: Prometheus, Grafana, additional valuable metrics for existing featuresd
- [ ] CI/CD: Github Actions
- [ ] Unit testing

## Current order processing logic
### Successful order processing
![Sucessfull order processing](https://github.com/karmazinoleh/OrderFlow/blob/master/Screenshot%202025-07-28%20at%2017.28.04.png)
### Unsuccessful order processing – payment exception
![Unsuccessful order processing](https://github.com/karmazinoleh/OrderFlow/blob/master/Screenshot%202025-07-28%20at%2017.34.45.png)
