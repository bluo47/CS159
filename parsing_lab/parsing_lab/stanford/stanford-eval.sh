#!/usr/bin/env bash
#

if [ $# -ne 2 ]; then
  echo Usage: `basename $0` '<system_trees> <gold_trees>'
  echo
  exit
fi

scriptdir=`dirname $0`

java -cp "$scriptdir/*:"  edu.stanford.nlp.parser.metrics.Evalb $1 $2
