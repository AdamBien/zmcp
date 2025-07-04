#!/bin/sh
BASEDIR=$(dirname $0)
java -cp ${BASEDIR}/zmcp.jar:${BASEDIR}/zmcp-* airhacks.App "$@"