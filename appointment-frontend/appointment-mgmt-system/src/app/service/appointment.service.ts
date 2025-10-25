import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Appointment } from '../model/Appointment.model';

@Injectable({
  providedIn: 'root'
})
export class AppointmentService {

  APIURL = "http://localhost:8080/api/appointments";

  constructor(private http: HttpClient) { }

  get() : Observable<any>{
    return this.http.get(this.APIURL);
  }

  save(appointment: Appointment) : Observable<any> {
    return this.http.post(this.APIURL, appointment);
  }

  update(id : any,appointment: any) : Observable<any> {
    return this.http.put(`${this.APIURL}/${id}`, appointment);
  }

  delete(id: any) : Observable<any> {
    return this.http.delete(`${this.APIURL}/${id}`)
  }

}
