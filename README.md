# SauceDemo Login Automation

Selenium + Cucumber (BDD) + JUnit4 test automation suite for the login flow of
[saucedemo.com](https://www.saucedemo.com/).

## Tech Stack

- Java 21
- Maven
- Selenium WebDriver 4.25.0
- Cucumber JVM 7.16.1 (`cucumber-java` + `cucumber-junit`)
- JUnit 4

## Prerequisites

- JDK 21
- Maven
- Google Chrome installed (the default browser used by the suite; Selenium 4's built-in Selenium
  Manager resolves the matching driver automatically)

## Running the Tests

Run the full suite:

```bash
mvn test
```

Run a single test case by tag (each scenario is tagged with its test case ID from
`TestCases/testcases.md`):

```bash
mvn test -Dcucumber.filter.tags="@TC_003"
```

After a run, an HTML report is generated at `target/cucumber-report.html`.

## Project Structure

```
src/main/java/org/example/pages/      Page objects (locators + actions)
src/test/java/base/                   WebDriver lifecycle (BaseClass)
src/test/java/hooks/                  Cucumber Before/After hooks
src/test/java/stepDefinitions/        Step definitions binding Gherkin steps to page objects
src/test/java/runner/                 Cucumber/JUnit test runner
src/test/resources/featureFiles/      Gherkin .feature files
TestCases/                            Manually authored test case documentation (design-level)
ai_prompts/                           Prompt template used to generate test cases
```

## Test Cases

Functional test cases for the login page are documented in `TestCases/testcases.md`
(`testcases.xlsx` mirrors the same content), covering:

- `TC_001` — Successful login with valid credentials
- `TC_002` — Login attempt with invalid password
- `TC_003` — Login attempt with a locked-out user account
- `TC_004` — Field validation for empty username and/or password
- `TC_005` — Login field security and password masking

Each is automated as a tagged scenario in `src/test/resources/featureFiles/Login.feature`.

## Further Documentation

- [`AUTOMATION_GUIDE.md`](AUTOMATION_GUIDE.md) — deep-dive on the automation layers and how to
  automate a new test case.
- [`TESTING_PROCESS.md`](TESTING_PROCESS.md) / `TestingProcess.docx` — full process document
  covering test-case design, the automation workflow, and folder structure (Markdown and Word
  versions of the same content).
