# Worked example: practising the workflow

Do this once, on this toy file, before touching the real app in `src/`.
The point is to get comfortable with what JBMC and Semgrep actually
output — with a bug simple enough to spot by eye — so the real
analysis in your report is about the vulnerabilities, not about
learning the tools for the first time.

## 1. Compile it

```bash
cd worked-example
javac ArraySample.java
```

## 2. Run it normally

```bash
java ArraySample
```

This will likely throw an `ArrayIndexOutOfBoundsException` at runtime —
which is itself a first, very direct piece of dynamic evidence: you ran
the program and it crashed. Note the exact exception and stack trace.

## 3. Ask JBMC to prove it without running it

```bash
jbmc --classpath . ArraySample --function ArraySample.sumAll --unwind 5 --no-unwinding-assertions --property 'java::ArraySample.sumAll:([I)I.array-index-out-of-bounds-high.1'
```

JBMC treats the array's contents/length as unconstrained (symbolic) and
tries to prove `sumAll` never violates the property named above — the
array-bounds check on line 15. Since the bug is real, it reports
`FAILURE`. Each flag here is doing something specific:

- `--unwind 5` caps how many times JBMC will unroll the loop — needed
  because the array's length is unconstrained, so without a cap JBMC
  would try to explore unboundedly many iterations.
- `--property '...'` tells JBMC to check only this one property.
  Without it, JBMC also checks (and would report as `FAILURE`) whether
  `values` could be `null` — a real but different issue, not what this
  exercise is about.
- `--no-unwinding-assertions` suppresses a separate warning JBMC would
  otherwise raise: that it can't *prove* 5 iterations was enough to
  cover every possible array length. That's a genuine limitation of
  bounded model checking on unconstrained-size inputs — worth knowing
  exists, but not something to chase for this exercise, since the real
  violation shows up well within the bound regardless.

Notice there's no `--trace` here, and that's deliberate. Normally you'd
want the trace to extract a concrete witness value to test manually —
but `main` already gives you one for free, since you ran it in step 2
and it crashed. For the real graded app, where you won't already have a
witness from `main`, add `--trace` back in and expect a much longer,
state-by-state output.

## 4. Ask Semgrep to flag it statically

```bash
semgrep --config auto ArraySample.java
```

Semgrep won't necessarily have a built-in rule for this exact
off-by-one pattern (it's more JBMC's territory) — that's fine and
itself worth noting: not every tool catches every bug, and part of the
real task is explaining *why* a given tool did or didn't flag
something.

## 5. Connect the three pieces

You now have three independent pieces of evidence for the same bug:
the real crash (step 2), JBMC's symbolic counterexample (step 3), and
whatever Semgrep did or didn't say (step 4). That triangulation —
finding → verification → real execution — is exactly the structure
you'll repeat for each vulnerability in the real app.

Once this makes sense, move to `src/` and start on the actual graded
analysis.
