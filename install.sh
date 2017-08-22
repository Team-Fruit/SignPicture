#!/bin/bash
cd `dirname $0`
bash gradlew setupDevWorkspace setupDecompWorkspace eclipse
echo 'Press any key to continue . . .'
read Wait