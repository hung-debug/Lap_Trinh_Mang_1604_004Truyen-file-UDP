<h2 align="center">
    <a href="https://dainam.edu.vn/vi/khoa-cong-nghe-thong-tin">
    🎓 Faculty of Information Technology (DaiNam University)
    </a>
</h2>
<h2 align="center">
   NETWORK PROGRAMMING
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

## 📖 1. Giới thiệu

Học phần trang bị cho người học những kiến thức nền tảng của lập trình mạng và các kỹ năng cần thiết để thiết kế và cài đặt các ứng dụng mạng và các chuẩn ở mức ứng dụng dựa trên mô hình Client/Server, có sử dụng các giao tiếp chương trình dựa trên Sockets. Kết thúc học phần, sinh viên có thể viết các chương trình ứng dụng mạng với giao thức tầng ứng dụng tự thiết kế.

## 📂 2. UDP file transfer dashboard (java swing)

Ứng dụng **gửi và nhận file qua giao thức udp** với giao diện đồ họa trực quan được xây dựng bằng **java swing**.  
dự án mô phỏng quá trình **truyền tải file giữa hai máy tính trong mạng lan** theo cơ chế **sender ↔ receiver**, đồng thời hiển thị trạng thái kết nối trực quan bằng chấm màu 🔴🟢.

---

## 3. ✨ Tính năng nổi bật

### 🔹 Sender (máy gửi)
- Nhập **ip** và **port** của receiver.
- **kết nối** tới receiver với nút `connect`.  
  > nếu kết nối thành công, trạng thái đổi từ 🔴 sang 🟢.
- Chọn file cần gửi (`choose file`).
- Gửi file sang receiver (`send file`).
- Hiển thị log chi tiết quá trình gửi.

### 🔹 Receiver (máy nhận)
- Nhập **port** để mở cổng lắng nghe.
- Bắt đầu nhận dữ liệu với `start receiver`.
- Xác nhận kết nối từ sender và chuyển trạng thái sang 🟢.
- Nhận file:
  - Tiếp nhận thông tin tên file (`filename:...`).
  - Ghép nối dữ liệu từ các gói udp.
  - Khi nhận `"eof"` → mở hộp thoại cho phép chọn nơi lưu file.
- Hiển thị log quá trình nhận file.

### 🔹 trạng thái kết nối
- 🔴 **đỏ** → chưa kết nối hoặc đã ngắt kết nối.  
- 🟢 **xanh** → kết nối thành công / đang nhận dữ liệu.  

---

## 🖼️ 4. Giao diện mô phỏng

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







