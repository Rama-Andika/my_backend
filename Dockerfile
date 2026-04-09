# 1. Gunakan sistem operasi Linux super kecil yang sudah terinstal Java 8
FROM eclipse-temurin:8-jre-alpine

# 2. Buat folder bernama /app di dalam container
WORKDIR /app

# 3. Copy file .jar dari laptop Anda ke dalam folder /app di container
# (Pastikan nama file .jar di bawah ini sesuai dengan nama file Anda di dalam folder target/)
COPY target/akidsmaginepg-backend.jar app.jar

# 4. Beri tahu Docker bahwa aplikasi ini akan menggunakan port 9091
EXPOSE 9091

# 5. Perintah yang dijalankan saat container pertama kali dihidupkan
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=prod"]