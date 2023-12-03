import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { EventControlReq } from '../model/eventdevice';
import { environment } from 'environment/envi';
import { Observable, tap } from 'rxjs';
import { ConfigAlertDeviceReq } from '../model/configdevice';
import { ConfigAutoAlertReq } from '../model/configauto';

@Injectable({
  providedIn: 'root'
})
export class ActionService {

  private URL_GET_ALL_DATA = environment.API_LOCAL +'/event-control';

  constructor(private http: HttpClient) { }

  public changeStatusDevice(req: EventControlReq): Observable<any>{
      const finalURL = this.URL_GET_ALL_DATA+"/change-status-sensor";
      console.log(finalURL)
      return this.http.post<any>(finalURL, req);
  }

  public configDevice(req: ConfigAlertDeviceReq): Observable<any>{
    console.log('goi vao service with type = '+ req.deviceType)
    const finalURL = this.URL_GET_ALL_DATA +"/config-alert-for-device"
    return this.http.post<any>(finalURL, req);
  }

  public findAllDevice(): Observable<any>{
    const finalURL = this.URL_GET_ALL_DATA +"/find-all-action"
    return this.http.get(finalURL);
  }

  public configAuto(req: ConfigAutoAlertReq): Observable<any>{
    const finalURL = this.URL_GET_ALL_DATA +"/config-automatic-alert"
    return this.http.post(finalURL, req);
  }
}