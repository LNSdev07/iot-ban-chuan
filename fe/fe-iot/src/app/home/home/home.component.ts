import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { TokenService } from 'src/app/common/token.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  isCollapsed = false;
  checkRoleAdmin = false;
userName: any;
  constructor(private tokenService: TokenService,
              private router: Router){

  }
  ngOnInit(): void {
    const roles = this.tokenService.getRoles();
    console.log("roles = "+ roles)
    if(roles?.includes("SUPER_ADMIN") || roles?.includes("ADMIN")){
      this.checkRoleAdmin = true;
    }
    this.userName = this.tokenService.getName();
  }

  logout() {
    console.log('call funcion logout')
    this.tokenService.removeDataLocalStorage();
    this.router.navigate(['/auth/login'])
 }
}
