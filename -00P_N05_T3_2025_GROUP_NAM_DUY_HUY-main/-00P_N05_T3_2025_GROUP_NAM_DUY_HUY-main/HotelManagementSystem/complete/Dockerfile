# ---------- Build stage ----------
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /workspace
COPY pom.xml .
RUN mvn -q -DskipTests dependency:go-offline
COPY src ./src
RUN mvn -q -DskipTests package

# ---------- Run stage ----------
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /workspace/target/*.jar app.jar

# (DEMO) Gắn thẳng thông tin DB vào image (chỉ dùng demo)
ENV DB_URL jdbc:mysql://mysql-46dedf8-duy-e77d.b.aivencloud.com:22056/defaultdb?sslMode=REQUIRED
ENV DB_USER avnadmin
ENV DB_PASS AVNS_8MUOfbvalIaMvNntLGp

# App phải lắng nghe PORT do Render cấp
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar","--server.address=0.0.0.0","--server.port=${PORT}","--spring.datasource.url=jdbc:mysql://mysql-46dedf8-duy-e77d.b.aivencloud.com:22056/defaultdb?sslMode=REQUIRED","--spring.datasource.username=avnadmin","--spring.datasource.password=AVNS_8MUOfbvalIaMvNntLGp"]


