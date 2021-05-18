#!/bin/bash

find ./ -type f -name '*.sh' -exec perl -pi -e 's/^M//g' {} \;
