#!/bin/bash

APP_NAME=shoppingmall
JAR_NAME=shoppingmall-0.0.1-SNAPSHOT.jar
JAR_PATH=./build/libs/$JAR_NAME
LOG_PATH=app.log

# 1. 실행 중인 프로세스 종료
PID=$(pgrep -f $JAR_NAME)
if [ -n "$PID" ]; then
  echo "Stopping running process: $PID"
  kill -15 "$PID"
  sleep 5
fi

# 2. JAR 파일 존재 여부 확인
if [ ! -f "$JAR_PATH" ]; then
  echo "❌ JAR 파일이 존재하지 않습니다: $JAR_PATH"
  echo "CI/CD에서 JAR 복사 확인 필요"
  exit 1
fi

# 3. 애플리케이션 실행
echo "Starting application..."
nohup java \
  -Duser.timezone=Asia/Seoul \
  -Dspring.datasource.url="$SPRING_DATASOURCE_URL" \
  -Dspring.datasource.username="$SPRING_DATASOURCE_USERNAME" \
  -Dspring.datasource.password="$SPRING_DATASOURCE_PASSWORD" \
  -Djwt.secret-key="$JWT_SECRET_KEY" \
  -Djwt.access-token-expiration="$JWT_ACCESS_TOKEN_EXPIRATION" \
  -Djwt.refresh-token-expiration="$JWT_REFRESH_TOKEN_EXPIRATION" \
  -Dtoss.testClientKey="$TOSS_TEST_CLIENT_KEY" \
  -Dtoss.testSecretKey="$TOSS_TEST_SECRET_KEY" \
  -Dcloud.aws.credentials.access-key="$CLOUD_AWS_ACCESS_KEY" \
  -Dcloud.aws.credentials.secret-key="$CLOUD_AWS_SECRET_KEY" \
  -Dcloud.aws.region.static="$CLOUD_AWS_REGION" \
  -Dcloud.aws.s3.bucket="$CLOUD_AWS_S3_BUCKET" \
  -Dspring.mail.username="$MAIL_USERNAME" \
  -Dspring.mail.password="$MAIL_PASSWORD" \
  -jar "$JAR_PATH" --spring.profiles.active=prod > "$LOG_PATH" 2>&1 &

echo "✅ 배포 스크립트 완료. 로그 확인: tail -f $LOG_PATH"
