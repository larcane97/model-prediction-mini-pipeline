FROM python:3.9.6

COPY redis-server/. /application
WORKDIR /application

RUN pip3 install --upgrade pip
RUN pip3 install -r requirements.txt

ENV REDIS_SERVER_ADDRESS "redis-test"

ENTRYPOINT ["python3","main.py"]