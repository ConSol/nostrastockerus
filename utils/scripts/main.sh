#!/usr/bin/env bash

source vars.sh

kubectl create namespace ${K8S_NAMESPACE}
source set-k8s-namespace.sh
echo "created namespace ${K8S_NAMESPACE} and set it as default"

source helm-install-prometheus-operator.sh
source deploy-zipkin.sh
echo "deployed monitoring and tracing infrastructure"

source gen-keystores.sh
source create-k8s-secrets.sh
echo "generated keys and certs and created secrets"

pushd ../..
mvn clean install
popd
echo "built services"

eval $(minikube docker-env)
for SERVICE_NAME in "${SERVICES[@]}"
do
    export CURRENT_SERVICE_NAME=${SERVICE_NAME}
    export CURRENT_SERVICE_SRC=$(realpath ../../${CURRENT_SERVICE_NAME})
    export CURRENT_CONTAINER_IMAGE=containerimagefor${CURRENT_SERVICE_NAME}
    pushd ${CURRENT_SERVICE_SRC}
    docker build -t ${CURRENT_CONTAINER_IMAGE} .
    echo "built image for ${CURRENT_SERVICE_NAME}"
    popd
done

for SERVICE_NAME in "user" "prophecy"
do
    export CURRENT_SERVICE_NAME=${SERVICE_NAME}
    export CURRENT_SERVICE_SRC=$(realpath ../../${CURRENT_SERVICE_NAME})
    export CURRENT_CONTAINER_IMAGE=containerimagefor${CURRENT_SERVICE_NAME}
    source deploy-to-k8s.sh
    echo "deployed ${CURRENT_SERVICE_NAME}"
done

echo "waiting 1 minute for user service to be available..."
sleep 1m
source create-job-token-secret.sh
echo "created secret for job token"

export CURRENT_SERVICE_NAME=stats
export CURRENT_SERVICE_SRC=$(realpath ../../${CURRENT_SERVICE_NAME})
export CURRENT_CONTAINER_IMAGE=containerimagefor${CURRENT_SERVICE_NAME}
source deploy-to-k8s.sh
echo "deployed ${CURRENT_SERVICE_NAME}"
