package data_management;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import com.data_management.Patient;
import com.data_management.PatientRecord;

public class PatientTest {
    
    @Test
    public void addAndGetRecord(){

        Patient patient = new Patient(123);

        patient.addRecord(75.0, "DiaslisticPressure", 123456789987L);
        patient.addRecord(98.0, "Saturation", 123456789123L);
        patient.addRecord(125.0, "SystolicPressure", 123456789234L);

        List <PatientRecord> patientRecord = patient.getRecords(123456789123L, 123456789987L);

        assertEquals(3, patientRecord.size());
    }
}
