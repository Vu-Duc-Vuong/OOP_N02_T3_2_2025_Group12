#!/bin/bash
# Wrapper script for start.sh
cd "$(dirname "$0")"
./scripts/core/start.sh "$@"
