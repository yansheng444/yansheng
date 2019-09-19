#!/bin/bash

set -e
cd `dirname "$0"`

TODAY=`date +"%Y%m%d"`


echo "Restarting application ..."

./shutdown.sh && ./start.sh dtauth.log.${TODAY}


