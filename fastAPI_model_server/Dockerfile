FROM python:3.9.6-alpine

COPY fastAPI_model_server/. /application
WORKDIR /application

RUN pip3 install --upgrade pip && \
    pip3 install -r requirements.txt

EXPOSE 8000

ENTRYPOINT ["python3","-m","uvicorn","model_server:app","--reload","--host","0.0.0.0"]