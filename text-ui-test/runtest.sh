#!/usr/bin/env bash

set -euo pipefail

# create bin directory if it doesn't exist
if [ ! -d "../bin" ]
then
    mkdir ../bin
fi

# clean previous ACTUAL files
rm -f ./ACTUAL*.TXT EXPECTED-UNIX.TXT

# compile the code into the bin folder, terminates if error occurred
# Compile all Java sources recursively to support packages
if ! javac -Xlint:none -d ../bin $(find ../src/main/java -name "*.java")
then
    echo "********** BUILD FAILURE **********"
    exit 1
fi

REGEN=0
if [[ "${1-}" == "--regen" ]]; then
  REGEN=1
  echo "Regenerating expected baselines from current program output..."
fi

normalize_files() {
  # Try dos2unix if available; otherwise fallback to Perl
  if command -v dos2unix >/dev/null 2>&1; then
    dos2unix "$@" >/dev/null 2>&1 || true
  else
    perl -pi -e 's/\r$//' "$@"
  fi
  # Ensure exactly one trailing newline
  perl -0777 -pe 's/\n+\z/\n/' -i "$@"
}

run_case() {
  local name="$1"
  local in_file="$2"
  local exp_file="$3"
  local actual_file="ACTUAL-${name}.TXT"

  echo "Running case: $name"

  if [ ! -f "$in_file" ]; then
    echo "  -> SKIPPED (missing input file: $in_file)"
    return 0
  fi

  # run the program, feed commands from input file and redirect the output
  if ! java -classpath ../bin nixchats.NixChats < "$in_file" > "$actual_file"; then
    echo "  -> RUNTIME ERROR (program exited with error)"
    [ $REGEN -eq 1 ] && echo "  -> Skipping baseline regen due to runtime error."
    return 1
  fi

  # If --regen provided, update expected baseline from actual output (backup existing)
  if [ $REGEN -eq 1 ]; then
    if [ -f "$exp_file" ]; then
      cp "$exp_file" "${exp_file}.bak"
    fi
    cp "$actual_file" "$exp_file"
  fi

  if [ ! -f "$exp_file" ]; then
    echo "  -> FAILED (missing expected file: $exp_file). Run with --regen to create."
    return 1
  fi

  # convert to UNIX format and normalize final trailing newline
  cp "$exp_file" EXPECTED-UNIX.TXT
  normalize_files "$actual_file" EXPECTED-UNIX.TXT

  # compare the output to the expected output
  if diff "$actual_file" EXPECTED-UNIX.TXT >/dev/null; then
    echo "  -> PASSED ✅"
    return 0
  else
    echo "  -> FAILED ❌"
    echo "First differences (side-by-side preview, first 60 lines):"
    diff -y -W 160 "$actual_file" EXPECTED-UNIX.TXT | sed -n '1,60p'
    return 1
  fi
}

overall=0
# Original/basic test (kept for backward compatibility)
run_case "BASIC" "input.txt" "EXPECTED.TXT" || overall=1
# New: valid inputs test
run_case "VALID" "input-valid.txt" "EXPECTED-valid.TXT" || overall=1
# New: invalid inputs test
run_case "INVALID" "input-invalid.txt" "EXPECTED-invalid.TXT" || overall=1

if [ $REGEN -eq 1 ]; then
  echo "Baselines regenerated where missing or outdated. Re-run without --regen to verify."
  exit 0
fi

if [ $overall -eq 0 ]
then
    echo "Test result: PASSED ✅"
    exit 0
else
    echo "Test result: FAILED ❌"
    exit 1
fi