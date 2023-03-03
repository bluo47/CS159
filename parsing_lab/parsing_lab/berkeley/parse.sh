#!/usr/bin/env bash
#

if [ $# -ne 2 ]; then
  echo Usage: `basename $0` '<input_file> <output_file>'
  echo
  exit
fi

scriptdir=`dirname $0`

java -mx2g -jar $scriptdir/BerkeleyParser-1.7.jar -gr $scriptdir/eng_sm6.gr < $1 > $2
