package repository;

import com.github.ldriscoll.ektorplucene.LuceneAwareCouchDbConnector;
import domain.Address;
import domain.Patient;
import org.ektorp.CouchDbInstance;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbInstance;
import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.joda.time.LocalDate.now;

public class PatientRepositoryTest {

    private LuceneAwareCouchDbConnector dbConnector;
    private PatientRepository patientRepository;

    @Before
    public void setUp() throws Exception {
        HttpClient httpClient = new StdHttpClient.Builder().build();
        CouchDbInstance dbInstance = new StdCouchDbInstance(httpClient);
        dbConnector = new LuceneAwareCouchDbConnector("lucene-spike-db", dbInstance);
        dbConnector.createDatabaseIfNotExists();
        patientRepository = new PatientRepository(dbConnector);
    }

    @Test
    public void shouldAddPatient(){
        Patient patient = createPatient("1", "name", 21, now());
        patientRepository.add(patient);

        assertNotNull(patient.getId());
        assertEquals(patientRepository.get(patient.getId()), patient);
    }


    @Test
    public void shouldFindPatientByPatientId(){
        Patient patient = createPatient("1", "name", 21, now());
        patientRepository.add(patient);

        Map<String, String> queryParams = new HashMap();
        queryParams.put("patientId", "1");
        List<Patient> patients = patientRepository.find(queryParams);

        assertEquals(patients.size(), 1);
        assertEquals(patients.get(0), patient);
    }

    @Test
    public void shouldFindPatientByPatientName(){
        Patient patient1 = createPatient("1", "name1", 11, now());
        Patient patient2 = createPatient("2", "name2", 22, now());
        Patient patient3 = createPatient("3", "name3", 32, now());
        Patient patient4 = createPatient("4", "name4", 41, now());
        Patient patient5 = createPatient("5", "name5", 58, now());

        patientRepository.add(patient1);
        patientRepository.add(patient2);
        patientRepository.add(patient3);
        patientRepository.add(patient4);
        patientRepository.add(patient5);

        Map<String, String> queryParams = new HashMap();
        queryParams.put("name", "name3");
        List<Patient> patients = patientRepository.find(queryParams);

        assertEquals(patients.size(), 1);
        assertEquals(patients.get(0), patient3);
    }

    @Test
    public void shouldFindPatientByPatientAgeAndDateRange(){
        Patient patient1 = createPatient("1", "name1", 22, LocalDate.parse("2010-01-01"));
        Patient patient2 = createPatient("2", "name2", 22, LocalDate.parse("2010-02-01"));
        Patient patient3 = createPatient("3", "name3", 22, LocalDate.parse("2010-03-01"));
        Patient patient4 = createPatient("4", "name4", 22, LocalDate.parse("2010-04-01"));
        Patient patient5 = createPatient("5", "name5", 22, LocalDate.parse("2010-05-01"));

        patientRepository.add(patient1);
        patientRepository.add(patient2);
        patientRepository.add(patient3);
        patientRepository.add(patient4);
        patientRepository.add(patient5);

        Map<String, String> queryParams = new HashMap();
        queryParams.put("age", "22");
        queryParams.put("dob<date>", "[2010-02-01 TO 2010-04-30]");
        List<Patient> patients = patientRepository.find(queryParams);

        assertEquals(3, patients.size());
        assertEquals(patient2, patients.get(0));
        assertEquals(patient3, patients.get(1));
        assertEquals(patient4, patients.get(2));
    }

    @Test
    @Ignore
    public void shouldFindPatientByAddress(){
        Patient patient1 = createPatient("1", "name1", 22, LocalDate.parse("2010-01-01")).withAddresses(new Address("addr2", "street2", "city2", "state1"));
        Patient patient2 = createPatient("2", "name2", 22, LocalDate.parse("2010-02-01")).withAddresses(new Address("addr2", "street2", "city2", "state2"));
        //Patient patient3 = createPatient("3", "name3", 22, LocalDate.parse("2010-03-01"));
        patientRepository.add(patient1);
        patientRepository.add(patient2);
        //patientRepository.add(patient3);

        Map<String, String> queryParams = new HashMap();
        queryParams.put("state", "state2");
        List<Patient> patients = patientRepository.find(queryParams);

        assertEquals(1, patients.size());
        assertEquals(patient2, patients.get(0));
    }



    private Patient createPatient(String patientId, String name, int age, LocalDate dob) {
        Patient patient = new Patient();
        patient.setName(name);
        patient.setPatientId(patientId);
        patient.setDob(dob);
        patient.setAge(age);
        return patient;
    }

    @After
    public void tearDown()  {
        List<Patient> all = patientRepository.getAll();
        for(Patient patient : all){
            patientRepository.remove(patient);
        }
    }
}
