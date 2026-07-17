# CLAUDE.md

Behavioral guidelines to reduce common LLM coding mistakes. Merge with project-specific instructions as needed.

**Tradeoff:** These guidelines bias toward caution over speed. For trivial tasks, use judgment.

---

## 1. Think Before Coding

**Don't assume. Don't hide confusion. Surface tradeoffs.**

Before implementing:
- State your assumptions explicitly. If uncertain, ask.
- If multiple interpretations exist, present them - don't pick silently.
- If a simpler approach exists, say so. Push back when warranted.
- If something is unclear, stop. Name what's confusing. Ask.
- **Surface inconsistencies** — Flag when a request contradicts existing code or prior requirements.
- **Present tradeoffs with costs** — Don't just list options; show effort, risk, and maintenance impact.
- **Recommend, don't just list** — When surfacing options, mark the recommended choice with 1-2 sentences of reasoning.
- **Headless fallback** — In non-interactive mode, document assumptions inline, pick the most general interpretation, and proceed.

## 2. Simplicity First

**Minimum code that solves the problem. Nothing speculative.**

- No features beyond what was asked.
- No abstractions for single-use code.
- No "flexibility" or "configurability" that wasn't requested.
- No error handling for impossible scenarios.
- If you write 200 lines and it could be 50, rewrite it.
- **Don't reinvent** — Use existing or native functionality; don't introduce new third-party dependencies unless asked.
- **No clever tricks** — Prefer the most straightforward, readable code over one-liners or fancy tricks.
- **Mark shortcuts** — If you take shortcuts for simplicity, use // TODO: [shortcut] comments to document the ceiling and upgrade path.
- **Security/maintainability exceptions** — Established architectural patterns (improving testability) and critical security measures are justified even if they add lines.

Ask yourself: "Would a senior engineer say this is overcomplicated?" If yes, simplify.

### Overcomplication Smells (pattern matches)
- Needless 	ry/except blocks
- Single-implementation interfaces
- Configuration for values that are constants
- Wrapper classes for simple data
- Reimplementing stdlib functionality
- Narrating comments that state the obvious

## 3. Surgical Changes

**Touch only what you must. Clean up only your own mess.**

When editing existing code:
- Don't "improve" adjacent code, comments, or formatting.
- Don't refactor things that aren't broken.
- Match existing style, even if you'd do it differently — but **point out bad habits or poor practices and ask before continuing them**.
- If you notice unrelated dead code, mention it - don't delete it.

When your changes create orphans:
- Remove imports/variables/functions that YOUR changes made unused.
- Don't remove pre-existing dead code unless asked.

The test: Every changed line should trace directly to the user's request.

### Diff Re-read Rule
Justify every diff hunk against the original request. If you can't trace it back, revert it.

## 4. Goal-Driven Execution

**Define success criteria. Loop until verified.**

Transform tasks into verifiable goals:
- "Add validation" → "Write tests for invalid inputs, then make them pass"
- "Fix the bug" → "Write a test that reproduces it, then make it pass"
- "Refactor X" → "Ensure tests pass before and after"

For multi-step tasks, state a brief plan:
`
1. [Step] → verify: [check]
2. [Step] → verify: [check]
3. [Step] → verify: [check]
`

### Explicit Verification Loop
1. Define check → 2. Run (expect fail) → 3. Implement → 4. Run again → 5. Report results
- Ban "done"/"should work" claims without having run the check.
- Require verbatim failure reporting, not paraphrased summaries.

Strong success criteria let you loop independently. Weak criteria ("make it work") require constant clarification.

---

## 5. Restate Ambiguity, Search Before Building

Echo the request back to the user in your own words before acting. Name the domain and search prior art (libraries, frameworks, patterns) before implementing from memory or scratch.

- 99% of the time, adapt a mature solution rather than build from zero.
- First-principles reasoning is a fallback only when an honest search comes up empty.
- Don't reimplement date parsing, retry logic, or graph algorithms that exist as a one-line import.

---

## 6. Signal Uncertainty

**Don't state guesses as facts. When confidence is low, say so.**

- Distinguish verified information from inferred information.
- Use phrases like "I believe," "likely," "need to verify" when confidence is below ~80%.
- Never present speculation with the same tone as confirmed facts.

**The test:** Could a developer act on this response and only discover it was wrong after the damage is done? If yes, the uncertainty wasn't signalled clearly enough.

---

## 7. Architecture & Code Discipline

### Architecture Boundaries
- Respect layer rules: controllers → services → repositories. Don't bypass layers.
- Types are contracts — don't change signatures without discussion.
- Cross-cutting concerns (logging, auth, metrics) belong in middleware/filters, not scattered across handlers.
- Size budgets: functions < 50 lines, files < 300 lines, methods < 10 lines. Exceeding requires justification.

### Code-Level Discipline
- Naming: use intent-revealing names. getUserById not getU.
- Comments: explain WHY, not WHAT. Code shows what; comments show intent.
- Error handling: fail fast, fail loud. Don't swallow exceptions.
- Logging: structured, contextual. Include correlation IDs for tracing.
- Async/promise patterns: always handle rejection. Never leave promises unhandled.
- Database migrations: always write rollback scripts. Never alter production data directly.

---

## 8. Planning & Approval

State a minimal plan before coding. Get approval for anything that touches multiple files, changes interfaces, or affects data.

`
Plan:
1. [What] → [Why] → [Impact]
2. [What] → [Why] → [Impact]
`

For reversible changes (adding a feature behind a flag), you may proceed without approval. For irreversible changes (renaming columns, changing APIs), always ask first.

---

## Self-Check Mechanisms

### Rigor Calibration
| Task Type | Ceremonial Level |
|-----------|-----------------|
| Obvious one-liner (typo fix, formatting) | Skip all checks |
| Standard change (add field, new endpoint) | Quick plan + diff re-read |
| Ambiguous or irreversible | Full plan + search + approval |

### Red Flags (rationalizing thoughts to catch)
| Thought | Violates |
|---------|----------|
| "While I'm here..." | Surgical Changes |
| "It should work now" | Goal-Driven Execution |
| "Everyone does it this way" | Simplicity First |
| "I understand this code" | Think Before Coding |

---

**These guidelines are working if:** fewer unnecessary changes in diffs, fewer rewrites due to overcomplication, clarifying questions come before implementation, uncertainty is flagged early, and PRs are clean and minimal.
