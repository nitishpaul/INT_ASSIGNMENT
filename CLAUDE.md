# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Overview

A Selenium + Cucumber (BDD) + JUnit4 test automation project (Java 21, Maven) that tests the login
flow of the demo site https://www.saucedemo.com/.

## Commands

- Run all tests: `mvn test` (Surefire picks up `runner/TestRunner.java` via its `Test*` naming pattern).
- Run a single test case: each scenario/example row in `Login.feature` is tagged `@TC_001`..`@TC_005`
  (matching the IDs in `TestCases/testcases.md`), so filter with
  `mvn test -Dcucumber.filter.tags="@TC_003"` (overrides the empty `tags` in `TestRunner`'s
  `@CucumberOptions` without editing it).
- Cucumber HTML report is written to `target/cucumber-report.html` after a run.
- No dedicated lint config exists beyond the standard `mvn compile`.
- On a machine with no `mvn`/`java` on PATH, IntelliJ ships both: use its bundled Maven
  (`...\IntelliJ IDEA <version>\plugins\maven\lib\maven3\bin\mvn.cmd`) with
  `JAVA_HOME` pointed at its bundled JBR (`...\IntelliJ IDEA <version>\jbr`).

## Architecture

This is a standard Page-Object-Model BDD framework wiring Cucumber glue code to Selenium:

- `src/test/resources/featureFiles/*.feature` — Gherkin scenarios, tagged per test case ID.
- `src/test/java/stepDefinitions/*Steps.java` — step definitions that translate Gherkin steps into
  calls on page objects. Each step class extends `base.BaseClass` to get driver access.
- `src/main/java/org/example/pages/*Components.java` — page objects encapsulating locators
  (`By`) and actions for one page; step definitions instantiate these directly (no factory/DI).
- `src/test/java/base/BaseClass.java` — owns the single `static WebDriver driver` instance.
  `initializeDriver(browserType)` (static) switches between `chrome`/`edge`/`firefox` (relies on
  Selenium 4's built-in Selenium Manager to resolve driver binaries — no WebDriverManager
  dependency). Note: the `firefox` case is missing a `break`, so it currently falls through into
  `default` and nulls out the driver — worth fixing before relying on Firefox runs.
- `src/test/java/hooks/Hooks.java` — `@BeforeAll`/`@AfterAll` hooks that launch the browser once and
  quit it once the entire suite finishes; all scenarios in a run share the same browser session
  (not reset between scenarios) to avoid relaunching Chrome per scenario.
- `src/test/java/runner/TestRunner.java` — JUnit4 `@RunWith(Cucumber.class)` entry point wiring
  `features` path to `glue` packages (`stepDefinitions`, `hooks`).

Adding a new flow generally means: write/extend a `.feature` file (tagged with the matching test
case ID), add a page object under `org/example/pages`, add matching step definitions, and reuse
`BaseClass` for driver access.

Because the browser session is shared across scenarios, any new scenario that depends on being on
a "logged-out" login page must account for state left behind by a prior scenario (e.g. a session
cookie from a successful login) rather than assuming a clean browser.

## TestCases/

Contains manually authored functional test case documentation (`testcases.md`, mirrored in
`testcases.xlsx`) for the SauceDemo login page, generated from the prompt in
`ai_prompts/testcase_generator.md`. These are design-level test cases (not automation code); the
automated Cucumber scenarios in `src/test/resources/featureFiles` implement them and are tagged
with the matching `@TC_00N` IDs.

## Other docs in this repo

- `README.md` — quick start (tech stack, prerequisites, running tests).
- `AUTOMATION_GUIDE.md` — deep-dive on the automation layers and the checklist for automating a
  new test case.
- `TESTING_PROCESS.md` / `TestingProcess.docx` — end-to-end process doc covering test-case design,
  the automation workflow, and folder structure (Markdown and Word versions of the same content).
