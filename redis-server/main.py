#!/bin/bash
import os
import datetime
import random
import zoneinfo

import redis
from apscheduler.schedulers.blocking import BlockingScheduler

from utils import get_all_h3_code, get_current_time, create_time_key, create_key

redis_server_address = os.environ["REDIS_SERVER_ADDRESS"]
sched = BlockingScheduler()
hcode_list = get_all_h3_code()


@sched.scheduled_job("cron", minute="0,10,20,30,40,50", id="push_realtime_feature")
def push_realtime_feature_into_redis():
    current_time = get_current_time()
    time_key = create_time_key(current_time, "realtime")

    put_data_into_redis(time_key)


@sched.scheduled_job("cron", minute="0", id="push_historical_feature")
def push_historical_feature_into_redis():
    current_time = get_current_time()
    time_key = create_time_key(current_time, "historical")

    put_data_into_redis(time_key)


def put_data_into_redis(time_key):
    feature = {}
    for hcode in hcode_list:
        key = create_key(time_key, hcode)
        feature[key] = int(random.normalvariate(10000, 1000))

    with redis.StrictRedis(host=redis_server_address, port=6379, db=0) as conn:
        conn.mset(feature)


def put_prev_data_into_redis():
    now = get_current_time()
    time = datetime.datetime(now.year, now.month, now.day, 0, 0, 0, tzinfo=zoneinfo.ZoneInfo("Asia/Seoul"))

    while True:
        time += datetime.timedelta(minutes=10)
        if time > now:
            break

        realtime_time_key = create_time_key(time, "realtime")
        put_data_into_redis(realtime_time_key)

        if time.minute == 0:
            historical_time_key = create_time_key(time, "historical")
            put_data_into_redis(historical_time_key)


if __name__ == "__main__":
    put_prev_data_into_redis()
    sched.start()
