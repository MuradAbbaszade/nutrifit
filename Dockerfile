FROM gradle:7.2-jdk11

WORKDIR /nutrifit
COPY . .
RUN gradle clean build

CMD gradle bootRun