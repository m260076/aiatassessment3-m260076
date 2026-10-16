#!/usr/bin/env bash
# Runs JBMC against the compiled application classes and saves output
# for you to quote/discuss in the report.
#
# Usage: ./scripts/run_jbmc.sh
#
# Adjust the --function targets below if you want to check a different
# method than the ones listed. Run `jbmc --help` for the full flag list
# (e.g. --trace for a fuller counterexample, --unwind N to change the
# loop-unwinding bound).
set -euo pipefail

cd "$(dirname "$0")/.."
mkdir -p analysis-output

echo "== JBMC: PricingService.getDiscountForTier =="
jbmc --classpath build PricingService --function PricingService.getDiscountForTier --trace \
    --property 'java::PricingService.getDiscountForTier:(I)D.array-index-out-of-bounds-high.1' \
    --property 'java::PricingService.getDiscountForTier:(I)D.array-index-out-of-bounds-low.1' \
    | tee analysis-output/jbmc-pricingservice.txt || true

echo
echo "Output also saved to analysis-output/jbmc-pricingservice.txt"
