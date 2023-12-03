import { Injectable } from '@angular/core';
import { environment } from 'environment/envi';

@Injectable({
  providedIn: 'root'
})
export class TokenService {
  private roles! : string[]
  constructor() { }

  public setId(id: string){
    localStorage.removeItem(environment.ID)
    localStorage.setItem(environment.ID, id)
  }

  public getID(){
    return localStorage.getItem(environment.ID)
  }

  public setToken(token: string){
    localStorage.removeItem(environment.TOKEN_KEY);
    localStorage.setItem(environment.TOKEN_KEY, token);
  };
  
  public getToken(): string | null {
    return localStorage.getItem(environment.TOKEN_KEY);
  }

  public setRefreshToken(token: string){
    localStorage.removeItem(environment.REFRESH_TOKEN_KEY);
    localStorage.setItem(environment.REFRESH_TOKEN_KEY, token);
  }

  public getRefreshToken(){
    return localStorage.getItem(environment.REFRESH_TOKEN_KEY);
  }

  public setName(name: string){
    localStorage.removeItem(environment.NAME_KEY);
    localStorage.setItem(environment.NAME_KEY, name);
  }

  public getName(): string | null{
    return localStorage.getItem(environment.NAME_KEY);
  }

  public setAvatar(avatar: string){
    localStorage.removeItem(environment.AVATAR_KEY);
    localStorage.setItem(environment.AVATAR_KEY, avatar);
  }

  public getAvatar(): string | null {
    return localStorage.getItem(environment.AVATAR_KEY);
  }

  public setRoles(roles: string[]){
    localStorage.removeItem(environment.ROLE_KEY);
    localStorage.setItem(environment.ROLE_KEY, JSON.stringify(roles));
  }

  public getRoles(): string|null {
   const role =  localStorage.getItem(environment.ROLE_KEY);
   return role;
  }

  public removeDataLocalStorage(){
    localStorage.removeItem(environment.AVATAR_KEY)
    localStorage.removeItem(environment.NAME_KEY)
    localStorage.removeItem(environment.REFRESH_TOKEN_KEY)
    localStorage.removeItem(environment.ROLE_KEY)
    localStorage.removeItem(environment.TOKEN_KEY)
    localStorage.removeItem(environment.ID)
  }
}
