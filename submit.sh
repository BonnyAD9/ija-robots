#!/usr/bin/sh

rm -r submit &> /dev/null

set -e

mkdir submit
mkdir submit/xsleza26

SUB=submit/xsleza26

cd requirements
make
cd ..
mv requirements/document.pdf $SUB/requirements.pdf

cp -r src data pom.xml readme.txt $SUB/

cd submit
zip xsleza26.zip `find -name '*'`
