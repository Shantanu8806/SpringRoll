import { Component } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Appointment } from 'src/app/model/Appointment.model';
import { AppointmentService } from 'src/app/service/appointment.service';

@Component({
  selector: 'app-appointment',
  templateUrl: './appointment.component.html',
  styleUrls: ['./appointment.component.css']
})
export class AppointmentComponent {

  appointmentForm = new FormGroup({
    id: new FormControl(0),
    patientName: new FormControl("", [Validators.required]),
    doctorName: new FormControl("", [Validators.required]),
    appointmentDateTime: new FormControl("", [Validators.required]),
    description: new FormControl("", [Validators.required]),
    status: new FormControl("", [Validators.required]),

    
  })
  
  error = '';


  constructor(private appointmentService: AppointmentService,
    private router:Router
  ) { }

  appointments: Appointment[] = [];

  ngOnInit() {
    this.getAppointments();
  }

  isInserted: boolean = true;

  revert() {
    this.isInserted = !this.isInserted;
    this.appointmentForm.patchValue({
      id: 0,
      patientName: "",
      doctorName: "",
      appointmentDateTime: "",
      description: "",
      status: "",
    });
  }

  update() {
    let sampleAppointment: any = this.appointmentForm.value;


    const localdate = sampleAppointment.appointmentDateTime;
    const formatedDate = localdate.replace('T', 'T') + ':00';
    const appointmentData = {

      ...sampleAppointment, appointmentDateTime: formatedDate

    };

    this.appointmentService.update(appointmentData.id, appointmentData).subscribe({
      next: (data) => alert("Appointment Updated successfully"),
      error: (error) => console.log(`Appointment updated successfully`),
      complete: () => { console.log("data updates successfully"); this.getAppointments() }
    })
  }

  updateToPatchInformation(user: any) {
    this.appointmentForm.patchValue(user);
    this.isInserted = !this.isInserted;
  }

  changeDate() {
    let data = this.appointmentForm.value;
    let tempDate = data.appointmentDateTime?.split("T")[0] || new Date().toISOString().split("T")[0];
    let selectedDate = new Date(tempDate?.toString());
    let currentDate = new Date();

    if(currentDate > selectedDate){
      alert("Please select new Date");
      this.appointmentForm.patchValue({
        ...data, appointmentDateTime: ""
      })
      return;
    }

  }

  insert() {
    console.log(this.appointmentForm.value);
    let appointment: any = this.appointmentForm.value;
    const localdate = appointment.appointmentDateTime;
    const formatedDate = localdate.replace('T', 'T') + ':00';

    const appointmentData = {

      ...appointment, appointmentDateTime: formatedDate

    };
    delete appointmentData.id;
    

    this.appointmentService.save(appointmentData).subscribe({
      next: () => {
        alert("Appointment created successfully");
        this.router.navigate(['']);
      },
      error: (err) => {
        //this.error = typeof err.error === 'string'? err.error:err.error || 'Failed to add Appointments';
        console.log(this.error);
      },
      complete: () => { console.log("data created successfully"); this.getAppointments() }
    })

  }

  delete(id: any) {

    let temp = confirm("Are you sure you want to delete?");
    if (!temp) {
      return;
    }
    this.appointmentService.delete(id).subscribe({
      next: (data: any) => alert("Appointment has been Deleted"),
      error: (error: any) => console.log(error),
      complete: () => { console.log("Data deleted successfully"); this.getAppointments() }
    })
  }



  private getAppointments() {
    this.appointmentService.get().subscribe({
      next: (data: any) => { this.appointments = data; console.log(this.appointments) },
      error: (error) => console.log(error),
      complete: () => console.log("Data retrieved successfully")
    });
  }
}
