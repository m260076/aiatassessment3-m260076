#!/usr/bin/env bash
# Runs the full pipeline: compile, then both tools.
set -euo pipefail

cd "$(dirname "$0")/.."
./scripts/compile.sh
./scripts/run_jbmc.sh
./scripts/run_semgrep.sh

echo
echo "All done. Findings saved under ./analysis-output/"
echo "Remember: a tool finding is a starting point, not the report itself —"
echo "you still need to construct/run a real triggering input for each"
echo "vulnerability and document what actually happens (see the top-level README)."
