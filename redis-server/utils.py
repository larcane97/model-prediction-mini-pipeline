import zoneinfo
import datetime
import random
import h3

def get_current_time(timezone="Asia/Seoul"):
    kst = zoneinfo.ZoneInfo(timezone)
    return datetime.datetime.now(tz=kst)


def create_time_key(current_time, key_type):
    assert key_type in ["realtime", "historical"], "key type should be either realtime or historical"
    if key_type == "realtime":
        time_str = current_time.strftime("%y%m%d%H%M")
        time_key_list = list(time_str)
        time_key_list[-1] = "0"
        time_key = "".join(time_key_list)
    else:
        time_key = current_time.strftime("%y%m%d%H")
    return time_key


def create_key(time_key, hcode):
    return hcode + time_key


def get_all_h3_code():
    # 대한민국 전체 지역 경계
    min_lat, max_lat = 33.0041, 38.6249
    min_lng, max_lng = 124.5862, 131.0862

    # H3 res7 코드 생성
    hexagons = set()
    for lat in range(int(min_lat * 10), int(max_lat * 10)):
        for lng in range(int(min_lng * 10), int(max_lng * 10)):
            hexagons.add(h3.geo_to_h3(lat / 10, lng / 10, 7))

    return hexagons