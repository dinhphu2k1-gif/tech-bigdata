#!/bin/bash

echo ""

echo -e "\nbuild docker hadoop image\n"
sudo docker build -t dinhphu/hadoop-spark:1.0.0 .

echo ""