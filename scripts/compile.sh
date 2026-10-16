#!/usr/bin/env bash
# Compiles the application source in src/.
set -euo pipefail

cd "$(dirname "$0")/.."
mkdir -p build
javac -d build src/*.java
echo "Compiled classes are in ./build"
