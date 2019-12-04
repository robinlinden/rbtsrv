#!/usr/bin/env python3

import asyncio

from nats.aio.client import Client as NATS
from nats.aio.errors import ErrConnectionClosed, ErrTimeout, ErrNoServers

async def run(loop):
    nc = NATS()

    await nc.connect("nats://127.0.0.1:4222", loop=loop)

    async def message_handler(msg):
        subject = msg.subject
        data = msg.data.decode()
        print(f"Received a message on '{subject}': {data}", flush=True)

    sid = await nc.subscribe("*", cb=message_handler)

    await asyncio.sleep(30)

    await nc.drain()

loop = asyncio.get_event_loop()
loop.run_until_complete(run(loop))
loop.close()
