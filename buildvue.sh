#!/bin/bash

cd src/main/vuepress
vuepress build docs
rm -rf ../resources/static/assets
cp -R ./docs/.vuepress/dist/* ../resources/static/
cd ../../..