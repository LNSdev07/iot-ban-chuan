import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'environment/envi';
import { LoginReq } from '../model/loginreq';
import { Observable } from 'rxjs';
import { UserModel } from 'src/app/home/user/model/user.model';

@Injectable({
  providedIn: 'root'
})
export class LoginService {
  private URL_AUTH = environment.API_LOCAL +'/auth';
  constructor(private http: HttpClient) { }
  
  public login(req: LoginReq): Observable<any>{
    const finalURL = this.URL_AUTH +"/login"
    return this.http.post(finalURL, req);
  }


}
