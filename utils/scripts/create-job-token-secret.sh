#!/usr/bin/env bash

source vars.sh

source get-admin-token.sh
JWT=$(cat admin-token.txt)
AUTHORIZATION_HEADER="Authorization: Bearer ${JWT}"

kubectl create secret generic job-token \
    --from-literal=AUTHORIZATION_HEADER="${AUTHORIZATION_HEADER}" \
    --namespace=${K8S_NAMESPACE} \
    --dry-run \
    -o yaml \
        | kubectl apply -f -
