#!/usr/bin/env bash

pushd ${CURRENT_SERVICE_SRC}
kubectl apply -f k8s.yaml --namespace=${K8S_NAMESPACE}
popd
