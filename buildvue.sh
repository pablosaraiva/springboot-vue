#!/bin/bash

cd src/main/vuepress
vuepress build
rm -rf src/main/resources/static/assets
cp -R ./.vuepress/dist/* ../resources/static/
cd ../../..