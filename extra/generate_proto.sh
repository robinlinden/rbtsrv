#!/usr/bin/env sh
py -3 -m grpc_tools.protoc -I../src/main/proto --python_out=. --grpc_python_out=. hello.proto
