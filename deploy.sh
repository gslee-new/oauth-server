
#3
echo "> Git pull"
git pull

echo "> clean gradle"
./gradlew clean

#4
echo "> start project build"
./gradlew build -x test

#7
echo "> 현재 구동중인 애플리케이션 pid 확인"
CURRENT_PID=$(pgrep -f oauth-server-0.0.1-SNAPSHOT.jar)
echo "현재 구동 중인 애플리케이션 pid: $CURRENT_PID"

#8
if [ -z "$CURRENT_PID" ]; then
  echo "> 현재 구동 중인 인스턴스가 없습니다."
else
  echo "> kill -15 $CURRENT_PID"
  kill -15 "$CURRENT_PID"
  sleep 5
fi

echo "> jar Name: oauth-server-0.0.1-SNAPSHOT.jar"
nohup java -jar build/libs/oauth-server-0.0.1-SNAPSHOT.jar &