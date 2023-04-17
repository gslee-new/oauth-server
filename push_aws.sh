#!/bin/bash
echo ">>> build jar"
sudo gradle clean build -x test

echo ">>> docker build"
docker build -t ecr .

echo ">>> aws ecr login"
aws ecr get-login-password --region ap-northeast-2 --profile default | docker login --username AWS --password-stdin 817875202538.dkr.ecr.ap-northeast-2.amazonaws.com

echo ">>> docker tag for 817875202538.dkr.ecr.ap-northeast-2.amazonaws.com/ecr:latest"
docker tag ecr:latest 817875202538.dkr.ecr.ap-northeast-2.amazonaws.com/ecr:latest

echo ">>> push image to ecr"
docker push 817875202538.dkr.ecr.ap-northeast-2.amazonaws.com/ecr:latest
