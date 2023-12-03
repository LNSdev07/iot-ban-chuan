import { Component, ViewChild } from '@angular/core';
import { ChartConfiguration, ChartOptions, ChartType } from "chart.js";
import { ReportService } from './service/report.service';
import { BaseChartDirective } from 'ng2-charts';
import { Subject } from 'rxjs';
@Component({
  selector: 'app-report',
  templateUrl: './report.component.html',
  styleUrls: ['./report.component.css']
})
export class ReportComponent {
  @ViewChild(BaseChartDirective) chart: BaseChartDirective | undefined;

  title = 'ng2-charts-demo';
  humirity: any =0;
  temparatue: any =0;
  gas : any =0;
  flame: any ='';

  ledState: any =0;
  buzzerState: any =0;


  private dataSubject: Subject<any>;

  temperatureColors = [
    { minValue: 0, maxValue: 10, color: 'rgba(255, 0, 0, 0.1)' },
    { minValue: 11, maxValue: 20, color: 'rgba(255, 0, 0, 0.3)' },
    { minValue: 21, maxValue: 30, color: 'rgba(255, 0, 0, 0.5)' },
    { minValue: 31, maxValue: 40, color: 'rgba(255, 0, 0, 0.6)' },
    { minValue: 41, maxValue: 50, color: 'rgba(255, 0, 0, 0.7)' },
    { minValue: 51, maxValue: 60, color: 'rgba(255, 0, 0, 0.8)' },
    { minValue: 61, maxValue: 100, color: 'rgba(255, 0, 0, 0.9)' }
  ];
  
  humidityColors = [
    { minValue: 0, maxValue: 10, color: 'rgba(0, 0, 255, 0.1)' },
    { minValue: 11, maxValue: 20, color: 'rgba(0, 0, 255, 0.3)' },
    { minValue: 21, maxValue: 30, color: 'rgba(0, 0, 255, 0.5)' },
    { minValue: 31, maxValue: 40, color: 'rgba(0, 0, 255, 0.6)' },
    { minValue: 41, maxValue: 50, color: 'rgba(0, 0, 255, 0.7)' },
    { minValue: 51, maxValue: 60, color: 'rgba(0, 0, 255, 0.8)' },
    { minValue: 61, maxValue: 100, color: 'rgba(0, 0, 255, 0.9)' }
  ];
  
  gasColors = [
    { minValue: 0, maxValue: 10, color: 'rgba(255, 255, 0, 0.1)' },
    { minValue: 11, maxValue: 20, color: 'rgba(255, 255, 0, 0.3)' },
    { minValue: 21, maxValue: 30, color: 'rgba(255, 255, 0, 0.5)' },
    { minValue: 31, maxValue: 40, color: 'rgba(255, 255, 0, 0.6)' },
    { minValue: 41, maxValue: 50, color: 'rgba(255, 255, 0, 0.7)' },
    { minValue: 51, maxValue: 60, color: 'rgba(255, 255, 0, 0.8)' },
    { minValue: 61, maxValue: 10000, color: 'rgba(255, 255, 0, 0.9)' }
  ];
  

  constructor(private service: ReportService) {
    this.dataSubject = this.service.getDataSubject();
  }


  ngOnInit() {
    // Gọi randomize() ban đầu
    this.service.connect();
    this.dataSubject.subscribe((data) => {
    this.updateChartData(data);
  });
  }
 
  updateChartData(data: any) {
    console.log('data = '+ JSON.stringify(data))
    this.humirity = data.humidity;
    this.temparatue = data.temperature;
    this.gas = data.gasSensor;
    console.log("this.gas = "+ this.gas)
    this.ledState = data.ledState;
    this.buzzerState = data.buzzerState;
    if(data.flameSensor == 0){
      this.flame = 'Đã phát hiện lửa';
    }
    else{
      this.flame = 'Không có lửa';
    }

    this.changeColorBasedOnCondition();

    if(this.lineChartLabels.length <10){
      this.lineChartLabels.push(data.time);
    }
    else{
      this.lineChartLabels.shift();
    }
    
    if(this.lineChartData[0].data.length <10){
      this.lineChartData[0].data.push(data.temperature)
    }
    else{
      this.lineChartData[0].data.shift();
    }
    if(this.lineChartData[1].data.length <10){
      this.lineChartData[1].data.push(data.humidity)
    }
    else{
      this.lineChartData[1].data.shift();
    }
    if(this.lineChartData[2].data.length <10){
      this.lineChartData[2].data.push(data.gasSensor)
    }
    else{
      this.lineChartData[2].data.shift();
    }
    // this.randomize();
    this.chart?.update();
    // console.log("this.lineChartData = "+ JSON.stringify(this.lineChartData))
    }


  // lineChart
  public lineChartData:Array<any> = [
    {data: [], label: 'Temperature'},
    {data: [], label: 'Humidity'},
    {data: [], label: 'Gas'}
  ];
  public lineChartLabels:Array<any> = [];
  public lineChartOptions:any = {
    responsive: true,
    cubicInterpolationMode: 'monotone'
  };
  public lineChartLegend:boolean = true;
  public lineChartType:string = 'line';


  // Hàm để thay đổi màu dựa trên điều kiện
  changeColorBasedOnCondition() {
    this.changeColorTemp('temperatureCard', this.humirity);
    this.changeColorHum('humidityCard', this.temparatue);
    this.changeColorGas('gasCard', this.gas);
  }
 
  changeColorTemp(elementId: string, value: number) {
    const element = document.getElementById(elementId);
    if (element) {
      for (const mapping of this.temperatureColors) {
        if (value >= mapping.minValue && value <= mapping.maxValue) {
          element.style.backgroundColor = mapping.color;
          break;
        }
      }
    }
  }

  changeColorHum(elementId: string, value: number) {
    const element = document.getElementById(elementId);
    if (element) {
      for (const mapping of this.humidityColors) {
        if (value >= mapping.minValue && value <= mapping.maxValue) {
          element.style.backgroundColor = mapping.color;
          break;
        }
      }
    }
  }

  changeColorGas(elementId: string, value: number) {
    const element = document.getElementById(elementId);
    if (element) {
      for (const mapping of this.gasColors) {
        if (value >= mapping.minValue && value <= mapping.maxValue) {
          element.style.backgroundColor = mapping.color;
          break;
        }
      }
    }
  }

}
