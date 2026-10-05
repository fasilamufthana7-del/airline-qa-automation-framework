# Phase 1 — Setup Instructions

## 1. Import into Eclipse
1. Unzip this project somewhere on your machine.
2. Eclipse → File → Import → Maven → Existing Maven Projects.
3. Browse to the unzipped `banking-ai-framework` folder → Finish.
4. Eclipse will read `pom.xml` and download Selenium, TestNG, and
   WebDriverManager automatically (needs internet). This can take a
   couple of minutes the first time.

## 2. Get a ParaBank test account
ParaBank doesn't have one fixed public login that's guaranteed to work.
Go to https://parabank.parasoft.com/parabank/register.htm and register a
free account (takes a minute, no real details needed).

## 3. Add your credentials
Open `src/test/resources/config.properties` and replace the placeholder
values with the username/password you just registered.

**Never commit real credentials to GitHub later** — when you get to
Phase 4, add `config.properties` to `.gitignore` or switch to reading
from environment variables.

## 4. Run the test

**From Eclipse:**
Right-click `testng.xml` → Run As → TestNG Suite.

**From command line** (open a terminal in the project folder):
```
mvn test
```

A Chrome window should open, navigate to ParaBank, log in, and you
should see `BUILD SUCCESS` (or a clear failure message) in the console.

## If something breaks
Common first-run issues:
- **Chrome not installed / wrong version** — WebDriverManager needs
  Chrome installed on your machine; it handles the driver version
  matching for you.
- **`config.properties not found`** — make sure it's inside
  `src/test/resources`, not `src/test/java`.
- **Login fails / no error but no overview page either** — ParaBank's
  demo site occasionally changes its HTML; open DevTools (F12) on the
  actual login page and confirm the locators in `LoginPage.java`
  (`name="username"`, `name="password"`) still match.

## Once this passes
Come back and tell me — we'll move to Phase 2 (Cucumber BDD feature
files) next.
