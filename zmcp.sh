#!/bin/sh
BASEDIR=$(dirname $0)
java -cp "${BASEDIR}/zmcp/*" airhacks.App "$@"