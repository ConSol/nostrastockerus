#!/usr/bin/env bash

source build-image.sh
source deploy-to-k8s.sh

# force RS to redeploy pod
POD=$(source get-pod.sh ${CURRENT_SERVICE_NAME})
kubectl delete pods ${POD}
