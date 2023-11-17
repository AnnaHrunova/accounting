<h1> Accounting </h1>

<b>Accounting</b> project includes operations for basic representation* of accounting activities: <br/>
* Client creation
* Account creation
* Transaction handling
* Fetching ot transactions history


<h2>Installation and running</h2>

<h4>Tools</h4>

* JDK 17 <br/>
* Gradle 8.4 <br/>
* Docker Desktop (Server Version: 20.10.24, API Version: 1.41)<br/>
* Intellij Idea 2023.1 (Ultimate Edition)
* MacBook Pro

<h4>Commands</h4>

* Build: `./gradlew clean build`
* Run:
  Local Run: `.run/LocalAccountingApplication.run.xml` run configuration is available. <br/>
  Local Run with mocked currency exchange API: `.run/LocalMockAccountingApplication.run.xml` run configuration is available. <br/>
  Manual local run (without using run configurations) `LocalAccountingApplication` main class is available <br/>
** Local run uses Testcontainers for running PostgreSQLContainer<br/>
** AccountingApplication main class not supposed for local usage<br/><br/>

* Test: `./gradlew clean test`
* Test report location: `build/reports/tests/test/index.html`
* Test coverage location: `build/reports/jacoco/test/html/index.html`