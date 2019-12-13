#!/usr/bin/env python3
import sys

import grpc

import hello_pb2
import hello_pb2_grpc

channel = grpc.insecure_channel("localhost:9876")
stub = hello_pb2_grpc.GreeterStub(channel)
request = hello_pb2.HelloRequest(name=sys.argv[1])
response = stub.SayHello(request)
print(response.message)
