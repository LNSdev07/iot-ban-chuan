import { Component, numberAttribute } from '@angular/core';
import { DataServiceService } from './service/data-service.service';
@Component({
  selector: 'app-data',
  templateUrl: './data.component.html',
  styleUrls: ['./data.component.css']
})
export class DataComponent {
  // param infor
  pageSize: number = 10;
  totalElement: number = 0;
  loading: boolean = true;
  pageIndex :number = 1;
  keyword: string ='';
  sortBy : string ='time';
  direction: string ='DESC';
  hornStatus: any = null;
  waterStatus: any = null;
  beginDateParam : any = null;
  endDateParam: any = null;

  ttc: string ='Trạng thái còi';
  ttmb: string ='Trạng thái đèn led';

  selectedStatusMb: any;
  selectedStatusBc: any;



  date = new Date();
  start = new Date();
  end = new Date();
  listOfData: { stt: number, tenCamBien: string, giaTriNhoNhat: number, giaTriLonNhat: number, thietLap: string }[] = [];
  dataTable: { id: number, temperature: number, flameSensor: number, humidity: number, gasSensor: number, buzzerState: number, ledState: number, time: number }[] = [];
  dateRange : [Date, Date];
  
  constructor(private service: DataServiceService) {
    this.end.setDate(this.end.getDate() + 7);
    this.dateRange = [this.start, this.end];

  }
  ngOnInit() {
     this.getData();
  }

  getData(){
    this.service.findAllData(this.pageIndex, this.pageSize, this.sortBy, this.direction, this.beginDateParam, this.endDateParam, this.waterStatus, this.hornStatus).subscribe(res =>{
      this.loading = false;
      this.dataTable = res.data.data;
      this.totalElement = res.data.totalElements;
      console.log('total = ', this.totalElement)
      console.log(this.dataTable)
    })
  }


  sortOrder: 'ascend' | 'descend' | null = null;

  sort(key: string, event: string | null) {
    this.sortBy = key;
    if (event === 'descend') {
      this.direction = 'DESC'; // Gán giá trị sắp xếp cho biến direction
      this.getData();
    }
    else if(event === 'ascend'){
      this.direction = 'ASC';
      this.getData();
    }
    else if(event === null){
      this.direction = 'DESC'; // Gán giá trị sắp xếp cho biến direction
      this.sortBy ='time';
      this.getData();
    }
  
  }
  



  searchData(reset: boolean = false): void {
    console.log("vao day")
    console.log('pageIndex = '+ this.pageIndex )
    console.log('pageSize = '+ this.pageSize)
    this.getData();
  }


  changeStatus(event: Event, tt: number){
    if(tt == 1){
      this.hornStatus = event;
    }
    else if(tt==0){
      this.waterStatus = event;
    }
    else{
      this.hornStatus = null;
      this.waterStatus = null;
    }
    this.getData();
  }



  handleDateRangeChange(newDateRange: any): void {
    console.log('Giá trị mới của dateRange:', newDateRange);
    // Thực hiện các xử lý tùy ý với giá trị mới ở đây
     if(newDateRange.length >0){
      this.beginDateParam = new Date(newDateRange[0]).getTime();
      this.endDateParam = new Date(newDateRange[1]).getTime();
      console.log('beginDateParam = '+ this.beginDateParam)
      console.log('endDateParam = '+ this.endDateParam)
      this.getData();
     }
     else{
      this.beginDateParam =null;
      this.endDateParam =null;
      this.getData();
     }
  }
  
  
  



}