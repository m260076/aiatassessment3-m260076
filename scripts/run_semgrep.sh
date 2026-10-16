#!/usr/bin/env bash
# Runs Semgrep's default community rules against the application source
# and saves output for you to quote/discuss in the report.
#
# Usage: ./scripts/run_semgrep.sh
set -euo pipefail

cd "$(dirname "$0")/.."
mkdir -p analysis-output

echo "== Semgrep: scanning src/ =="
semgrep --config auto --config .semgrep/custom-rules.yml src/ | tee analysis-output/semgrep-src.txt || true

echo
echo "Output also saved to analysis-output/semgrep-src.txt"
