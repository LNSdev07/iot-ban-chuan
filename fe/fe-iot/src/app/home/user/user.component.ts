import { Component } from '@angular/core';
import { UserService } from './service/user.service';
import { NzMessageService } from 'ng-zorro-antd/message';
import { UserModel } from './model/user.model';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent {
 // param infor
 pageSize: number = 10;
 totalElement: number = 0;
 loading: boolean = true;
 pageIndex :number = 1;
 keyword: string ='';
 sortBy : string ='modifiedTime';
 direction: string ='DESC';
 status: any = null;
 role: any = null;
 beginDateParam : any = null;
 endDateParam: any = null;

 ttc: string ='Trạng thái';
 ttmb: string ='Role';

 selectedStatusMb: any;
 selectedStatusBc: any;

 isVisible = false;
 modalName = "";

 listOfOption: string[] = ['SUPER_ADMIN', 'ADMIN', 'USER'];
 listOfSelectedValue = [];

 radioValue = false;
 username = '';
 password = '';
 name =''

 userModel: UserModel ={
   username: '',
   password: '',
   name : '',
   role: [],
   status: 1
 }



 date = new Date();
 start = new Date();
 end = new Date();
 listOfData: { stt: number, tenCamBien: string, giaTriNhoNhat: number, giaTriLonNhat: number, thietLap: string }[] = [];
 dataTable: { id: number, name: string, username: string, mail: string, phone: string, status: number, createdDate: number, modifiedDate: number, role: string }[] = [];
 dateRange : [Date, Date];
typeAction= 1;
 
 constructor(private userService: UserService,
  private message: NzMessageService) {
   this.end.setDate(this.end.getDate() - 7);
   this.dateRange = [this.end, this.start];
  
 }
 ngOnInit() {
    this.getData();
 }

 getData(){
   this.userService.findUser(this.pageIndex, this.pageSize, this.sortBy, this.direction, this.beginDateParam, this.endDateParam, this.keyword, this.status, this.role).subscribe(res =>{
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

 toggleRadio() {
  console.log('thay doi trang thai radio')
  if(this.radioValue === true){
    this.radioValue = false;
    console.log('this.radioValue ---> '+ this.radioValue)
  }
  else {
    this.radioValue = true;
    console.log('this.radioValue ---> '+ this.radioValue)
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
     this.status = event;
   }
   else if(tt==0){
     this.role = event;
   }
   else{
     this.status = null;
     this.role = null;
   }
   console.log("call event change status")
   this.getData();
 }

 onInputChange(event: any): void {
  // Xử lý giá trị đầu vào ở đây, ví dụ:
  this.keyword = event.target.value;
  console.log("keyword = "+ this.keyword)
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


   showModal(name: any, username: string): void {
    this.modalName = ''
    this.modalName = name;
    this.isVisible = true;
    if(username =='') {
      this.typeAction =1;
      this.resetForm();
    }
    else {
      this.typeAction =0;
      console.log('username = '+ username)
      this.getDetailUser(username);
    }
  }

   closeModal(): void {
    this.isVisible = false;
  }

  handleOk(typeAction : number){
    if(typeAction ==1){
      // them mowi
      console.log('them moi')
      // this.isVisible = false;
    const checkValid = true;
    this.validateFormAddUser(this.username, this.name, this.password, checkValid, false);
    if(checkValid){
      this.userModel.password = this.password;
      this.userModel.username = this.username;
      this.userModel.name = this.name;
      this.userModel.role = this.getRole();
      this.userModel.status = this.radioValue==true?1:0;

      // thuec hien guiw form len server
      console.log("form -> " + JSON.stringify(this.userModel))

      this.userService.createUser(this.userModel).subscribe(
        // console.log("res = "+ res);

        (res: any) => {
          if(res.status.code == "201"){
            this.message.create("error", res.status.message);
          }
          else{
            console.log('Success response:', res);
          this.message.create("success", `Tạo mới người dùng thành công`);
          this.isVisible = false;
          this.getData();
          }
        },
        (error: any) => {
          console.log('Error response:', error);
          this.message.create("error", error.error.status.message);
        }
      )
      
    }
    }
    else{
      // chinh sua
      console.log('chinh sua')
      const check = true;
      const update = true;
      this.validateFormAddUser(this.username, this.name, this.password, check, update);
     if(check){
      this.userModel.password = this.password;
      this.userModel.username = this.username;
      this.userModel.name = this.name;
      this.userModel.role = this.getRole();
      this.userModel.status = this.radioValue==true?1:0;

      this.userService.updateUser(this.userModel).subscribe(
        // console.log("res = "+ res);

        (res: any) => {
          if(res.status.code == "201"){
            this.message.create("error", res.status.message);
          }
          else{
          console.log('Success response:', res);
          this.message.create("success", `Cập nhật thông tin thành công`);
          this.isVisible = false;
          this.getData();
          }
        },
        (error: any) => {
          console.log('Error response:', error);
          this.message.create("error", error.error.status.message);
        }
      )
    }
    
    }
  }

  getRole() : any {
     const role =[];
     for (const item of this.listOfSelectedValue) {
          role.push(this.mapRoleFromStringToInt(item))
     }
     return role;
  }

  mapRoleFromStringToInt(role: string): any{
     if(role === 'SUPER_ADMIN') return 2;
     else if(role === 'ADMIN') return 1;
     else if(role === 'USER') return 0;
  }

  validateFormAddUser(username: string, name: string, pass: string, checkValid: boolean,  update: boolean){
    console.log("name = "+ name)
     if(username == '' || name === ''){
      this.message.create("error", `username hoặc name không được rỗng`);
      checkValid = false;
     }
     else if(pass.length < 6 && !update){
      this.message.create("error", `password phải dài hơn 6 kí tự`);
      checkValid = false;
     }
  }

  disableUsername = false;


  getDetailUser(username: string){
    this.userService.getDetailUser(username).subscribe(res =>{
      this.disableUsername = true;
      console.log("res = "+ JSON.stringify(res));
      this.username = res.username;
      this.name = res.name;
      this.radioValue = res.status==1?true:false;
      let rolesArray = res.role.split(', ');
      this.listOfSelectedValue = rolesArray;
    })
  }

  resetForm(){
    this.username = '';
    this.name = '';
    this.radioValue = false;
    this.listOfSelectedValue = [];
  }
}
