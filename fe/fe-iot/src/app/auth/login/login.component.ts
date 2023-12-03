import { Component } from '@angular/core';
import { FormGroup, FormControl, Validators, NonNullableFormBuilder, FormBuilder } from '@angular/forms';
import { LoginService } from './service/login.service';
import { LoginReq } from './model/loginreq';
import { NzMessageService } from 'ng-zorro-antd/message';
import { HttpErrorResponse } from '@angular/common/http';
import { TokenService } from 'src/app/common/token.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {

  constructor(private fb: FormBuilder,
              private loginService: LoginService,
              private message: NzMessageService,
              private tokenService : TokenService,
              private router: Router) {}

  loginReq: LoginReq ={
    username: '',
    password: ''
  }        

  validateForm: FormGroup = this.fb.group({
    userName: ['', [Validators.required]],
    password: ['', [Validators.required]],
    remember: [true]
  });

  submitForm(): void {
    if (this.validateForm.valid) {
      console.log('submit', this.validateForm.value);
      this.loginReq.username = this.validateForm.value.userName;
      this.loginReq.password = this.validateForm.value.password;
      this.loginService.login(this.loginReq).subscribe(
        (res: any) => {
          console.log('Success response:', res);
          this.message.create("success", `Đăng nhập thành công`);
          // lưu token vào localStorage
          const token = res.token;
          const role = res.role;
          // console.log('token = '+ token)
          this.tokenService.setToken(token);
          this.tokenService.setRoles(role);
          this.tokenService.setName(res.name)
          this.router.navigate(['/home/data'])
        },
        (error: any) => {
          console.log('Error response:', error);
          this.message.create("error", `Tài khoản hoặc mật khẩu không đúng, hoặc người dùng không hoạt động`);
        }
      )
      
    } else {
      this.markFormGroupTouched(this.validateForm);
    }
  }

  markFormGroupTouched(formGroup: FormGroup) {
    Object.values(formGroup.controls).forEach(control => {
      control.markAsDirty();
      control.updateValueAndValidity({ onlySelf: true });
    });
  }


}
