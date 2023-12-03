import { Injectable } from '@angular/core';
import { Client } from '@stomp/stompjs';
import { Subject } from 'rxjs';
import * as SockJS from 'sockjs-client';
import { NzMessageService } from 'ng-zorro-antd/message';

@Injectable({
  providedIn: 'root'
})
export class ReportService {
  webSocketEndPoint: string = 'http://localhost:8080/api/iot/v1/data-socket';
  topic: string = '/topic/data-receive';
  stompClient: Client = new Client;
  private dataSubject = new Subject<any>();
  isWebSocketConnected: boolean = false;

  constructor(private message: NzMessageService) {
    // this.isWebSocketConnected = false;
    try {
      this.stompClient = new Client({
        webSocketFactory: () => new SockJS(this.webSocketEndPoint)
      });
      this.stompClient.onConnect = (frame) => {
        console.log('farme = '+ frame)
        // this.onConnect(frame);
        if (frame && frame.headers && frame.headers['message'] === 'Connection error') {
          this.onConnectFailure(frame.headers['message']);
        } else {
          this.onConnect(frame);
        }
      };
      this.stompClient.onDisconnect = (frame) => {
        this.onDisconnect(frame);
      };
      this.stompClient.onWebSocketError = (error) => {
        this.onWebSocketError(error);
      };
    } catch (error) {
      console.log('kết nối websocket thất bại')
      this.message.create("error", `Kết nối Websocket thất bại`);
    }
  }
  connect() {
    // console.log('Initialize WebSocket Connection');
    // console.log('aaaaaaaaa')
    // // this.stompClient.subscribe('/topic/data-receive', )
    // this.stompClient.activate();
    if (!this.isWebSocketConnected) {
      try {
        this.stompClient.activate();
        this.isWebSocketConnected = true;
        // this.message.create("success", `Kết nối Websocket thành công`);
        this.message.create("success", `Kết nối Websocket thành công`);
      } catch (error) {
        console.log('Kết nối WebSocket thất bại', error);
        this.message.create("error", `Kết nối Websocket thất bại: ${error}`);
      }
    } else {
      console.log('WebSocket is already connected. No need to reconnect.');
    }
  }

  onWebSocketError(error: any) {
    console.log('Lỗi khi thiết lập kết nối WebSocket', error);
    this.message.create("error", `Lỗi khi thiết lập kết nối Websocket: ${error}`);
  }

  onConnectFailure(error: any) {
    console.log('Kết nối WebSocket thất bại', error);
    this.message.create("error", `Kết nối Websocket thất bại: ${error}`);
  }

  disconnect() {
    if (this.stompClient.connected) {
      this.stompClient.deactivate();
      console.log('Disconnected');
    }
  }

  onConnect(frame: any) {
    console.log('Connected');
    this.stompClient.subscribe(this.topic, (sdkEvent) => {
      this.onMessageReceived(sdkEvent);
    });
  }

  getDataSubject(): Subject<any> {
    return this.dataSubject;
  }

  onDisconnect(frame: any) {
    console.log('Disconnected');
    setTimeout(() => {
      this.connect();
    }, 5000);
  }

  /**
   * Send message to server via web socket
   * @param {*} message
   */
  send(message: any) {
    console.log('Calling API via web socket');
    this.stompClient.publish({ destination: '/app/hello', body: JSON.stringify(message) });
  }

  onMessageReceived(message: any) {
    try {
      // Lấy nội dung JSON từ chuỗi binaryBody và chuyển đổi thành đối tượng JSON
      const binaryBody = message._binaryBody;
      const jsonString = String.fromCharCode.apply(null, Object.values(binaryBody));
      const data = JSON.parse(jsonString);
      // In ra dữ liệu từ thông điệp JSON
      // console.log('Data Received from Server:', data);
      // Đẩy dữ liệu mới vào Subject
      this.dataSubject.next(data);
      
      // Bây giờ bạn có thể truy cập các thuộc tính bên trong đối tượng data, ví dụ: data.command, data.headers, vv.
  } catch (error) {
      console.error('Failed to parse message:', error);
  }
  }
}
