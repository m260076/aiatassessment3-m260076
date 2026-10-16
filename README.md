# Assessment 3 — Software Vulnerability Analysis: Starter Repository

This repository gives you a ready-to-use environment for the assessment —
you should not need to install anything locally.

**Full task instructions, word count, submission process, and the marking
rubric are on the LMS.** This README only covers the technical setup.

## 1. Get your own copy

Click **"Use this template"** on GitHub (top of the repo page) and create
the new repository under **your own personal GitHub account** — not a
shared/organisation account. This matters: GitHub's free Codespaces usage
quota only applies to personal accounts, so forking under your own account
is what keeps this free for you.

<img width="2554" height="1026" alt="image" src="https://github.com/user-attachments/assets/c4046cf5-41fd-4e65-930c-3958d23a3006" />


## 2. Open it in a Codespace

On your new repository, click **Code → Codespaces → Create codespace on
main**. This will take a minute or two to build the first time — it's
installing Java, JBMC, and Semgrep for you automatically. You don't need to
install anything yourself.

<img width="2558" height="1116" alt="image" src="https://github.com/user-attachments/assets/10cf774f-d898-4c55-b794-b2ada8976cfe" />



Once it's open, the terminal at the bottom of the VS Code window is where
you'll run everything below.

## 3. Practise on the worked example first

Before touching the real application, work through
`worked-example/WALKTHROUGH.md`. It's a small toy file with one obvious bug,
designed to get you comfortable with what JBMC and Semgrep output, so the
real analysis is about the vulnerabilities — not about learning the tools
for the first time. 

## 4. Analyse the real application

The application you're assessing is in `src/`. You can run everything with
one command:

```bash
./scripts/run_all.sh
```

or run each step individually:

```bash
./scripts/compile.sh       # compiles src/ into build/
./scripts/run_jbmc.sh      # runs JBMC against the compiled classes
./scripts/run_semgrep.sh   # runs Semgrep against the source
```

Tool output is also saved under `analysis-output/` so you can quote from it
in your report.

Run the tools and confirm you have the failing output saved before you start applying any fixes. 


## 5. Construct and run your own test inputs

Tool output is a starting point, not the finding itself. For each
vulnerability you identify, you also need to **construct a concrete input
and actually run it** against the relevant method, and document what really
happens — not just what the tool predicted. Both `PricingService.java` and
`LoginService.java` have a small `main` method you can run directly for
this, e.g.:

```bash
java -cp build PricingService 99
java -cp build LoginService "' OR '1'='1" "anything"
```

Adjust the arguments to construct the actual test cases you need for your
analysis — the examples above are just there to show you the mechanism, not
to hand you the findings.

## 6. Stopping your Codespace

When you're done for the session, stop the Codespace (Codespace menu →
"Stop codespace") rather than just closing the tab — this preserves your
free quota. It auto-stops after 30 minutes idle regardless, but stopping it
yourself when you're finished is good practice.

## Troubleshooting

If `jbmc`, `javac`, or `semgrep` aren't found, the container may still be
building — check the terminal output from when the Codespace started. If a
command still fails after that, reach out on the LMS or mmulye@aiat.edu.au rather than losing
time on it.
