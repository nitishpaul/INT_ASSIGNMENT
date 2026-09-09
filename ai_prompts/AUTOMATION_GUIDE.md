# Automation Guide

This document describes how test-case automation works in this repository — the tools and
framework in use, how the existing Login test cases (`TC_001`–`TC_005`) are implemented end to
end, and the steps to follow when automating additional test cases so the pattern stays
consistent.

## 1. Tools, Technology & Framework

| Concern                 | Choice                                                      |
|--------------------------|-------------------------------------------------------------|
| Language / runtime       | Java 21                                                      |
| Build tool               | Maven (`pom.xml`)                                            |
| Browser automation       | Selenium WebDriver 4.25.0 (`selenium-java`)                  |
| Driver management        | Selenium 4's built-in Selenium Manager (no WebDriverManager dependency; resolves matching browser driver binaries automatically) |
| BDD framework            | Cucumber-JVM 7.16.1 (`cucumber-java` for step definitions, `cucumber-junit` for the JUnit runner) |
| Gherkin spec language    | `.feature` files under `src/test/resources/featureFiles` |
| Test runner / assertions | JUnit 4 (`@RunWith(Cucumber.class)`), `org.junit.Assert` |
| Design pattern           | Page Object Model (POM) — locators + actions encapsulated per page, step definitions call into page objects (no factory/DI layer) |
| Test data / cases        | Manually authored design docs in `TestCases/testcases.md` (mirrored in `testcases.xlsx`), generated from the prompt in `ai_prompts/testcase_generator.md` |
| Reporting                | Cucumber HTML report, written to `target/cucumber-report.html` after each run |
| Browsers supported       | Chrome (default, used by `Hooks`), Edge, Firefox — selected via `BaseClass.initializeDriver(browserType)` |

Run everything with `mvn test`. Run a single test case with
`mvn test -Dcucumber.filter.tags="@TC_00N"`.

## 2. How Automation Is Structured (Layers)

The framework wires a Gherkin scenario down to a browser action through four layers:

```
.feature file (Gherkin)  →  Step Definitions  →  Page Object  →  Selenium WebDriver
        |                         |                    |
   Cucumber tags            asserts outcomes      locators (By) + actions
   (@TC_00N)                via JUnit Assert       for one page
```

1. **Feature files** — `src/test/resources/featureFiles/*.feature`
   Gherkin `Feature` / `Scenario` / `Scenario Outline` blocks describe behavior in plain English.
   Every scenario (or example row) is tagged with the matching test case ID from
   `TestCases/testcases.md` (e.g. `@TC_001`), so any test case can be run in isolation via
   `-Dcucumber.filter.tags`.

2. **Step Definitions** — `src/test/java/stepDefinitions/*Steps.java`
   Java classes annotated with `@Given`/`@When`/`@And`/`@Then` that translate each Gherkin line
   into calls on a page object, then assert the expected outcome with `org.junit.Assert`. Step
   definition classes extend `base.BaseClass` to get access to the shared `WebDriver`.

3. **Page Objects** — `src/main/java/org/example/pages/*Components.java`
   One class per page/component. Holds all `By` locators for that page and exposes intent-revealing
   action/query methods (e.g. `enterUsername`, `clickLogin`, `getErrorMessage`). Step definitions
   instantiate these directly — there is no factory or dependency-injection container.

4. **BaseClass** — `src/test/java/base/BaseClass.java`
   Owns the single `static WebDriver driver`. `initializeDriver(browserType)` switches between
   `chrome` / `edge` / `firefox` and applies common setup (implicit wait, maximize window).
   `navigateTo(url)` and `getDriver()` are the shared accessors used everywhere else.

5. **Hooks** — `src/test/java/hooks/Hooks.java`
   `@BeforeAll` launches the browser once (`initializeDriver("chrome")`); `@AfterAll` quits it once
   the entire suite finishes. **The browser session is shared across all scenarios in a run** — it
   is not reset between scenarios, to avoid relaunching Chrome per scenario.

6. **Runner** — `src/test/java/runner/TestRunner.java`
   JUnit4 `@RunWith(Cucumber.class)` entry point. `@CucumberOptions` wires the `features` path,
   `glue` packages (`stepDefinitions`, `hooks`), the HTML report plugin, and an empty `tags` filter
   (overridden per-run from the command line, never edited directly).

## 3. Existing Automation: Login Feature (Reference Example)

`TestCases/testcases.md` defines five design-level test cases for the SauceDemo login page; all
five are automated in `src/test/resources/featureFiles/Login.feature`:

| Test Case | Scenario type                                   | Tag(s)                     |
|-----------|--------------------------------------------------|-----------------------------|
| TC_001    | Successful login with valid credentials           | `@TC_001`                  |
| TC_002    | Invalid password                                  | `@TC_002` (Examples row)    |
| TC_003    | Locked-out user                                   | `@TC_003` (Examples row)    |
| TC_004    | Empty username and/or password validation         | `@TC_004` (Examples row, 2 cases) |
| TC_005    | Password field masking / not exposed in plain text| `@TC_005`                  |

TC_002–TC_004 share one `Scenario Outline` with an `Examples` table (data-driven), since they all
follow the same steps (enter username → enter password → click login → assert error message) with
different inputs/expected messages. Tags are applied at the `Scenario Outline` level and inherited
by every `Examples` row, plus per-row tags aren't needed since all four rows map to the outline's
combined tag set — filter by `@TC_003` still selects only that scenario outline (all its rows run;
Cucumber tags apply to the whole outline, not individual example rows).

Supporting pieces:
- **Page object**: `LoginPageComponents` — locators for username/password fields, login button,
  product page title, error message; actions `enterUsername`, `enterPassword`, `clickLogin`,
  `login(username, password)`; queries `getProductPageText`, `getErrorMessage`,
  `getPasswordFieldType`.
- **Step definitions**: `LoginSteps` — binds every Gherkin line in `Login.feature` to
  `LoginPageComponents`, asserting with `assertEquals`.

## 4. How to Automate a New Test Case

Follow this checklist to keep new automation consistent with the existing pattern. Steps 1–2
assume the test case is already documented at the design level (see §5 if not).

1. **Write/extend a `.feature` file** in `src/test/resources/featureFiles/`.
   - Reuse an existing feature file if the new test case belongs to the same page/flow (e.g. more
     login scenarios go into `Login.feature`); otherwise create a new one named after the feature
     under test (e.g. `Checkout.feature`).
   - Tag the `Scenario` (or `Scenario Outline`) with the matching ID from `TestCases/testcases.md`,
     e.g. `@TC_006`. Use a `Scenario Outline` + `Examples` table when several test cases only
     differ by input data and expected result (as TC_002–TC_004 do).
   - Reuse existing Gherkin step phrasing where the same action applies (e.g.
     `Given User is on the SauceDemo login page`) so step definitions can be shared instead of
     duplicated.

2. **Add or extend a page object** under `src/main/java/org/example/pages/`.
   - One class per page/component, named `<Page>Components.java`.
   - Constructor takes the shared `WebDriver` (passed in by the step definition, sourced from
     `BaseClass.getDriver()`).
   - Keep all `By` locators as `private final` fields; expose only action methods (`click...`,
     `enter...`, `select...`) and query methods (`get...`) — never leak `By` locators or raw
     `WebElement`s out of the page object.

3. **Add step definitions** under `src/test/java/stepDefinitions/`.
   - Name the class `<Feature>Steps.java`; extend `base.BaseClass`.
   - Each `@Given`/`@When`/`@And`/`@Then` method should do one thing: call into a page object
     method, or assert an outcome via `org.junit.Assert`. Avoid embedding Selenium calls directly
     in step definitions — go through the page object.
   - Use Cucumber parameter placeholders (`{string}`, `{int}`, …) for step data rather than
     hardcoding values, mirroring `enterUsername(String userName)` in `LoginSteps`.

4. **No runner/hook changes needed** for a same-page addition — `TestRunner` already globs all
   `featureFiles` and all `stepDefinitions`/`hooks` packages. Only touch `TestRunner` if a new
   glue package is introduced, and only touch `Hooks` if the new flow needs different setup/teardown
   behavior.

5. **Account for shared browser state.** Since the browser session persists across all scenarios
   in a run (`Hooks` starts it once in `@BeforeAll` and quits it once in `@AfterAll`), any new
   scenario that assumes a "clean" starting point (e.g. logged-out login page) must explicitly
   navigate/reset to that state rather than assuming it — a prior scenario may have left a session
   cookie or navigated elsewhere.

6. **Run and verify:**
   ```bash
   mvn test -Dcucumber.filter.tags="@TC_00N"
   ```
   Check `target/cucumber-report.html` for the result, then run the full suite (`mvn test`) to make
   sure nothing else regressed.

## 5. If the Test Case Doesn't Exist Yet at the Design Level

New functional test cases should first be documented the same way the login ones were, using the
prompt in `ai_prompts/testcase_generator.md` as a template (Test Case ID, Scenario, Preconditions,
Test Data, Test Steps, Expected Result, Priority) and appended to `TestCases/testcases.md`
(and mirrored into `testcases.xlsx`) with the next sequential `TC_00N` ID. Only after the design
doc exists should the `TC_00N` tag be introduced into a `.feature` file per §4.

## 6. Known Gaps / Things to Fix Before Extending

- `BaseClass.initializeDriver`: the `"firefox"` case is missing a `break`, so it falls through into
  `default` and nulls out the driver — fix this before relying on Firefox runs.
- Browser choice is hardcoded to `"chrome"` in `Hooks.setUp()` — parameterize (e.g. via a system
  property or Maven profile) if cross-browser runs are needed.
- No `@Before`/`@After` per-scenario reset exists (by design, to avoid relaunching the browser) —
  new scenarios must handle state cleanup themselves (see §4.5).
