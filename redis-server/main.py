#!/bin/bash
import os
import datetime
import random
import zoneinfo
import logging

import redis
from apscheduler.schedulers.blocking import BlockingScheduler

from utils import get_all_h3_code, get_current_time, create_time_key, create_key

redis_server_address = os.environ["REDIS_SERVER_ADDRESS"]
sched = BlockingScheduler()
hcode_list = get_all_h3_code()


def push_realtime_feature_into_redis():
    current_time = get_current_time()
    time_key = create_time_key(current_time, "realtime")

    put_data_into_redis(time_key)
    logging.info(f"{current_time} finish to put realtime feature")


def push_historical_feature_into_redis():
    current_time = get_current_time()
    time_key = create_time_key(current_time, "historical")

    put_data_into_redis(time_key)
    logging.info(f"{current_time} finish to put historical feature")


def put_data_into_redis(time_key):
    feature = {}
    for hcode in hcode_list:
        key = create_key(time_key, hcode)
        feature[key] = int(random.normalvariate(10000, 1000))

    with redis.StrictRedis(host=redis_server_address, port=6379, db=0) as conn:
        conn.mset(feature)


def put_prev_data_into_redis():
    now = get_current_time()
    historical_time = now - datetime.timedelta(days=14)
    realtime_time = now - datetime.timedelta(minutes=10)

    realtime_time_key = create_time_key(realtime_time, "realtime")
    put_data_into_redis(realtime_time_key)

    while True:
        historical_time += datetime.timedelta(hours=1)
        if historical_time > now:
            break

        historical_time_key = create_time_key(historical_time, "historical")
        put_data_into_redis(historical_time_key)


if __name__ == "__main__":
    logging.basicConfig(format='%(levelno)s %(message)s', level=logging.INFO)
    put_prev_data_into_redis()
    current_time = get_current_time()
    logging.info(f"{current_time} finish put prev data")
    sched.add_job(push_historical_feature_into_redis, "cron", minute="0", hour="*",
                  timezone=zoneinfo.ZoneInfo("Asia/Seoul"))
    sched.add_job(push_realtime_feature_into_redis, "cron", minute="0,10,20,30,40,50", hour="*",
                  timezone=zoneinfo.ZoneInfo("Asia/Seoul"))
    sched.start()
