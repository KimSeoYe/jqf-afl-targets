#!/bin/bash

JQF_DIR=~/git/jqf
PROJ_DIR=~/git/jqf-afl-targets/rhino

# Create classpath
cp="$JQF_DIR/fuzz/target/classes:$JQF_DIR/fuzz/target/test-classes:$PROJ_DIR/target/classes"

for jar in $PROJ_DIR/target/dependency/*.jar; do
  cp="$cp:$jar"
done

echo $cp