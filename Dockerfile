FROM alpine:latest
WORKDIR /usr/src/app
COPY hello.sh .
RUN chmod +x hello.sh
CMD ["./hello.sh"]