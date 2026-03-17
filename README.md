# OrderFlow
OrderFlow is project to gain hands-on experience working with microservices architecture while creating an e-commerce platform

### ✅ The following features are currently implemented: 
- [x] Kafka configuration (topics, async/sync events)
- [x] Creating order via List<OrderItem>. When created, orderCreatedEvent send to the Notification Microservice
- [x] Custom Kafka exception
- [x] Product creation
- [x] Cart and additon to cart

### 📝 Planned to be implemented:
- [ ] Validating of DTOs
- [ ] Delete CartItems from Cart
- [ ] CRUD for products
- [ ] LogisticsMicroservice with the shiping/delivery logic
- [ ] Email notifications via NotificationMicroservice
- [ ] User logic: Authorization, Authentication, Security (JPA tokens)
- [ ] Metrics and observability: Prometheus, Grafana, additional valuable metrics for existing featuresd
- [ ] SAGA Orchestration
