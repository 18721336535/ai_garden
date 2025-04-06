#include <WiFi.h>
#include <PubSubClient.h>
//#include <DHT.h>

// WiFi配置
const char* ssid = "CMCC-wkbc";
const char* password = "tn96zfmv";


// MQTT 服务器配置
const char* mqtt_server = "192.168.1.7";
const int mqtt_port = 1883;
const char* mqtt_user = "melon";
const char* mqtt_password = "password2";
// MQTT 主题配置
const char* machineControlTopic = "machine/control";
const char* soilMoistureTopic = "melon/soilMoisture";
const char* lightTenseTopic = "melon/lightTense";

// 继电器引脚配置
//const int relayPin = 2;

// DHT11 传感器配置
const int dhtPin = 4;

// 定义土壤湿度传感器连接的模拟引脚
const int soilMoisturePin = 34;  // 假设传感器连接到GPIO 34
// 定义光敏传感器连接的模拟引脚
const int lightSensorPin = 35;  // 假设传感器连接到GPIO 34

// 定义L9110控制引脚
#define MOTOR_IA 16  // A通道输入A
#define MOTOR_IB 17  // A通道输入B

//DHT dht(dhtPin, DHT11);

// WiFi 和 MQTT 客户端实例
WiFiClient espClient;
PubSubClient client(espClient);

// 连接到 WiFi 网络
void setup_wifi() {
  delay(10);
  Serial.println();
  Serial.print("Connecting to ");
  Serial.println(ssid);

  WiFi.begin(ssid, password);

  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }

  Serial.println("");
  Serial.println("WiFi connected");
  Serial.println("IP address: ");
  Serial.println(WiFi.localIP());
}

// MQTT 消息回调函数
void callback(char* topic, byte* payload, unsigned int length) {
  Serial.print("Message topic arrived [");
  Serial.print(topic);
  Serial.print("] ");
  String message = "";
  for (int i = 0; i < length; i++) {
    message += (char)payload[i];
  }
   Serial.print("Message message arrived :");
  Serial.println(message);

  // 根据消息内容控制继电器
  if (String(topic) == machineControlTopic) {
    if (message == "OFF") {
      //digitalWrite(relayPin, LOW); // 关闭继电器
      motor_stop();
      Serial.println("machine OFF");
    } else if (message == "ON") {
      //digitalWrite(relayPin, HIGH); // 打开继电器
      motor_forward();
      delay(30000);
      Serial.println("machine ON");
      motor_stop();
    }
  }
}

// 连接到 MQTT 服务器
void reconnect() {
  while (!client.connected()) {
    Serial.print("Attempting MQTT connection...");
    if (client.connect("mqttx_b6989f91", mqtt_user, mqtt_password)) {
      Serial.println("connected");
      // 订阅控制继电器的主题
      client.subscribe(machineControlTopic);
    } else {
      Serial.print("failed, rc=");
      Serial.print(client.state());
      Serial.println(" try again in 5 seconds");
      delay(5000);
    }
  }
}

// 正转函数
void motor_forward() {
  digitalWrite(MOTOR_IA, HIGH);
  digitalWrite(MOTOR_IB, LOW);

}
// 停止函数
void motor_stop() {
  digitalWrite(MOTOR_IA, LOW);
  digitalWrite(MOTOR_IB, LOW);
}

void setup() {
  Serial.begin(115200);
  setup_wifi();
  client.setServer(mqtt_server, mqtt_port);
  client.setCallback(callback);

  // 初始化继电器引脚
  //pinMode(relayPin, OUTPUT);
  //digitalWrite(relayPin, LOW); // 初始状态关闭继电器

  // 初始化电机控制引脚为输出模式
  pinMode(MOTOR_IA, OUTPUT);
  pinMode(MOTOR_IB, OUTPUT);
  // 初始状态停止电机
  digitalWrite(MOTOR_IA, LOW);
  digitalWrite(MOTOR_IB, LOW);


  // 初始化 DHT11 传感器
  //dht.begin();
}

void loop() {
  if (!client.connected()) {
    reconnect();
  }
  client.loop();

  // 1.采集 DHT11 传感器数据
  //float humidity = dht.readHumidity();
  //float temperature = dht.readTemperature();

//  float humidity = 100.0;
//  float temperature = 18.0;
  // 检查读取是否成功
//  if (isnan(humidity) || isnan(temperature)) {
//    Serial.println("Failed to read from DHT sensor!");
//    return;
//  }

  // 2. 读取土壤湿度传感器的模拟值
  int moistureSensorValue = analogRead(soilMoisturePin);
  // 将读取的模拟值转换为电压值（可选）
  float moistureVoltage = moistureSensorValue * (3.3 / 4095.0);  // ESP32的ADC分辨率为12位，最大值为4095
  // 输出原始模拟值和电压值到串口监视器
  Serial.print("soilMoisture Sensor Value: ");
  Serial.print(moistureSensorValue);
  Serial.print(" | soilMoistureVoltage: ");
  Serial.println(moistureVoltage);

  //3. 读取光敏传感器的模拟值
  int lightSensorValue = (4095.0 - analogRead(lightSensorPin));
  // 将读取的模拟值转换为电压值（可选）
  float lightVoltage = lightSensorValue * (3.3 / 4095.0);  // ESP32的ADC分辨率为12位，最大值为4095
  // 输出原始模拟值和电压值到串口监视器
  Serial.print("lightSensor Sensor Value: ");
  Serial.print(lightSensorValue);
  Serial.print(" | lightSensor Voltage: ");
  Serial.println(lightVoltage);

   //发送传感器数据到 MQTT broker
  String moistureData = "soilMoisture:" + String(moistureVoltage);
  client.publish(soilMoistureTopic, moistureData.c_str());
  String lightData = "lighttense:" + String(lightVoltage);
  client.publish(lightTenseTopic, lightData.c_str());
  Serial.println("Sensor data sent: " + lightData);

  // 每 10 秒发送一次数据
  delay(10000);
}