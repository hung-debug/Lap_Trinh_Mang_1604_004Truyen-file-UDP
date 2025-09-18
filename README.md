<h2 align="center">
    <a href="https://dainam.edu.vn/vi/khoa-cong-nghe-thong-tin">
    🎓 Faculty of Information Technology (DaiNam University)
    </a>
</h2>
<h2 align="center">
   LẬP TRÌNH MẠNG 
</h2>
<div align="center">
    <p align="center">
        <img src="docs/aiotlab_logo.png" alt="AIoTLab Logo" width="170"/>
        <img src="docs/fitdnu_logo.png" alt="AIoTLab Logo" width="180"/>
        <img src="docs/dnu_logo.png" alt="DaiNam University Logo" width="200"/>
    </p>

[![AIoTLab](https://img.shields.io/badge/AIoTLab-green?style=for-the-badge)](https://www.facebook.com/DNUAIoTLab)
[![Faculty of Information Technology](https://img.shields.io/badge/Faculty%20of%20Information%20Technology-blue?style=for-the-badge)](https://dainam.edu.vn/vi/khoa-cong-nghe-thong-tin)
[![DaiNam University](https://img.shields.io/badge/DaiNam%20University-orange?style=for-the-badge)](https://dainam.edu.vn)


</div>

## GỬI FILE BẰNG GIAO THỨC UDP
 
## 📖 1. Giới thiệu
Học phần trang bị cho người học những kiến thức nền tảng của lập trình mạng và các kỹ năng cần thiết để thiết kế và cài đặt các ứng dụng mạng và các chuẩn ở mức ứng dụng dựa trên mô hình Client/Server, có sử dụng các giao tiếp chương trình dựa trên Sockets. Kết thúc học phần, sinh viên có thể viết các chương trình ứng dụng mạng với giao thức tầng ứng dụng tự thiết kế.

## 📂 2. UDP file transfer dashboard (Java Swing)
Ứng dụng gửi và nhận file qua giao thức UDP với giao diện đồ họa trực quan được xây dựng bằng Java Swing.
Dự án mô phỏng quá trình truyền tải file giữa hai máy tính trong mạng LAN theo cơ chế Sender ↔ Receiver, đồng thời hiển thị trạng thái kết nối trực quan bằng chấm màu 🔴🟢.

## ✨ 3. Tính năng nổi bật
🔹 Sender (máy gửi)

Nhập IP và port của receiver.

Kết nối tới receiver với nút Connect.

Nếu kết nối thành công, trạng thái đổi từ 🔴 sang 🟢.

Chọn file cần gửi (Choose file).

Gửi file sang receiver (Send file).

Hiển thị log chi tiết quá trình gửi.

🔹 Receiver (máy nhận)

Nhập port để mở cổng lắng nghe.

Bắt đầu nhận dữ liệu với Start Receiver.

Xác nhận kết nối từ sender và chuyển trạng thái sang 🟢.

Nhận file:

Tiếp nhận thông tin tên file (FILENAME:...).

Ghép nối dữ liệu từ các gói UDP.

Khi nhận "EOF" → mở hộp thoại cho phép chọn nơi lưu file.

Hiển thị log quá trình nhận file.

🔹 Trạng thái kết nối

🔴 đỏ → chưa kết nối hoặc đã ngắt kết nối.

🟢 xanh → kết nối thành công / đang nhận dữ liệu.

## ⚙️ 4. Công nghệ sử dụng

[![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.oracle.com/java/technologies/javase-downloads.html) 
[![Swing](https://img.shields.io/badge/Java%20Swing-007396?style=for-the-badge&logo=java&logoColor=white)](https://docs.oracle.com/javase/tutorial/uiswing/) 
[![Nimbus](https://img.shields.io/badge/Nimbus%20Look&Feel-4B0082?style=for-the-badge&logo=java&logoColor=white)](https://docs.oracle.com/javase/tutorial/uiswing/lookandfeel/nimbus.html) 
[![UDP](https://img.shields.io/badge/UDP%20Socket-00599C?style=for-the-badge&logo=socket.io&logoColor=white)](https://docs.oracle.com/javase/tutorial/networking/datagrams/) 
[![HTTP](https://img.shields.io/badge/HTTP-FF6F00?style=for-the-badge&logo=mozilla&logoColor=white)](https://developer.mozilla.org/en-US/docs/Web/HTTP) 
[![NTP](https://img.shields.io/badge/NTP-228B22?style=for-the-badge&logo=internet-explorer&logoColor=white)](https://www.ntp.org/) 
[![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)](https://www.mysql.com/) 
[![JDBC](https://img.shields.io/badge/JDBC%20Connector-CC0000?style=for-the-badge&logo=java&logoColor=white)](https://dev.mysql.com/downloads/connector/j/) 
[![Eclipse](https://img.shields.io/badge/Eclipse-2C2255?style=for-the-badge&logo=eclipseide&logoColor=white)](https://www.eclipse.org/) 
[![NetBeans](https://img.shields.io/badge/NetBeans-1B6AC6?style=for-the-badge&logo=apachenetbeanside&logoColor=white)](https://netbeans.apache.org/) 

## 🖼️ 5. Giao diện mô phỏng

### sender
<div align="center">
    <p align="center">
        <img src="docs/Screenshot 2025-09-18 081940.png" alt="" width="270"/>
    </p>
<div>


### receiver

<div align="center">
    <p align="center">
        <img src="docs/Screenshot 2025-09-18 081932.png" alt="" width="270"/>
    </p>
<div>

## ⚙️ Cách sử dụng

### Biên dịch
```bash

javac senderfile/udpfilesenderdashboard.java
javac senderfile/udpfilereceiverdashboard.java













