#!/usr/bin/env bash

pushd ${COMMON_MODULE_DIR}
mvn clean install
popd

pushd ${CURRENT_SERVICE_SRC}
mvn clean install
eval $(minikube docker-env)
docker build -t ${CURRENT_CONTAINER_IMAGE} .
popd
