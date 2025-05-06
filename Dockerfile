# ベースイメージを公式の存在するものに修正
FROM maven:3.9.4-eclipse-temurin-17

# 作業ディレクトリの作成
WORKDIR /app

# pom.xml とソースをコピー
COPY . .

# アプリをビルド（テストはスキップ）
RUN mvn clean package -DskipTests

# JAR ファイルを実行
CMD ["java", "-jar", "target/OnePlateGo-0.0.1-SNAPSHOT.jar"]
