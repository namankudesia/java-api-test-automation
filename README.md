# Java API Test Automation Framework

> Enterprise-grade REST API test framework — **Java 17 · TestNG · Rest-Assured · Maven · Jenkins CI/CD · Extent Reports**

---

## How It Was Built

This framework was designed for real-world enterprise QA workflows:

- **Rest-Assured** for fluent, readable API calls with built-in response validation
- **TestNG** for parallel test execution, data-driven tests, and retry logic
- **Extent Reports** auto-generates beautiful HTML reports after every run
- **Faker** generates randomized test data, eliminating hardcoded values
- **ConfigManager** reads from `config.properties` with environment variable override
- **Retry Listener** automatically reruns flaky tests up to 2 times
- **Parallel execution** — tests run concurrently across 4 threads (configurable in testng.xml)

---

## How to Run

```bash
# 1. Clone
git clone https://github.com/namankudesia/java-api-test-automation.git
cd java-api-test-automation

# 2. Configure base URL and token
vi src/main/resources/config.properties

# 3. Run all tests
mvn clean test

# 4. Run specific suite
mvn test -Dsurefire.suiteXmlFiles=testng.xml

# 5. Run specific test class
mvn test -Dtest=UserApiTest

# 6. View HTML report
open reports/ExtentReport_*.html
```

---

## Project Structure

```
├── pom.xml                          # Maven dependencies
├── testng.xml                       # Suite configuration (parallel threads)
├── src/
│   ├── main/java/com/naman/framework/
│   │   ├── config/ConfigManager.java       # Centralized config + env override
│   │   ├── utils/
│   │   │   ├── RequestBuilder.java         # Fluent request builder
│   │   │   └── ResponseValidator.java      # Fluent response assertions
│   │   ├── models/
│   │   │   ├── UserRequest.java            # Lombok POJO
│   │   │   └── UserResponse.java
│   │   └── listeners/
│   │       ├── ExtentReportListener.java   # HTML report generation
│   │       └── RetryListener.java          # Auto-retry for flaky tests
│   └── test/java/com/naman/tests/
│       ├── UserApiTest.java               # CRUD + negative + perf tests
│       └── AuthApiTest.java               # Login, token, auth tests
└── reports/                               # Generated HTML reports
```

---

## CI/CD (Jenkins)

```groovy
pipeline {
    agent any
    stages {
        stage('Test') {
            steps {
                sh 'mvn clean test'
                publishHTML([reportDir: 'reports', reportFiles: '*.html', reportName: 'API Test Report'])
            }
        }
    }
    post {
        always { junit 'target/surefire-reports/*.xml' }
    }
}
```

> Built by [Naman Kudesia](https://github.com/namankudesia)
