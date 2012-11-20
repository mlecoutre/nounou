#!/bin/sh

echo "Starting application..."
# This should be done with maven
# ./start-application.sh
/usr/bin/env node ./src/test/js/run-acceptance-specs.js
echo "Stopping application..."
#./stop-application.sh
echo "Done."