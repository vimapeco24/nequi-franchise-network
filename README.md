# franchiseEntity-network-management

The Franchise Network Management project is a Spring Boot application that provides a RESTful API for managing franchises, branches, and products. The application allows users to create franchises, add branches to franchises, manage products in branches, and track product stock levels.

### Operations

- Create a franchise
- Add a branch to a franchise
- Add and remove products from a branch
- Modify the stock of a product in a branch
- Get the top product of a branch

### Diagram de Operaciones

![franchises-network.drawio.png](franchises-network.drawio.png)

### How to run the project

1. Clone the repository
2. Navigate to the project directory
3. Select the desired profile:
   - `local` for local development
   - `dev` for development (no shared by author, use secrets or keyvault)
   - `test` for testing (no shared by author, use secrets or keyvault)
   - `prod` for production (no shared by author, use secrets or keyvault)
4. Run the following command to start the application:
   ```
   ./gradlew bootRun --args='--spring.profiles.active=dev'
   ```
5. The application will start on port 8080 by default
6. You can access the API documentation at `http://localhost:8080/webjars/swagger-ui/index.html`


### API Documentation

The API documentation is available at the following file path: ***swagger.json***

### Postman Collection

The Postman collection is available at the following file path: ***franchise.postman_collection.json***

### Test Cases

The test cases are located in the `src/test/java/com/network/franchise/...` directory. The test cases are written using JUnit 5 and can be run using the following command:
```
./gradlew test
```

### Code Coverage

The code coverage report is generated using JaCoCo and can be found in the `build/reports/tests/test/index.html` directory. You can open the `index.html` file in your web browser to view the code coverage report.
```
./gradlew clean test jacocoTestReport
```
![jococo-report.png](jococo-report.png)

### Author

- **[Raul Bolivar Navas](https://www.linkedin.com/in/rasysbox)** - [GitHub](https://github.com/raulrobinson/franchise-network-management)

### License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.
