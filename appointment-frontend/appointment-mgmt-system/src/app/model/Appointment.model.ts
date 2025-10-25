export interface Appointment {
    id?: number;
    patientName: string;
    doctorName: string;
    appointmentDateTime: string; 
    description: string;
    status:'SCHEDULED'|'COMPLETED'|'CANCELLED';
}