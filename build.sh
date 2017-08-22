#!/bin/bash
cd `dirname $0`
bash gradlew build
echo 'Press any key to continue . . .'
read Wait