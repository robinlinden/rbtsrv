#!/usr/bin/env python3

import sys
import grpc
import robot_pb2
import robot_pb2_grpc

channel = grpc.insecure_channel("localhost:9876")
stub = robot_pb2_grpc.RobotServiceStub(channel)
request = robot_pb2.RobotInfo(id=sys.argv[1])
response = stub.Add(request)
