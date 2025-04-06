#include "esp_camera.h"
#include <WiFi.h>
#include <WiFiUdp.h>

// WiFi配置
const char* ssid = "CMCC-wkbc";
const char* password = "tn96zfmv";

// 服务器配置 "192.168.1.5" "123.60.128.58"
const char* serverIP = "192.168.1.6";
const int serverPort = 8000;

WiFiUDP udp;

// 摄像头引脚配置（根据ESP32-CAM模块调整）
#define PWDN_GPIO_NUM     32
#define RESET_GPIO_NUM    -1
#define XCLK_GPIO_NUM      0
#define SIOD_GPIO_NUM     26
#define SIOC_GPIO_NUM     27
#define Y9_GPIO_NUM       35
#define Y8_GPIO_NUM       34
#define Y7_GPIO_NUM       39
#define Y6_GPIO_NUM       36
#define Y5_GPIO_NUM       21
#define Y4_GPIO_NUM       19
#define Y3_GPIO_NUM       18
#define Y2_GPIO_NUM        5
#define VSYNC_GPIO_NUM    25
#define HREF_GPIO_NUM     23
#define PCLK_GPIO_NUM     22

void setup() {
  Serial.begin(115200);

  // 初始化WiFi
  WiFi.begin(ssid, password);
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.println("\nWiFi connected");

  // 摄像头配置
  camera_config_t config;
  config.ledc_channel = LEDC_CHANNEL_0;
  config.ledc_timer = LEDC_TIMER_0;
  config.pin_d0 = Y2_GPIO_NUM;
  config.pin_d1 = Y3_GPIO_NUM;
  config.pin_d2 = Y4_GPIO_NUM;
  config.pin_d3 = Y5_GPIO_NUM;
  config.pin_d4 = Y6_GPIO_NUM;
  config.pin_d5 = Y7_GPIO_NUM;
  config.pin_d6 = Y8_GPIO_NUM;
  config.pin_d7 = Y9_GPIO_NUM;
  config.pin_xclk = XCLK_GPIO_NUM;
  config.pin_pclk = PCLK_GPIO_NUM;
  config.pin_vsync = VSYNC_GPIO_NUM;
  config.pin_href = HREF_GPIO_NUM;
  config.pin_sscb_sda = SIOD_GPIO_NUM;
  config.pin_sscb_scl = SIOC_GPIO_NUM;
  config.pin_pwdn = PWDN_GPIO_NUM;
  config.pin_reset = RESET_GPIO_NUM;
  config.xclk_freq_hz = 20000000;
  config.pixel_format = PIXFORMAT_JPEG;
  config.frame_size = FRAMESIZE_HVGA; // FRAMESIZE_QQVGA 160x120    FRAMESIZE_QVGA 320x240
  config.jpeg_quality = 12;            // 0-63
  config.fb_count = 1;
//  <option value="13">UXGA(1600x1200)</option>
//                                <option value="12">SXGA(1280x1024)</option>
//                                <option value="11">HD(1280x720)</option>
//                                <option value="10">XGA(1024x768)</option>
//                                <option value="9">SVGA(800x600)</option>
//                                <option value="8">VGA(640x480)</option>
//                                <option value="7">HVGA(480x320)</option>
//                                <option value="6">CIF(400x296)</option>
//                                <option value="5">QVGA(320x240)</option>
//                                <option value="4">240x240</option>
//                                <option value="3">HQVGA(240x176)</option>
//                                <option value="2">QCIF(176x144)</option>
//                                <option value="1">QQVGA(160x120)</option>
//                                <option value="0">96x96</option>
  // 初始化摄像头"123.60.128.58"
  esp_err_t err = esp_camera_init(&config);
  if (err != ESP_OK) {
    Serial.printf("Camera init failed with error 0x%x", err);
    return;
  }
}

void loop() {
  delay(300); // 控制帧率
  // 捕获帧
  camera_fb_t *fb = esp_camera_fb_get();
  if (!fb) {
    Serial.println("Camera capture failed");
    return;
  }

  // 通过UDP发送帧数据
  sendFrame(fb->buf, fb->len);

  // 释放帧缓冲区
  esp_camera_fb_return(fb);

}

void sendFrame(uint8_t *data, size_t len) {
  udp.beginPacket(serverIP, serverPort);
  udp.write(data, len);
  udp.endPacket();
}