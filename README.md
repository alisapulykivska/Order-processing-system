# OrderFlow
OrderFlow is project to gain hands-on experience working with microservices architecture while creating an e-commerce platform

### ✅ The following features are currently implemented: 
- [x] Kafka configuration (topics, async/sync events)
- [x] Cart with products
- [x] SAGA Orchestration + Compensating transactions
- [x] Validation of DTOs on endpoints
- [x] Custom exceptions for every microservice
- [x] Authorization, Authentication, Security with Keycloak
- [x] API Gateway with Spring Cloud
- [x] CRUD for Product, Order, User entities

### 📝 Planned to be implemented:
- [ ] Add Elasticsearch for ProductMicroservice
- [ ] Add caching using Redis
- [ ] Metrics and observability: Prometheus and Grafana
- [ ] GraphQL Gateway
- [ ] 🟡 front-end page partly implemented
- [ ] InventoryMicroservice with sync (HTTP) requests and Spring Cloud
- [ ] LogisticsMicroservice with the shiping/delivery logic
- [ ] Email notifications via NotificationMicroservice
- [ ] CI/CD: Github Actions

## Current order processing logic
### Successful order processing
![Sucessfull order processing](https://github.com/karmazinoleh/OrderFlow/blob/master/Screenshot%202025-07-28%20at%2017.28.04.png)
### Unsuccessful order processing – payment exception
![Unsuccessful order processing](https://github.com/karmazinoleh/OrderFlow/blob/master/Screenshot%202025-07-28%20at%2017.34.45.png)
