#!/bin/bash
if ! [ -d ./creations ] ; then
  mkdir ./creations
fi

if [ -e ./creations/"$1" ] ; then
  exit 1
fi

mv ./.temp ./creations/"$1"
exit 0