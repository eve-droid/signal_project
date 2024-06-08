package data_management;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.List;

public class DataStorageTest {

    public DataStorageTest(){

    }

    @Test
    public void testAddAndGetRecords() {
        DataStorage storage = DataStorage.getInstance();
        storage.addPatientData(1, 100.0, "WhiteBloodCells", 1714376789050L);
        storage.addPatientData(1, 200.0, "WhiteBloodCells", 1714376789051L);
        storage.addPatientData(2, 200.0, "WhiteBloodCells", 1714376789052L);

        int numberOfRecords = storage.totalNumberOfRecord();
        PatientRecord rec = storage.getRecord(6, 1714376789050L);
        assertEquals(null, rec); // Check if null is returned if record is not found

        assertEquals(3, numberOfRecords); // Check if three records are added

        List<PatientRecord> records = storage.getRecords(1, 1714376789050L, 1714376789051L);
        assertEquals(2, records.size()); // Check if two records are retrieved
        assertEquals(100.0, records.get(0).getMeasurementValue(), 0.001); // Validate first record

        List<Patient> patientList = storage.getAllPatients();
        assertEquals(2, patientList.size());//check number of patients
    }

    @Test
    public void testAddAndGetPatients() {
        DataStorage storage = DataStorage.getInstance();

        List<Patient> patientList = storage.getAllPatients();
        assertEquals(2, patientList.size());//check number of patients

        storage.getPatient("Patient ID: 3, 1714376789050, Label: Saturation, Data: 98");
        assertEquals(3, storage.getAllPatients().size());//check number of patients
    }

    @Test
    public void testAddAndGetWrongRecords() {
        DataStorage storage = DataStorage.getInstance();

        List<PatientRecord> records = storage.getRecords(4, 1714376789050L, 1714376789051L);//check no error occur if wrong patientId

        assertEquals(0, records.size());//check if empty array
        
    }
}
