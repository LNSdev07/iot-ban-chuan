import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
  HttpErrorResponse,
  HttpResponse
} from '@angular/common/http';
import { Observable, catchError, map, throwError } from 'rxjs';
import { TokenService } from './token.service';
import { Router } from '@angular/router';

@Injectable()
export class InterceptorInterceptor implements HttpInterceptor {

  constructor(private tokenService: TokenService,
              private router: Router) {}

  // intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
  //   return next.handle(request);
  // }

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    console.log('Đã đính token(JWT) vào request!') 
    let authRequest = request;
    const token = this.tokenService.getToken();
    if(token != null){
      authRequest = request.clone(
        {
          setHeaders:{
            Authorization:  `Bearer ${token}`
          }
        })
    }
    return next.handle(authRequest).pipe(
      map((event: HttpEvent<any>) => {
        if (event instanceof HttpResponse) {
          // Trích xuất giá trị từ response
          const httpCode = event.body.status;
          if(event.status === 401 || httpCode === 401) this.redirectToLogin(); // token het han
        }
        return event;
      }),
      catchError((error: HttpErrorResponse) => {
        // Xử lý lỗi
        console.log(`HTTP code: ${error.status}, Message: ${error.error.message}, Data: ${error.error.data}`);
        console.log( error.error.error)
        if(error.error.error === 'Unauthorized') {
          this.redirectToLogin(); // token het han
        }
        return throwError(error);
      })
    );
  }

  redirectToLogin() {
      this.router.navigate(['/auth/login'])
  }
}
