import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'environment/envi';
import { Observable } from 'rxjs';
import { UserModel } from '../model/user.model';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private URL_USER = environment.API_LOCAL +'/user';
  constructor(private http: HttpClient) { }

  public findUser(pageIndex: any, pageSize: any,  sortBy: any, direction: any, begin: any,
    end: any, keyword: any, status: any, role: any): Observable<any>{

      console.log("keyword = "+ keyword);
      console.log("status = "+ status);
      console.log("role = "+ role);
      const urlParams = [
        `page=${Number(pageIndex)}`,
        pageSize !== null ? `pageSize=${Number(pageSize)}` : '',
        sortBy !== null ? `sortBy=${sortBy}` : '',
        direction !== null ? `direction=${direction}` : '',
        begin !== null ? `begin=${Number(begin)}` : '',
        end !== null ? `end=${Number(end)}` : '',
        keyword !== null ? `keyword=${String(keyword)}` : '',
        status !== null ? `status=${Number(status)}` : '',
        role !== null ? `role=${Number(role)}` : ''
      ];

      const finalURL = this.URL_USER+"/find-user" + '?' + urlParams.filter(param => param !== '').join('&');
     
      return this.http.get<any>(finalURL)
      .pipe(data => {
           console.log("data = "+ data)
          return data;
      });
  }


  public createUser(req: UserModel): Observable<any>{
    const finalURL = this.URL_USER +"/create-user"
       return this.http.post(finalURL, req);
  }

  public updateUser(req: UserModel): Observable<any>{
    const finalURL = this.URL_USER +"/update-user"
       return this.http.put(finalURL, req);
  }

  public getDetailUser(username: string):Observable<any>{
    const finalURL = this.URL_USER +"/get-user?username="+ username;
       return this.http.get(finalURL);
  }
}
