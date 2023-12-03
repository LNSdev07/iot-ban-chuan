import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'environment/envi';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class DataServiceService {
  private URL_GET_ALL_DATA = environment.API_LOCAL +'/get-all-data';

  constructor(private http: HttpClient) { }

  public findAllData(pageIndex: any, pageSize: any,  sortBy: any, direction: any, begin: any,
    end: any, water: any, horn: any): Observable<any>{

      console.log("page index " + pageIndex)
      console.log("page size " + pageSize)
      console.log("sortBy " + sortBy)
      console.log("sortBy " + sortBy)
      console.log("direction " + direction)
      console.log("begin " + begin)
      console.log("end " + end)
      console.log("water " + water)
      console.log("horn " + horn)

      const urlParams = [
        `page=${Number(pageIndex)}`,
        pageSize !== null ? `pageSize=${Number(pageSize)}` : '',
        sortBy !== null ? `sortBy=${sortBy}` : '',
        direction !== null ? `direction=${direction}` : '',
        begin !== null ? `begin=${Number(begin)}` : '',
        end !== null ? `end=${Number(end)}` : '',
        water !== null ? `waterStatus=${Number(water)}` : '',
        horn !== null ? `hornStatus=${Number(horn)}` : ''
      ];

      const finalURL = this.URL_GET_ALL_DATA + '?' + urlParams.filter(param => param !== '').join('&');
     
      return this.http.get<any>(finalURL)
      .pipe(data => {
           console.log("data = "+ data)
          return data;
      });
  }
}