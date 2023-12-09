import { Component } from '@angular/core';
import { NzMessageService } from 'ng-zorro-antd/message';
import { ReportService } from '../report/service/report.service';
import { Subject } from 'rxjs';
import { EventControlReq } from './model/eventdevice';
import { ActionService } from './service/action.service';
import { ConfigAlertDeviceReq } from './model/configdevice';
import { ConfigAutoAlertReq } from './model/configauto';
// import { AudioRecordingService } from "./audio-recording.service";
import { DomSanitizer } from "@angular/platform-browser";
import { AudioRecordingService } from './service/audio-recording.service';
import { HttpClient, HttpResponse } from '@angular/common/http';
@Component({
  selector: 'app-action',
  templateUrl: './action.component.html',
  styleUrls: ['./action.component.css']
})
export class ActionComponent {

  isVisible = false;
  modalName: string ='';
  min =0;
  max =0;
  switchValue = false;
  chooseSpeechToText = false;

  disableButtonAction = false;

  buzzerStatus = 0;
  ledStatus = 0;

  configAutoAlertReq: ConfigAutoAlertReq = {
    alert: '',
    value: false
  }
  
  eventDeviceReq: EventControlReq ={
    deviceAlert: "",
    status: false
  };

  configAlertDeviceReq: ConfigAlertDeviceReq ={
    deviceType: '',
    min: 0,
    max: 0
  }
 

  constructor(private message: NzMessageService,
              private actionService: ActionService,
              private reportService: ReportService,
              private http: HttpClient,
              private audioRecordingService: AudioRecordingService,
              private sanitizer: DomSanitizer) {
              this.dataSubject = this.reportService.getDataSubject();

              this.audioRecordingService
              .recordingFailed()
              .subscribe(() => (this.isRecording = false));
            this.audioRecordingService
              .getRecordedTime()
              .subscribe(time => (this.recordedTime = time));
            this.audioRecordingService.getRecordedBlob().subscribe(data => {
              this.teste = data;
              this.blobUrl = this.sanitizer.bypassSecurityTrustUrl(
                URL.createObjectURL(data.blob)
              );
            });
          
              }

  showModal(name: any, min: any, max: any): void {
    this.modalName = 'Thiết lập '
    this.isVisible = true;
    this.modalName += name;
    this.min = min;
    this.max = max;
  }

  closeModal(): void {
    this.isVisible = false;
  }

  handleOk(){
    this.isVisible = false;
    console.log(this.min)
    console.log(this.max)
    this.showInfo()
    console.log(this.modalName)
    if(this.modalName.includes('Nhiệt Độ')){
         this.configAlertDeviceReq.deviceType = 'TEMPERATURE';
    }
    else if(this.modalName.includes('Độ ẩm')){
        this.configAlertDeviceReq.deviceType = 'HUMIDITY';
    }
    else if(this.modalName.includes('Gas')){
       this.configAlertDeviceReq.deviceType = 'GAS';
    }
    this.configAlertDeviceReq.min = this.min;
    this.configAlertDeviceReq.max = this.max;
    console.log("this.configAlertDeviceReq = "+ this.configAlertDeviceReq.deviceType)
    this.actionService.configDevice(this.configAlertDeviceReq).subscribe(res =>{
      console.log(res)
      if(res === 'OK'){
        this.message.create('success', "Cập nhật thành công");
        this.getData();
      }
      else{
        this.message.create('error', "Cập nhật thất bại");
      }
    })

  }

  messageInfo: string = 'Các giá trị phải thuộc [ 0, 100 ] và min <= max';

  showInfo(){
    if(this.min <0 || this.min > 100){
      this.message.create('error', this.messageInfo);
    }
    else if(this.max <0 || this.max >2000){
      this.message.create('error', this.messageInfo);
    }
    else if(this.max < this.min){
      this.message.create('error', this.messageInfo);
    }
    return;
  }


  listOfData: { stt: number, deviceType: string, min: number, max: number, updatedTime: Date }[] = [];


  private dataSubject!: Subject<any>;

  ngOnInit() {
   this.getData();
    // this.reportService.connect();
    // this.dataSubject.subscribe((data) => {
    //   console.log(JSON.stringify(data))
    //   this.hornStatus = data.hornStatus;
    //   this.waterStatus = data.waterStatus;
    // });

  

  }

  getData(){
    this.actionService.findAllDevice().subscribe(res =>{
      console.log(res.data)
      this.listOfData = res.data;
      
    })
  }



  changeStatus(type: any, status: number){
      this.eventDeviceReq.deviceAlert = type;
      this.eventDeviceReq.status = status==1?true:false;
      console.log(this.eventDeviceReq)
      return this.actionService.changeStatusDevice(this.eventDeviceReq).subscribe(res =>{
        if(res === 'OK'){
          this.message.create('success', "Cập nhật thành công");
          console.log("type = "+ type)
          console.log("status = "+ status)
          this.reverseStatus(type, status);
        }
        else{
          this.message.create('error', "Cập nhật thất bại");
        }
      });
  }

  reverseStatus(type: any, status: number){
     if(type == 'BUZZER'){
       if(status ==0) this.buzzerStatus =1;
       else this.buzzerStatus =0;
     }
     else if(type == 'LED'){
      if(status == 0) this.ledStatus =1;
      else this.ledStatus =0;
     }
     console.log("this.buzzerStatus = "+ this.buzzerStatus)
  }

  changeAuto() {
    console.log(this.switchValue)
    this.configAutoAlertReq.alert ="AUTOMATIC";
    this.configAutoAlertReq.value = this.switchValue;
    return this.actionService.configAuto(this.configAutoAlertReq).subscribe(res =>{
      if(res === 'OK'){
        this.message.create('success', "Cập nhật thành công");
        this.disableButtonAction = this.switchValue;
      }
      else{
        this.message.create('error', "Cập nhật thất bại");
      }
    });
  }

  changeSpeechToText(){
    console.log(this.chooseSpeechToText)
  }


  isRecording = false;
  recordedTime: any;
  blobUrl: any;
  teste: any;


  startRecording() {
    if (!this.isRecording) {
      this.isRecording = true;
      this.audioRecordingService.startRecording();
    }
  }

  abortRecording() {
    if (this.isRecording) {
      this.isRecording = false;
      this.audioRecordingService.abortRecording();
    }
  }

  stopRecording() {
    if (this.isRecording) {
      this.audioRecordingService.stopRecording();
      this.isRecording = false;
    }
  }

  clearRecordedData() {
    this.blobUrl = null;
  }

  ngOnDestroy(): void {
    this.abortRecording();
  }

  download(): void {
    // const url = window.URL.createObjectURL(this.teste.blob);
    // const link = document.createElement("a");
    // link.href = url;
    // link.download = this.teste.title;
    // link.click();

    if (this.teste && this.teste.blob) {
      const formData = new FormData();
      formData.append('file', this.teste.blob, this.teste.title);
      this.audioRecordingService.uploadFile(formData)
        .subscribe(
          (response: HttpResponse<string>) => {
            console.log('File uploaded successfully:', response.body);
            const mes = response.body==null?"":response.body;
            this.message.create('success', mes);
            // Handle success as needed
          },
          (error) => {
            console.error('File upload failed:', error);
            this.message.create('error', 'Không thể thực hiện chức năng này');
            // Handle error as needed
          }
        );
    }
  }

  
  

}