import { Injectable } from '@angular/core';
import {ApiService} from "./api.service";
import {Observable} from "rxjs";
import {User} from "../models/User";

@Injectable({
  providedIn: 'root'
})
export class UsersService {

  constructor(
      private apiService: ApiService
  ) { }

  isUserAdmin() : Observable<boolean> {
      return this.apiService.get('/config/isApplicationAdmin');
  }

  getUser(): Observable<User> {
    return this.apiService.get('/getUser');
  }

  setUser(): Observable<boolean> {
    return this.apiService.get('/setUser');
  }
}
