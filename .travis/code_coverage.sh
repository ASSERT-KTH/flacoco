#!/bin/bash

# Verify SHASUM
curl -fLso codecov https://codecov.io/bash;
VERSION=$(grep -o 'VERSION=\"[0-9\.]*\"' codecov | cut -d'"' -f2);
for i in 1 256 512
do
  shasum -a $i -c --ignore-missing <(curl -s "https://raw.githubusercontent.com/codecov/codecov-bash/${VERSION}/SHA${i}SUM")
done

# Upload code coverage report
bash codecov