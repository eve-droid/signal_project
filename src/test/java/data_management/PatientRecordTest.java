package data_management;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.data_management.PatientRecord;

public class PatientRecordTest {
    
    @Test
    public void testAddAndRetrieveRecordData(){

        PatientRecord record = new PatientRecord(3, 85, "Saturation", 1715250000000L);

        assertEquals(3, record.getPatientId());
        assertEquals(85, record.getMeasurementValue(), 0.0);
        assertEquals("Saturation", record.getRecordType());
        assertEquals(1715250000000L, record.getTimestamp());
    }
}
