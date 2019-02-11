#!/usr/bin/env bash

export K8S_NAMESPACE=microprofile-article
export SERVICES=("user" "prophecy" "stats")
export PASSWORD_FOR_KEYS_AND_KEY_STORES=secret
export KEYSTORES_DIR=$(realpath ../keystores)
export PROMETHEUS_OPERATOR_RELEASE_NAME=microprofile-article
export COMMON_MODULE_DIR=$(realpath ../../common)

export CURRENT_SERVICE_NAME=user
export CURRENT_SERVICE_SRC=$(realpath ../../${CURRENT_SERVICE_NAME})
export CURRENT_CONTAINER_IMAGE=containerimagefor${CURRENT_SERVICE_NAME}
